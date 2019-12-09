package org.matsim.drt;

import org.matsim.api.core.v01.population.Plan;

interface ReservationDecision {

    boolean isReservationRequested(Plan plan);

    double determineReservationValidityEnd(Plan plan);
}
