package org.matsim.drt;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.insertion.*;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.scheduler.DrtScheduleInquiry;
import org.matsim.contrib.drt.scheduler.RequestInsertionScheduler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.passenger.PassengerRequestAcceptedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEvent;
import org.matsim.contrib.dvrp.schedule.ScheduleInquiry;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeCleanupEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeCleanupListener;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

final class ReservedVehiclePoolRequestInserter implements UnplannedRequestInserter, MobsimBeforeCleanupListener {
    private static final Logger log = Logger.getLogger(DefaultUnplannedRequestInserter.class);
    static final String NO_INSERTION_FOUND_CAUSE = "no_insertion_found";

    private final DrtConfigGroup drtCfg;
    private final Fleet fleet;
    private final MobsimTimer mobsimTimer;
    private final EventsManager eventsManager;
    private final RequestInsertionScheduler insertionScheduler;
    private final VehicleData.EntryFactory vehicleDataEntryFactory;

    private final ForkJoinPool forkJoinPool;
    private final ParallelMultiVehicleInsertionProblem insertionProblem;
    private final DrtScheduleInquiry scheduleInquiry;

    private Map<DrtReservationRequest,DvrpVehicle> reservedVehicles;

    ReservedVehiclePoolRequestInserter(DrtConfigGroup drtCfg, Fleet fleet, MobsimTimer mobsimTimer,
                                       EventsManager eventsManager, RequestInsertionScheduler insertionScheduler,
                                       VehicleData.EntryFactory vehicleDataEntryFactory, PrecalculablePathDataProvider pathDataProvider,
                                       InsertionCostCalculator.PenaltyCalculator penaltyCalculator, DrtScheduleInquiry scheduleInquiry) {
        this.drtCfg = drtCfg;
        this.fleet = fleet;
        this.mobsimTimer = mobsimTimer;
        this.eventsManager = eventsManager;
        this.insertionScheduler = insertionScheduler;
        this.vehicleDataEntryFactory = vehicleDataEntryFactory;
        this.scheduleInquiry = scheduleInquiry;

        this.forkJoinPool = new ForkJoinPool(drtCfg.getNumberOfThreads());
        this.insertionProblem = new ParallelMultiVehicleInsertionProblem(pathDataProvider, drtCfg, mobsimTimer, forkJoinPool,
                penaltyCalculator);

        //sort Reservations by their validity end time stamps
        this.reservedVehicles = new TreeMap<>(Comparator.comparingDouble(DrtReservationRequest::getEndOfReservationValidity));
    }

    @Override
    public void notifyMobsimBeforeCleanup(@SuppressWarnings("rawtypes") MobsimBeforeCleanupEvent e) {
        insertionProblem.shutdown();
    }

    @Override
    public void scheduleUnplannedRequests(Collection<DrtRequest> unplannedRequests) {
        if (unplannedRequests.isEmpty()) {
            return;
        }

        //check for reservation validity end time stamps and get all vehicles back for which reservation is expired.
        unReserveVehicles();

        Set<DrtReservationRequest> reservationRequests = new HashSet<>();
        unplannedRequests.stream()
                .filter(req  -> req instanceof DrtReservationRequest)
                .forEach(req -> reservationRequests.add((DrtReservationRequest) req));

        Set<DrtRequest> conventionalRequests = new HashSet<>(unplannedRequests);
        conventionalRequests.removeAll(reservationRequests);

        //handle non-reserving requests first. this means, priority remains on those requests, that do not want to block the vehicle for a longer period.
        //reservation requests only can be assigned to vehicles which are idle after handling non-reserving requests
        handleConventionalRequests(conventionalRequests);
        unplannedRequests.removeAll(conventionalRequests); //maybe better: remove handled requests one by one from the unplannedRequests (inside the handling method) ??
        handleReservationRequests(reservationRequests);
        unplannedRequests.removeAll(reservationRequests); //maybe better: remove handled requests one by one from the unplannedRequests (inside the handling method) ??
    }

    private void unReserveVehicles(){
        if(reservedVehicles.isEmpty()) return;
        List<DrtReservationRequest> activeReservations = new ArrayList<>(reservedVehicles.keySet());
        for (DrtReservationRequest activeReservation : activeReservations) {
            if(activeReservation.getEndOfReservationValidity() <= mobsimTimer.getTimeOfDay()) this.reservedVehicles.remove(activeReservation);
            else break;
        }
    }

