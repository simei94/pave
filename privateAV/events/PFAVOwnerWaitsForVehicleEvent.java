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

package privateAV.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

import java.util.Map;

public class PFAVOwnerWaitsForVehicleEvent extends Event {

    static final String EVENT_TYPE = "PFAVOwner waits";

    static final String ATTRIBUTE_OWNER = "owner";
    static final String ATTRIBUTE_VEHICLE = "vehicle";

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Person> owner;

    public PFAVOwnerWaitsForVehicleEvent(double time, Id<DvrpVehicle> vehicleId, Id<Person> owner) {
        super(time);
        this.vehicleId = vehicleId;
        this.owner = owner;
    }

    /**
     * @return a unique, descriptive name for this event type, used to identify event types in files.
     */
    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_OWNER, owner + "");
        attr.put(ATTRIBUTE_VEHICLE, vehicleId + "");
        return attr;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public Id<Person> getOwner() {
        return owner;
    }
}
