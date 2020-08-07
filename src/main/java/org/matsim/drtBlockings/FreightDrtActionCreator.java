/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.drtBlockings;

import org.matsim.contrib.drt.vrpagent.DrtActionCreator;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.passenger.PassengerEngine;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.tracker.OnlineTrackerListener;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentLogic;
import org.matsim.contrib.dvrp.vrpagent.VrpLegFactory;
import org.matsim.contrib.dynagent.DynAction;
import org.matsim.contrib.dynagent.DynAgent;
import org.matsim.contrib.dynagent.IdleDynActivity;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.drtBlockings.tasks.FreightDeliveryTask;
import org.matsim.drtBlockings.tasks.FreightPickupTask;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;

class FreightDrtActionCreator implements VrpAgentLogic.DynActionCreator {

    private final VrpLegFactory legFactory;
    private final PassengerEngine passengerEngine;
    private final DrtActionCreator delegate;

    FreightDrtActionCreator(PassengerEngine passengerEngine, MobsimTimer timer, DvrpConfigGroup dvrpCfg) {
        this(passengerEngine, v -> VrpLegFactory.createWithOnlineTracker(dvrpCfg.getMobsimMode(), v,
                OnlineTrackerListener.NO_LISTENER, timer));
    }

    private FreightDrtActionCreator(PassengerEngine passengerEngine, VrpLegFactory legFactory) {
        this.passengerEngine = passengerEngine;
        this.legFactory = legFactory;
        this.delegate = new DrtActionCreator(this.passengerEngine, this.legFactory);
    }


    @Override
    public DynAction createAction(DynAgent dynAgent, DvrpVehicle vehicle, double now) {
        Task currentTask = vehicle.getSchedule().getCurrentTask();

        //we can use IdleDynActivity even though vehicle is not idle. The object type only
        //prohibits to alter the activity and time...

        if(currentTask.getTaskType().equals(FreightDeliveryTask.FREIGHT_DELIVERY_TASK_TYPE))
                return new IdleDynActivity("FreightDrtDelivery", currentTask::getEndTime);

        if(currentTask.getTaskType().equals(FreightPickupTask.FREIGHT_PICKUP_TASK_TYPE.name()))
                return new IdleDynActivity("FreightDrtPickup", currentTask::getEndTime);

        if(currentTask.getTaskType().equals(FreightRetoolTask.RETOOL_TASK_TYPE.name()))
                return new IdleDynActivity("FreightDrtRetooling", currentTask::getEndTime);

        else return delegate.createAction(dynAgent, vehicle, now);
    }

}
