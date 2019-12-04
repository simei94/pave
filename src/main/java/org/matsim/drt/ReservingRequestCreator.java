package org.matsim.drt;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.drt.passenger.DrtRequestCreator;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.passenger.PassengerRequest;
import org.matsim.contrib.dvrp.passenger.PassengerRequestCreator;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimPassengerAgent;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.PlanAgent;

final class ReservingRequestCreator implements PassengerRequestCreator {
    private static final Logger log = Logger.getLogger(ReservingRequestCreator.class);
    private final String mode;
    private final EventsManager eventsManager;
    private final MobsimTimer timer;

    DrtRequestCreator delegate;
    private ReservationDecision reservationDecision;

    ReservingRequestCreator(String mode, EventsManager eventsManager, MobsimTimer timer, ReservationDecision reservationDecision) {
        this.delegate = new DrtRequestCreator(mode,eventsManager,timer);
        this.mode = mode;
        this.eventsManager = eventsManager;
        this.timer = timer;
        this.reservationDecision = reservationDecision;
    }

    @Override
    public PassengerRequest createRequest(Id<Request> id, MobsimPassengerAgent passenger, Link fromLink, Link toLink, double departureTime, double submissionTime) {
        if(reservationDecision.isReservationRequested(passenger)){

            //FIXME this will not work if pre-booking is allowed in DRT
            Leg leg = (Leg)((PlanAgent)passenger).getCurrentPlanElement();

//		((PlanAgent) passenger).getCurrentPlan().getPlanElements().
            DrtRoute drtRoute = (DrtRoute)leg.getRoute();
            double latestDepartureTime = departureTime + drtRoute.getMaxWaitTime();
            double latestArrivalTime = departureTime + drtRoute.getTravelTime();

            log.debug("");
            log.debug(timer.getTimeOfDay() + " ");
            log.debug(mode + " ");
            log.debug(id + " ");
            log.debug(passenger.getId() + " ");
            log.debug(fromLink.getId() + " ");
            log.debug(toLink.getId() + " ");
            log.debug(drtRoute.getDirectRideTime() + " ");
            log.debug(drtRoute.getDistance() + " ");
            log.debug("");

            eventsManager.processEvent(
                    new DrtRequestSubmittedEvent(timer.getTimeOfDay(), mode, id, passenger.getId(), fromLink.getId(),
                            toLink.getId(), drtRoute.getDirectRideTime(), drtRoute.getDistance()));

            // TODO: get the reservation validity time period. For instance, actStart time of first act after the end of the current sequence of act -> drt trip
            double reservationValidity = Double.NEGATIVE_INFINITY;

            return new DrtReservationRequest(id, passenger.getId(), mode, fromLink, toLink, departureTime, latestDepartureTime,
                    latestArrivalTime, submissionTime, reservationValidity);

        } else{
            return delegate.createRequest(id, passenger, fromLink, toLink, departureTime, submissionTime);
        }
    }
}
