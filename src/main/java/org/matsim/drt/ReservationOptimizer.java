package org.matsim.drt;

import org.matsim.contrib.drt.optimizer.DrtOptimizer;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

interface ReservationOptimizer extends DrtOptimizer {

    /**
     * this method is called every time a reservation request is submitted so that the ReservationOptimizer is notified of it
     * @param reservation
     */
    void reservationSubmitted(Reservation reservation);

    boolean isVehicleReserved(DvrpVehicle vehicle);

}
