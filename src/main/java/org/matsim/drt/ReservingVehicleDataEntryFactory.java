package org.matsim.drt;

import com.google.inject.Provider;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.VehicleDataEntryFactoryImpl;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.schedule.DrtStopTask;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.edrt.optimizer.EDrtVehicleDataEntryFactory;
import org.matsim.core.config.Config;

import javax.inject.Inject;

class ReservingVehicleDataEntryFactory  implements VehicleData.EntryFactory {

    private final VehicleDataEntryFactoryImpl delegate;

    ReservingVehicleDataEntryFactory(DrtConfigGroup drtCfg) {
        this.delegate = new VehicleDataEntryFactoryImpl(drtCfg);
    }

    @Override
    public VehicleData.Entry create(DvrpVehicle vehicle, double currentTime) {
        if(!delegate.isEligibleForRequestInsertion(vehicle, currentTime)) return null;

        //if one of the requests at the last stop is a reservation dropOff, exclude this vehicle
        Task task = Schedules.getNextToLastTask(vehicle.getSchedule());
        if(task.getStatus().equals(Task.TaskStatus.PLANNED) && task instanceof DrtStopTask){
            DrtStopTask drtStopTask = (DrtStopTask) task;
            for (DrtRequest drtRequest : drtStopTask.getDropoffRequests().values()) {
                if(drtRequest instanceof DrtReservationRequest){
                    return null; //vehicle will be removed from entry set of VehicleData
                }
            }
        }
        return delegate.create(vehicle, currentTime);
    }

    static class ReservingVehicleDataEntryFactoryProvider implements Provider<ReservingVehicleDataEntryFactory> {
        @Inject
        private Config config;

        @Override
        public ReservingVehicleDataEntryFactory get() {
            return new ReservingVehicleDataEntryFactory(DrtConfigGroup.getSingleModeDrtConfig(config));
        }
    }
}
