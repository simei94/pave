package org.matsim.drt;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.core.router.TripStructureUtils;

import java.util.List;

final class DefaultReservationDecision implements ReservationDecision {
    @Override
    public boolean isReservationRequested(Plan plan) {
        return plan.getPerson().getId().toString().contains("freight");
    }

    /**
     * retrieves the planned arrival time of the last drt leg in the given plan
     *
     * @param plan
     * @return
     */
    @Override
    public double determineReservationValidityEnd(Plan plan) {
        if(! isReservationRequested(plan) ) throw new IllegalStateException();

        List<Leg> legs = TripStructureUtils.getLegs(plan);
        Leg lastDrtLeg = null;
        for (int i = legs.size() - 1; i > 0; i--) {
            Leg leg = legs.get(i);
            if(TripStructureUtils.getRoutingMode(leg).equals(TransportMode.drt)){
                lastDrtLeg = leg;
                break;
            }
        }
        if(lastDrtLeg == null) throw new IllegalStateException();
        return lastDrtLeg.getDepartureTime() + lastDrtLeg.getRoute().getTravelTime();
    }
}
