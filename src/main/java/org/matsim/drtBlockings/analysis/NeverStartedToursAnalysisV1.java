package org.matsim.drtBlockings.analysis;

import com.google.common.base.Function;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.DrtBlockingRequest;
import org.matsim.drtBlockings.events.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeverStartedToursAnalysisV1 implements DrtBlockingRequestSubmittedEventHandler, DrtBlockingRequestScheduledEventHandler {

    private Map<Id<Request>, Double> requestToTime = new HashMap<>();
//    private Map<Id<DvrpVehicle>, DrtBlockingTourData> allTours = new HashMap<>();
//    private Map<Id<Request>, DrtBlockingTourData> submittedTours = new HashMap<>();
    private List<DrtBlockingTourData> allTours = new ArrayList<>();
    private List<Id<Request>> scheduledTours = new ArrayList<>();


    public static void main(String[] args) {
//        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/chessboard/Analysis_test/";
//        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-1pct/drtBlockingTest_30Blockings_realisticServiceTimeWindows/";
        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-1pct/drtBlockingTest_30Blockings_realisticServiceTimeWindows_0-24/";
//        String eventsFile = dir + "output_events.xml.gz";
        String eventsFile = dir + "noIncDRT.output_events.xml.gz";
//        String eventsFile = dir + "output_events_3.xml";
        String outputFile = dir + "neverStartedTourStatsV1.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        NeverStartedToursAnalysisV1 handler = new NeverStartedToursAnalysisV1();
        manager.addHandler(handler);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.writeStats(outputFile);
        System.out.println("Writing of DrtBlocking TourStats to " + outputFile + " was successful!");
    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i = 0;
            writer.write("No;requestId;requestTime");
            writer.newLine();

//            for (i=0; i <= this.neverScheduledTours.size(); i++) {
            for (DrtBlockingTourData data : this.allTours) {

                if(!this.scheduledTours.contains(data.requestId)) {

                    writer.write(i + ";" + data.requestId + ";" + data.time);
                    writer.newLine();
                }


                i++;
            }

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestSubmittedEvent event) {

        Id<Request> requestId = event.getRequestId();

        DrtBlockingTourData data = new DrtBlockingTourData(requestId, event.getTime());
        this.requestToTime.putIfAbsent(requestId, event.getTime());
//        this.allTours.put(vehId, data);
        this.allTours.add(data);

    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {

//        double submitTime = this.requestToTime.remove(event.getRequestId());
//        DrtBlockingTourData data = new DrtBlockingTourData(event.getRequestId(), submitTime);

        this.scheduledTours.add(event.getRequestId());
    }

    private class DrtBlockingTourData {
        private double time;
        private Id<Request> requestId;

        private DrtBlockingTourData(Id<Request> requestId, double time) {
            this.requestId = requestId;
            this.time = time;
        }

    }
}
