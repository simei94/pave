package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.dvrp.optimizer.Request;

final class DrtReservationRequest extends DrtRequest {
    private final double endOfReservationValidity;

    DrtReservationRequest(Id<Request> id, Id<Person> passengerId, String mode, Link fromLink, Link toLink, double earliestStartTime, double latestStartTime, double latestArrivalTime, double submissionTime, double endOfReservationValidity) {
        super(id, passengerId, mode, fromLink, toLink, earliestStartTime, latestStartTime, latestArrivalTime, submissionTime);
        this.endOfReservationValidity = endOfReservationValidity;
    }

    double getEndOfReservationValidity() {
        return endOfReservationValidity;
    }
}
