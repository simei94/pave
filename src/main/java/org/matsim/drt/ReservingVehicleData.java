package org.matsim.drt;

import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.drt.schedule.DrtStopTask;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.Task;

import javax.inject.Inject;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

final class ReservingVehicleData extends VehicleData {

    @Inject
    private ReservationDecision reservationDecision;

    ReservingVehicleData(double currentTime, Stream<? extends DvrpVehicle> vehicles, EntryFactory entryFactory, ForkJoinPool forkJoinPool) {
        super(currentTime, vehicles, entryFactory, forkJoinPool);
    }

    @Override
    public void updateEntry(DvrpVehicle vehicle) {

        //if one of the requests at the last stop is a reservation dropOff, exclude this vehicle
        Task task = Schedules.getNextToLastTask(vehicle.getSchedule());
        if(task.getStatus().equals(Task.TaskStatus.PLANNED) && task instanceof DrtStopTask){
            DrtStopTask drtStopTask = (DrtStopTask) task;
            for (DrtRequest drtRequest : drtStopTask.getDropoffRequests().values()) {
                if(drtRequest instanceof DrtReservationRequest){
                    entries.remove(vehicle);
                    return;
                }
            }
        }
        super.updateEntry(vehicle);
    }
}
