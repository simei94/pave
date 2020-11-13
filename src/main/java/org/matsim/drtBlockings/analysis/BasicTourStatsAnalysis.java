package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEventHandler;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEventHandler;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.events.*;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicTourStatsAnalysis implements DrtBlockingRequestScheduledEventHandler, TaskStartedEventHandler,
TaskEndedEventHandler, DrtBlockingEndedEventHandler, LinkEnterEventHandler {

    private Network network;
    private Map<Id<DvrpVehicle>, DrtBlockingTourData> currentTours = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDistance = new HashMap<>();
//    private Map<Id<DvrpVehicle>, Double> vehToAccess = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDeparture = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToArrival = new HashMap<>();
//    private Map<Id<DvrpVehicle>, Double> vehToDuration = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDistance = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDuration = new HashMap<>();
    private Map<Id<DvrpVehicle>, Id<Request>> vehToRequest = new HashMap<>();
    private Map<Id<DvrpVehicle>, Integer> vehToTaskNo = new HashMap<>();

    private List<DrtBlockingTourData> finishedTours = new ArrayList<>();

    public BasicTourStatsAnalysis(Network network) {
        this.network = network;
    }

    public static void main(String[] args) {
//        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/chessboard/Analysis_test/";
        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/drtBlockingTest_30Blockings_ServiceWidthIncr/";
//        String eventsFile = dir + "output_events.xml.gz";
        String eventsFile = dir + "blckBase1.output_events.xml.gz";
//        String carriersFile = dir + "";
//        String inputNetwork = dir + "output_network.xml.gz";
        String inputNetwork = dir + "blckBase1.output_network.xml.gz";
        String outputFile = dir + "testBasicTourStats.csv";
//        final Carriers carriers = new Carriers();
//        new CarrierPlanXmlReader(carriers).readFile(carriersFile);

        EventsManager manager = EventsUtils.createEventsManager();
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(inputNetwork);

            BasicTourStatsAnalysis handler = new BasicTourStatsAnalysis(network);
        manager.addHandler(handler);
//        manager.addHandler((LinkEnterEventHandler) linkEnterEvent -> System.out.println("HEY"));
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.writeStats(outputFile);
        System.out.println("Writing of DrtBlocking TourStats to " + outputFile + " was successful!");
    }

    public void notifyIterationEnd(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory();
        String outputFile = dir + "/testBasicTourStats.csv";
//        writeStats(outputFile);
    }

    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i =1;
            writer.write("no;vehId;totalDistance;accessLegDistance;departureTime;arrivalTime;tourDuration;accessLegDuration;requestId;numberOfTasks");
            writer.newLine();

            for (DrtBlockingTourData data  : this.finishedTours) {
                writer.write(i + ";" + data.veh + ";" + data.tourDistance + ";" + data.accessDistance + ";"
                + data.departure + ";" + data.arrival + ";" + data.tourDuration + ";" + data.accessDuration + ";" + data.requestId + ";" + data.taskNo);
                writer.newLine();
                i++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleEvent(LinkEnterEvent event) {
        //check if veh has a running tour

        //could be that the vehicle will not be recognized because its VehicleId and not vId
        //maybe its necessary to add .toString after getVehicleId() but should work like this too
        //maybe like that
        Id<DvrpVehicle> dvrpVehicleId = Id.create(event.getVehicleId(), DvrpVehicle.class);
        if (this.currentTours.containsKey(dvrpVehicleId)) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.vehToDistance.computeIfAbsent(dvrpVehicleId, v -> 0.);
            this.vehToDeparture.putIfAbsent(dvrpVehicleId, event.getTime());
            this.vehToDistance.replace(dvrpVehicleId,
                        distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
//            System.out.println(event.getLinkId());
        }
    }

    @Override
    public void handleEvent(TaskEndedEvent event) {
        //not sure if this eventType is even needed
        //so far we just need DrtBlockingEndedEvent to register end of tour!
    }

    @Override
    public void handleEvent(TaskStartedEvent event) {
        //here we register the access leg + count vehicle's tasks
        //check if veh has a running tour
        Id<DvrpVehicle> dvrpVehicleId = event.getDvrpVehicleId();
        if (this.currentTours.containsKey(dvrpVehicleId)) {

            if (!this.vehToTaskNo.containsKey(dvrpVehicleId)) {
                this.vehToTaskNo.put(dvrpVehicleId, 0);
            } else {
                this.vehToTaskNo.replace(dvrpVehicleId, this.vehToTaskNo.get(dvrpVehicleId),
                        this.vehToTaskNo.get(dvrpVehicleId) + 1);
            }

            if (event.getTaskType().equals(FreightRetoolTask.RETOOL_TASK_TYPE)) {
                this.vehToAccessDistance.putIfAbsent(dvrpVehicleId, this.vehToDistance.get(dvrpVehicleId));
                Double accessDuration = event.getTime() - this.vehToDeparture.get(dvrpVehicleId);
                if (accessDuration >= 0) {
                    this.vehToAccessDuration.putIfAbsent(dvrpVehicleId, accessDuration);
                } else {
                    System.out.println("Access leg duration for vehicle " + dvrpVehicleId + " is " + accessDuration + " (< 0!");
                }
            }
        }
    }

    @Override
    public void handleEvent(DrtBlockingEndedEvent event) {
        if (this.currentTours.containsKey(event.getVehicleId())) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.vehToDistance.remove(event.getVehicleId());
            DrtBlockingTourData data = this.currentTours.remove(event.getVehicleId());

//            this.vehToDistance.replace(event.getVehicleId(),
//                    distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());

//            System.out.println(data.tourDistance);

            //get eventTime and calculate tourDuration
            //The following should be the case for every tour!
            if (event.getTime() > this.vehToDeparture.get(event.getVehicleId())) {
                this.vehToArrival.put(event.getVehicleId(), event.getTime());
                Double tourDuration = event.getTime() - vehToDeparture.get(event.getVehicleId());
                data.tourDuration = tourDuration;
                data.tourDistance = distanceSoFar + network.getLinks().get(event.getLinkId()).getLength();
                data.accessDistance = this.vehToAccessDistance.remove(event.getVehicleId());
                data.accessDuration = this.vehToAccessDuration.remove(event.getVehicleId());
                data.departure = this.vehToDeparture.remove(event.getVehicleId());
                data.arrival = this.vehToArrival.remove(event.getVehicleId());
                data.requestId = this.vehToRequest.remove(event.getVehicleId());
                data.taskNo = this.vehToTaskNo.remove(event.getVehicleId());

//                System.out.println(event.getLinkId() + " END OF TOUR!");

//                this.vehToDuration.put(event.getVehicleId(), tourDuration);
            } else {
                System.out.println("The tours of vehicle " + event.getVehicleId() + " are not correctly handled!");
            }
            //remove  veh from currentTours and out it onto finishedTours
//            this.finishedTours.add(this.currentTours.remove(event.getVehicleId()));
            this.finishedTours.add(data);
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
        //put veh into map of current tours when Request is requested
//        currentTours.put(event.getVehicleId(), event.getVehicleId());
        DrtBlockingTourData data = new DrtBlockingTourData(event.getVehicleId(), 0.);
        this.currentTours.put(event.getVehicleId(), data);
        this.vehToRequest.put(event.getVehicleId(), event.getRequestId());
    }

    private class DrtBlockingTourData {
        //TODO: switch from data in maps to data in here!
        private final Id<DvrpVehicle> veh;
        private double departure;
        private double arrival;
        private double tourDuration;
        private double accessDuration;
        private double tourDistance = 0.;
        private double accessDistance = 0.;
        private Id<Request> requestId;
        private int taskNo;

        private DrtBlockingTourData(Id<DvrpVehicle> veh, double departure) {
            this.veh = veh;
            this.departure = departure;
        }
    }
}
