package org.matsim.drt;

import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.core.mobsim.framework.MobsimPassengerAgent;

interface ReservationDecision {

    boolean isReservationRequested(MobsimPassengerAgent requestingAgent);
}
