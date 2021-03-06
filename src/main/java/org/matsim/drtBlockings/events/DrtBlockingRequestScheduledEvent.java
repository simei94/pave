/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.drtBlockings.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierVehicle;

import java.util.Map;

public class DrtBlockingRequestScheduledEvent extends Event {

    public static final String EVENT_TYPE = "DrtBlockingRequest scheduled";
    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_CARRIER = "carrier";
    public static final String ATTRIBUTE_CARRIERVEHICLE = "carrierVehicle";
    public static final String ATTRIBUTE_REQUEST = "request";
    private final Id<Request> requestId;

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Carrier> carrierId;
    private final Id<CarrierVehicle> carrierVehicleId;

    public DrtBlockingRequestScheduledEvent(double timeOfDay, Id<Request> requestId, Id<Carrier> carrierId, Id<CarrierVehicle> carrierVehicleId, Id<DvrpVehicle> vehicleId) {
        super(timeOfDay);
        this.requestId = requestId;
        this.vehicleId = vehicleId;
        this.carrierId = carrierId;
        this.carrierVehicleId = carrierVehicleId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_REQUEST, requestId + "");
        attr.put(ATTRIBUTE_CARRIER, carrierId + "");
        attr.put(ATTRIBUTE_CARRIERVEHICLE, carrierVehicleId + "");
        attr.put(ATTRIBUTE_VEHICLE, vehicleId + "");
        return attr;
    }

    public Id<Request> getRequestId() {
        return requestId;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public Id<Carrier> getCarrierId() { return carrierId; }

    public Id<CarrierVehicle> getCarrierVehicleId() { return carrierVehicleId; }

    public static DrtBlockingRequestScheduledEvent convert(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();
        double time = Double.parseDouble(attributes.get(ATTRIBUTE_TIME));
        Id<Request> requestId = Id.create(attributes.get(ATTRIBUTE_REQUEST), Request.class);
        Id<Carrier> carrierId = Id.create(attributes.get(ATTRIBUTE_CARRIER), Carrier.class);
        Id<CarrierVehicle> carrierVehicleId = Id.create(attributes.get(ATTRIBUTE_CARRIERVEHICLE), CarrierVehicle.class);
        Id<DvrpVehicle> vehicleId = Id.create(attributes.get(ATTRIBUTE_VEHICLE), DvrpVehicle.class);

        return new DrtBlockingRequestScheduledEvent(time, requestId, carrierId, carrierVehicleId, vehicleId);
    }
}
