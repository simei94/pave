package org.matsim.drt;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.insertion.*;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.scheduler.RequestInsertionScheduler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.passenger.PassengerRequestAcceptedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeCleanupEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimBeforeCleanupListener;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

class ReservedVehiclePoolRequestInserter implements UnplannedRequestInserter, MobsimBeforeCleanupListener {
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

    private Map<Id<Person>,DvrpVehicle> reservedVehicles = new HashMap<>();
    private ReservationDecision reservationDecision;

    ReservedVehiclePoolRequestInserter(DrtConfigGroup drtCfg, Fleet fleet, MobsimTimer mobsimTimer,
                                              EventsManager eventsManager, RequestInsertionScheduler insertionScheduler,
                                              VehicleData.EntryFactory vehicleDataEntryFactory, PrecalculablePathDataProvider pathDataProvider,
                                              InsertionCostCalculator.PenaltyCalculator penaltyCalculator) {
        this.drtCfg = drtCfg;
        this.fleet = fleet;
        this.mobsimTimer = mobsimTimer;
        this.eventsManager = eventsManager;
        this.insertionScheduler = insertionScheduler;
        this.vehicleDataEntryFactory = vehicleDataEntryFactory;

        forkJoinPool = new ForkJoinPool(drtCfg.getNumberOfThreads());
        insertionProblem = new ParallelMultiVehicleInsertionProblem(pathDataProvider, drtCfg, mobsimTimer, forkJoinPool,
                penaltyCalculator);
        insertionScheduler.initSchedules();
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

        Set<DrtReservationRequest> reservationRequests = new HashSet<>();
        unplannedRequests.stream()
                .filter(req  -> req instanceof DrtReservationRequest)
                .forEach(req -> reservationRequests.add((DrtReservationRequest) req));
        unplannedRequests.removeAll(reservationRequests);

        //handle non-reserving requests first. this means, priority remains on those requests, that do not want to block the vehicle for a longer period.
        //reservation requests only can be assigned to vehicles which are idle after handling non-reserving requests
        handleConventionalRequests(unplannedRequests);
        handleReservationRequests(reservationRequests);
    }

    private void handleConventionalRequests(Collection<DrtRequest> unplannedRequests) {
        //exclude reserved vehicles
        VehicleData vData = new VehicleData(mobsimTimer.getTimeOfDay(), fleet.getVehicles().values().stream().filter(v -> ! this.reservedVehicles.values().contains(v)),
                vehicleDataEntryFactory, forkJoinPool);
        Iterator<DrtRequest> reqIter = unplannedRequests.iterator();
        while (reqIter.hasNext()) {
            DrtRequest req = reqIter.next();
            tryToAssignAndReturnVehicle(vData,req);
            reqIter.remove();
        }
    }

    private void handleReservationRequests(Collection<DrtReservationRequest> unplannedReservationRequests){
        VehicleData vData = new ReservingVehicleData(mobsimTimer.getTimeOfDay(), fleet.getVehicles().values().stream().filter(v -> ! this.reservedVehicles.values().contains(v)),
                vehicleDataEntryFactory, forkJoinPool);
        for (DrtReservationRequest unplannedReservationRequest : unplannedReservationRequests) {
            if(this.reservedVehicles.containsKey(unplannedReservationRequest.getPassengerId())){
                DvrpVehicle assignedVehicle = this.reservedVehicles.get(unplannedReservationRequest.getPassengerId());
                //this is a horrible hack: we create a stream with only one vehicle in it.
                //need this  because the insertionScheduler only takes insertionWithPathData and i yet do not know how to create this..
                VehicleData vehicleDataWithOneVehicle = new ReservingVehicleData(mobsimTimer.getTimeOfDay(), Stream.of(assignedVehicle), vehicleDataEntryFactory, forkJoinPool);
                if (tryToAssignAndReturnVehicle(vehicleDataWithOneVehicle, unplannedReservationRequest) == null){
                    throw new RuntimeException("there was a registration for vehicle " + assignedVehicle
                            + " but somehow, the request of " + unplannedReservationRequest.getPassengerId()
                            + " could not be inserted");
                }
            } else{
                //also not nice. we end up creating vehicle data lots of times per time step instead of once...
                DvrpVehicle vehicle = tryToAssignAndReturnVehicle(vData, unplannedReservationRequest);
                if(vehicle != null) this.reservedVehicles.put(unplannedReservationRequest.getPassengerId(), vehicle);
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