    private void handleConventionalRequests(Collection<DrtRequest> unplannedRequests) {
        //exclude reserved vehicles
        VehicleData vData = new VehicleData(mobsimTimer.getTimeOfDay(), fleet.getVehicles().values().stream().filter(v -> ! this.reservedVehicles.containsValue(v)),
                vehicleDataEntryFactory, forkJoinPool);
        for (DrtRequest unplannedRequest : unplannedRequests) {
            tryToAssignAndReturnVehicle(vData, unplannedRequest);
        }
    }

    private void handleReservationRequests(Collection<DrtReservationRequest> unplannedReservationRequests){
        for(DrtReservationRequest unplannedReservationRequest : unplannedReservationRequests){
            //check if passenger has an active reservation
            DrtReservationRequest reservingRequest = this.reservedVehicles.keySet().stream().filter(req -> req.getPassengerId().equals(unplannedReservationRequest.getPassengerId())).findAny().orElse(null);
            if(reservingRequest != null){
                DvrpVehicle assignedVehicle = this.reservedVehicles.get(reservingRequest);
                //this is a horrible hack: we create a stream with only one vehicle in it.
                //need this  because the insertionScheduler only takes insertionWithPathData and i yet do not know how to create this..
                VehicleData vehicleDataWithOneVehicle = new ReservingVehicleData(mobsimTimer.getTimeOfDay(), Stream.of(assignedVehicle), vehicleDataEntryFactory, forkJoinPool);
                if (tryToAssignAndReturnVehicle(vehicleDataWithOneVehicle, unplannedReservationRequest) == null){
                    throw new RuntimeException("there was a registration for vehicle " + assignedVehicle
                            + " but somehow, the request of " + unplannedReservationRequest.getPassengerId()
                            + " could not be inserted");
                }
            } else{
                VehicleData vData = new ReservingVehicleData(mobsimTimer.getTimeOfDay(),
                        fleet.getVehicles().values().stream().filter(v -> ! this.reservedVehicles.containsValue(v) ).filter(scheduleInquiry::isIdle),
                        vehicleDataEntryFactory, forkJoinPool);
                DvrpVehicle vehicle = tryToAssignAndReturnVehicle(vData, unplannedReservationRequest);
                if(vehicle != null) this.reservedVehicles.put(unplannedReservationRequest, vehicle);
            }
        }
    }


    /**
     *
     * @param vData
     * @param req
     * @return assigned DvrpVehicle or null if no insertion could be found
     */
    private DvrpVehicle tryToAssignAndReturnVehicle(VehicleData vData, DrtRequest req) {

        Optional<SingleVehicleInsertionProblem.BestInsertion> best = insertionProblem.findBestInsertion(req, vData.getEntries());
        if (!best.isPresent()) {
            eventsManager.processEvent(
                    new PassengerRequestRejectedEvent(mobsimTimer.getTimeOfDay(), drtCfg.getMode(), req.getId(),
                            req.getPassengerId(), NO_INSERTION_FOUND_CAUSE));
            log.warn("No insertion found for drt request "
                    + req
                    + " from passenger id="
                    + req.getPassengerId()
                    + " fromLinkId="
                    + req.getFromLink().getId());
            return null;
        } else {
            eventsManager.processEvent(
                    new PassengerRequestAcceptedEvent(mobsimTimer.getTimeOfDay(), drtCfg.getMode(), req.getId(),
                            req.getPassengerId()));
            SingleVehicleInsertionProblem.BestInsertion bestInsertion = best.get();
            insertionScheduler.scheduleRequest(bestInsertion.vehicleEntry, req, bestInsertion.insertion);

            vData.updateEntry(bestInsertion.vehicleEntry.vehicle);
            eventsManager.processEvent(
                    new PassengerRequestScheduledEvent(mobsimTimer.getTimeOfDay(), drtCfg.getMode(), req.getId(),
                            req.getPassengerId(), bestInsertion.vehicleEntry.vehicle.getId(),
                            req.getPickupTask().getEndTime(), req.getDropoffTask().getBeginTime()));
            return bestInsertion.vehicleEntry.vehicle;
        }
    }
}
