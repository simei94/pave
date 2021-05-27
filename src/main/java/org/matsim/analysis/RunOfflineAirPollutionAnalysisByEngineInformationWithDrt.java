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

package org.matsim.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.HbefaVehicleCategory;
import org.matsim.contrib.emissions.Pollutant;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup.DetailedVsAverageLookupBehavior;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup.HbefaRoadTypeSource;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup.NonScenarioVehicles;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Injector;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.EngineInformation;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import static org.matsim.contrib.emissions.Pollutant.*;

/**
 *
 * Computes the emissions using the 'allVehicles' output file which should be written out as a default in more recent MATSim runs
 *
 * @author ikaddoura
 */

public class RunOfflineAirPollutionAnalysisByEngineInformationWithDrt {
	private static final Logger log = Logger.getLogger(RunOfflineAirPollutionAnalysisByEngineInformationWithDrt.class);

	private final String runDirectory;
	private final String runId;
	private final String hbefaWarmFile;
	private final String hbefaColdFile;
	private final String analysisOutputDirectory;

	// provided by HBEFA for 2020
	private final double petrolShare = 0.512744724750519;
	private final double dieselShare = 0.462841421365738;
	private final double lpgShare = 0.011381645;
	private final double cngShare = 0.0038579236716032;
	private final double hybridPetrolShare = 0.005743607878685;
	private final double hybridDieselShare = 0.00014232617104426;

	private final static double shareOfPrivateVehiclesChangedToElectric = 0.0; // in addition to electric vehicle share in the reference case!

	static List<Pollutant> pollutants2Output = Arrays.asList(CO2_TOTAL, NOx, PM, PN, Pb, PM2_5);

	public RunOfflineAirPollutionAnalysisByEngineInformationWithDrt(String runDirectory, String runId, String hbefaFileWarm, String hbefaFileCold, String analysisOutputDirectory) {
		if (!runDirectory.endsWith("/")) runDirectory = runDirectory + "/";
		this.runDirectory = runDirectory;

		this.runId = runId;
		this.hbefaWarmFile = hbefaFileWarm;
		this.hbefaColdFile = hbefaFileCold;

		if (!analysisOutputDirectory.endsWith("/")) analysisOutputDirectory = analysisOutputDirectory + "/";
		this.analysisOutputDirectory = analysisOutputDirectory;
	}

	public static void main(String[] args) throws IOException {

//		if (args.length == 1) {
//			String rootDirectory = args[0];
//			if (!rootDirectory.endsWith("/")) rootDirectory = rootDirectory + "/";

		final String hbefaFileCold = "D:/svn/shared-svn/projects/matsim-germany/hbefa/hbefa-files/v4.1/EFA_ColdStart_Concept_2020_detailed_perTechAverage_Bln_carOnly.csv";
		final String hbefaFileWarm = "D:/svn/shared-svn/projects/matsim-germany/hbefa/hbefa-files/v4.1/EFA_HOT_Concept_2020_detailed_perTechAverage_Bln_carOnly.csv";

//			final String runRootDirectory = "D:/pave_runs/S3-smallFleets/";
//			final String runRootDirectory = "D:/pave_runs/S5-smallFleets/";

		final String runRootDirectory = "D:/svn/runs-svn/pave/output-and-analysis/S2-smallFleets";


		Set<String> runIds = new HashSet<>();


//		runIds.add("p-baseSingleTrip");

//		runIds.add("p5F-20");


//		runIds.add("p3F-3");
//		runIds.add("p3F-6");
//		runIds.add("p3F-9");
//
//		runIds.add("p3F-10");
//		runIds.add("p3F-11");
//		runIds.add("p3F-12");
//
//		runIds.add("p3F-13");
//		runIds.add("p3F-14");
//		runIds.add("p3F-15");


//		runIds.add("p1F-3-850veh");
//		runIds.add("p1F-4-3200veh");
//		runIds.add("p1F-5-2000veh");
//		runIds.add("p1F-6-1100veh");
//		runIds.add("p1F-10-750veh");
//		runIds.add("p1F-11-440veh");
//		runIds.add("p1F-12-240veh");



//				runIds.add("p2F-26-6200veh");
//		runIds.add("p2F-28-1450veh");
//		runIds.add("p2F-29-1100veh");
//		runIds.add("p2F-30-760veh");
//		runIds.add("p2F-31-880veh");
//		runIds.add("p2F-32-1500veh");
//		runIds.add("p2F-33-2000veh");





//			runIds.add("p1-4");
//			runIds.add("p1-8");
//			runIds.add("p1-9");



		Set<String> errors = new HashSet<>();

		runIds.forEach(runId -> {
			String runDirectory = runRootDirectory + "output-" + runId + "/";
			RunOfflineAirPollutionAnalysisByEngineInformationWithDrt analysis = new RunOfflineAirPollutionAnalysisByEngineInformationWithDrt(
					runDirectory,
					runId,
					hbefaFileWarm,
					hbefaFileCold,
					runDirectory + "emission-analysis-hbefa-v4.1");
			try {
				analysis.run();
			} catch (IOException e) {
				errors.add(runDirectory);
			}
		});

		errors.forEach(run -> System.out.println("could not process " + run));

//		} else {
//			throw new RuntimeException("Please set the root directory. Aborting...");
//		}
	}

	void run() throws IOException {

		Config config = ConfigUtils.createConfig();
		config.vehicles().setVehiclesFile(runDirectory + runId + ".output_allVehicles.xml.gz");
		config.network().setInputFile(runDirectory + runId + ".output_network.xml.gz");
		config.transit().setTransitScheduleFile(runDirectory + runId + ".output_transitSchedule.xml.gz");
		config.transit().setVehiclesFile(runDirectory + runId + ".output_transitVehicles.xml.gz");
		config.global().setCoordinateSystem("EPSG:31468");
		config.plans().setInputFile(null);
		config.parallelEventHandling().setNumberOfThreads(null);
		config.parallelEventHandling().setEstimatedNumberOfEvents(null);
		config.global().setNumberOfThreads(1);

		EmissionsConfigGroup eConfig = ConfigUtils.addOrGetModule(config, EmissionsConfigGroup.class);
		eConfig.setDetailedVsAverageLookupBehavior(DetailedVsAverageLookupBehavior.tryDetailedThenTechnologyAverageElseAbort);
		eConfig.setDetailedColdEmissionFactorsFile(hbefaColdFile);
		eConfig.setDetailedWarmEmissionFactorsFile(hbefaWarmFile);
		eConfig.setHbefaRoadTypeSource(HbefaRoadTypeSource.fromLinkAttributes);
		eConfig.setNonScenarioVehicles(NonScenarioVehicles.ignore);
		eConfig.setWritingEmissionsEvents(true);

		File folder = new File(analysisOutputDirectory);
		folder.mkdirs();

		final String eventsFile = runDirectory + runId + ".output_events.xml.gz";

		final String emissionEventOutputFile = analysisOutputDirectory + runId + ".emission.events.offline.xml.gz";
		final String linkEmissionAnalysisFile = analysisOutputDirectory + runId + ".emissionsPerLink.csv";
		final String linkEmissionPerMAnalysisFile = analysisOutputDirectory + runId + ".emissionsPerLinkPerM.csv";
		final String vehicleTypeFile = analysisOutputDirectory + runId + ".emissionVehicleInformation.csv";

		Scenario scenario = ScenarioUtils.loadScenario(config);

		// network
		for (Link link : scenario.getNetwork().getLinks().values()) {

			double freespeed = Double.NaN;

			if (link.getFreespeed() <= 13.888889) {
				freespeed = link.getFreespeed() * 2;
				// for non motorway roads, the free speed level was reduced
			} else {
				freespeed = link.getFreespeed();
				// for motorways, the original speed levels seems ok.
			}

			if(freespeed <= 8.333333333){ //30kmh
				link.getAttributes().putAttribute("hbefa_road_type", "URB/Access/30");
			} else if(freespeed <= 11.111111111){ //40kmh
				link.getAttributes().putAttribute("hbefa_road_type", "URB/Access/40");
			} else if(freespeed <= 13.888888889){ //50kmh
				double lanes = link.getNumberOfLanes();
				if(lanes <= 1.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/Local/50");
				} else if(lanes <= 2.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/Distr/50");
				} else if(lanes > 2.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/Trunk-City/50");
				} else{
					throw new RuntimeException("NoOfLanes not properly defined");
				}
			} else if(freespeed <= 16.666666667){ //60kmh
				double lanes = link.getNumberOfLanes();
				if(lanes <= 1.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/Local/60");
				} else if(lanes <= 2.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/Trunk-City/60");
				} else if(lanes > 2.0){
					link.getAttributes().putAttribute("hbefa_road_type", "URB/MW-City/60");
				} else{
					throw new RuntimeException("NoOfLanes not properly defined");
				}
			} else if(freespeed <= 19.444444444){ //70kmh
				link.getAttributes().putAttribute("hbefa_road_type", "URB/MW-City/70");
			} else if(freespeed <= 22.222222222){ //80kmh
				link.getAttributes().putAttribute("hbefa_road_type", "URB/MW-Nat./80");
			} else if(freespeed > 22.222222222){ //faster
				link.getAttributes().putAttribute("hbefa_road_type", "RUR/MW/>130");
			} else{
				throw new RuntimeException("Link not considered...");
			}
		}

		// car vehicles

		VehicleType petrolCarVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("petrolCar", VehicleType.class));
		scenario.getVehicles().addVehicleType(petrolCarVehicleType);
		EngineInformation petrolCarEngineInformation = petrolCarVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( petrolCarEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( petrolCarEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( petrolCarEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( petrolCarEngineInformation, "petrol (4S)" );

		VehicleType dieselCarVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("dieselCar", VehicleType.class));
		scenario.getVehicles().addVehicleType(dieselCarVehicleType);
		EngineInformation dieselCarEngineInformation = dieselCarVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( dieselCarEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( dieselCarEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( dieselCarEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( dieselCarEngineInformation, "diesel" );

		VehicleType cngVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("cngCar", VehicleType.class));
		scenario.getVehicles().addVehicleType(cngVehicleType);
		EngineInformation cngCarEngineInformation = cngVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( cngCarEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( cngCarEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( cngCarEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( cngCarEngineInformation, "bifuel CNG/petrol" );

		VehicleType lpgVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("lpgCar", VehicleType.class));
		scenario.getVehicles().addVehicleType(lpgVehicleType);
		EngineInformation lpgCarEngineInformation = lpgVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( lpgCarEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( lpgCarEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( lpgCarEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( lpgCarEngineInformation, "bifuel LPG/petrol" );

		// electric vehicles
		VehicleType electricVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("electricCar", VehicleType.class));
		scenario.getVehicles().addVehicleType(electricVehicleType);
		EngineInformation electricEngineInformation = electricVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( electricEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( electricEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( electricEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( electricEngineInformation, "electricity" );

		// plug-in hybrid petrol vehicles
		VehicleType pluginHybridPetrolVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("pluginHybridPetrol", VehicleType.class));
		scenario.getVehicles().addVehicleType(pluginHybridPetrolVehicleType);
		EngineInformation pluginHybridPetrolEngineInformation = pluginHybridPetrolVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( pluginHybridPetrolEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( pluginHybridPetrolEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( pluginHybridPetrolEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( pluginHybridPetrolEngineInformation, "Plug-in Hybrid petrol/electric" );

		// plug-in hybrid petrol vehicles
		VehicleType pluginHybridDieselVehicleType = scenario.getVehicles().getFactory().createVehicleType(Id.create("pluginHybridDiesel", VehicleType.class));
		scenario.getVehicles().addVehicleType(pluginHybridDieselVehicleType);
		EngineInformation pluginHybridDieselEngineInformation = pluginHybridDieselVehicleType.getEngineInformation();
		VehicleUtils.setHbefaVehicleCategory( pluginHybridDieselEngineInformation, HbefaVehicleCategory.PASSENGER_CAR.toString());
		VehicleUtils.setHbefaTechnology( pluginHybridDieselEngineInformation, "average" );
		VehicleUtils.setHbefaSizeClass( pluginHybridDieselEngineInformation, "average" );
		VehicleUtils.setHbefaEmissionsConcept( pluginHybridDieselEngineInformation, "Plug-in Hybrid diesel/electric" );


		VehicleType defaultCarVehicleType = scenario.getVehicles().getVehicleTypes().get(Id.create("car", VehicleType.class));

		VehicleType freightVehicleType = scenario.getVehicles().getVehicleTypes().get(Id.create("freight", VehicleType.class));
		VehicleUtils.setHbefaVehicleCategory( freightVehicleType.getEngineInformation(), HbefaVehicleCategory.NON_HBEFA_VEHICLE.toString());

		for (VehicleType type : scenario.getVehicles().getVehicleTypes().values()) {

			if (scenario.getTransitVehicles().getVehicleTypes().containsKey(type.getId())) {
				// consider transit vehicles as non-hbefa vehicles
				VehicleUtils.setHbefaVehicleCategory( type.getEngineInformation(), HbefaVehicleCategory.NON_HBEFA_VEHICLE.toString());
			}
		}

		List<Id<Vehicle>> vehiclesToChangeToElectric = new ArrayList<>();
		List<Id<Vehicle>> carVehiclesToChangeToSpecificType = new ArrayList<>();

		final Random rnd = MatsimRandom.getLocalInstance();

		// change some vehicle types, e.g. to investigate decarbonization scenarios, or to account for electric drt vehicles

		int totalVehiclesCounter = 0;
		for (Vehicle vehicle : scenario.getVehicles().getVehicles().values()) {
			totalVehiclesCounter++;

			if (scenario.getTransitVehicles().getVehicles().get(vehicle.getId()) != null) {
				// skip transit vehicles

			} else if (vehicle.getType().getId().toString().equals(freightVehicleType.getId().toString())) {
				// skip freight vehicles

			} else if (vehicle.getId().toString().contains("freight")) {
				// some freight vehicles have the type "car", skip them...
				log.info("Freight vehicle " + vehicle.getId().toString());

			} else if (vehicle.getType().getId().toString().equals(defaultCarVehicleType.getId().toString())) {

				carVehiclesToChangeToSpecificType.add(vehicle.getId());

				if (rnd.nextDouble() < shareOfPrivateVehiclesChangedToElectric) {
					vehiclesToChangeToElectric.add(vehicle.getId());
				}

			} else if (vehicle.getId().toString().contains("drt") || vehicle.getId().toString().contains("taxi")) {
				vehiclesToChangeToElectric.add(vehicle.getId());

			} else {
				throw new RuntimeException("Unknown vehicle type: " + vehicle.getType().getId().toString() + ". Aborting...");
			}
		}

		for (Id<Vehicle> id : carVehiclesToChangeToSpecificType) {
			scenario.getVehicles().removeVehicle(id);

			VehicleType vehicleType;
			double rndNumber = rnd.nextDouble();
			if (rndNumber < petrolShare) {
				// petrol
				vehicleType = petrolCarVehicleType;
			} else if (rndNumber >= petrolShare && rndNumber < petrolShare + dieselShare) {
				// diesel
				vehicleType = dieselCarVehicleType;
			} else if (rndNumber >= petrolShare + dieselShare && rndNumber < petrolShare + dieselShare + lpgShare) {
				// lpg
				vehicleType = lpgVehicleType;
			} else if (rndNumber >= petrolShare + dieselShare + lpgShare && rndNumber < petrolShare + dieselShare + lpgShare + cngShare) {
				// cng
				vehicleType = cngVehicleType;
			} else if (rndNumber >= petrolShare + dieselShare + lpgShare + cngShare && rndNumber < petrolShare + dieselShare + lpgShare + cngShare + hybridPetrolShare) {
				// hybrid petrol
				vehicleType = pluginHybridPetrolVehicleType;
			} else if (rndNumber >= petrolShare + dieselShare + lpgShare + cngShare + hybridPetrolShare && rndNumber < petrolShare + dieselShare + lpgShare + cngShare + hybridPetrolShare + hybridDieselShare) {
				// hybrid diesel
				vehicleType = pluginHybridDieselVehicleType;
			} else {
				// electric
				vehicleType = electricVehicleType;
			}

			Vehicle vehicleNew = scenario.getVehicles().getFactory().createVehicle(id, vehicleType);
			scenario.getVehicles().addVehicle(vehicleNew);
			log.info("Type for vehicle " + id + " changed to: " + vehicleType.getId().toString());
		}

		for (Id<Vehicle> id : vehiclesToChangeToElectric) {
			scenario.getVehicles().removeVehicle(id);
			Vehicle vehicleNew = scenario.getVehicles().getFactory().createVehicle(id, electricVehicleType);
			scenario.getVehicles().addVehicle(vehicleNew);
			log.info("Type for vehicle " + id + " changed to electric.");
		}

		// the following is copy paste from the example...

		EventsManager eventsManager = EventsUtils.createEventsManager();

		AbstractModule module = new AbstractModule(){
			@Override
			public void install(){
				bind( Scenario.class ).toInstance( scenario );
				bind( EventsManager.class ).toInstance( eventsManager );
				bind( EmissionModule.class ) ;
			}
		};

		com.google.inject.Injector injector = Injector.createInjector(config, module);

		EmissionModule emissionModule = injector.getInstance(EmissionModule.class);

//        EventWriterXML emissionEventWriter = new EventWriterXML(emissionEventOutputFile);
//        emissionModule.getEmissionEventsManager().addHandler(emissionEventWriter);

		EmissionsOnLinkHandler emissionsEventHandler = new EmissionsOnLinkHandler();
		eventsManager.addHandler(emissionsEventHandler);

		eventsManager.initProcessing();

		MatsimEventsReader matsimEventsReader = new MatsimEventsReader(eventsManager);
		matsimEventsReader.readFile(eventsFile);

		log.info("Done reading the events file.");
		log.info("Finish processing...");
		eventsManager.finishProcessing();

//        log.info("Closing events file...");
//        emissionEventWriter.closeFile();

		log.info("Total number of vehicles: " + totalVehiclesCounter);
		log.info("Number of passenger car vehicles: " + carVehiclesToChangeToSpecificType.size());
		log.info("Number of passenger car vehicles that are changed to electric vehicles: " + vehiclesToChangeToElectric.size());

		log.info("Emission analysis completed.");

		log.info("Writing output...");

		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		nf.setMaximumFractionDigits(4);
		nf.setGroupingUsed(false);

		{
			File file1 = new File(linkEmissionAnalysisFile);

			BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));

			bw1.write("linkId");

			for (Pollutant pollutant : pollutants2Output) {
				bw1.write(";" + pollutant);
			}
			bw1.newLine();

			Map<Id<Link>, Map<Pollutant, Double>> link2pollutants = emissionsEventHandler.getLink2pollutants();

			for (Id<Link> linkId : link2pollutants.keySet()) {
				bw1.write(linkId.toString());

				for (Pollutant pollutant : pollutants2Output) {
					double value = 0.;
					if (link2pollutants.get(linkId).get(pollutant) != null) {
						value = link2pollutants.get(linkId).get(pollutant);
					}
					bw1.write(";" + nf.format(value));
				}
				bw1.newLine();
			}

			bw1.close();
			log.info("Output written to " + linkEmissionAnalysisFile);
		}

		{
			File file1 = new File(linkEmissionPerMAnalysisFile);

			BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));

			bw1.write("linkId");

			for (Pollutant pollutant : pollutants2Output) {
				bw1.write(";" + pollutant + " [g/m]");
			}
			bw1.newLine();

			Map<Id<Link>, Map<Pollutant, Double>> link2pollutants = emissionsEventHandler.getLink2pollutants();

			for (Id<Link> linkId : link2pollutants.keySet()) {
				bw1.write(linkId.toString());

				for (Pollutant pollutant : pollutants2Output) {
					double emission = 0.;
					if (link2pollutants.get(linkId).get(pollutant) != null) {
						emission = link2pollutants.get(linkId).get(pollutant);
					}

					double emissionPerM = Double.NaN;
					Link link = scenario.getNetwork().getLinks().get(linkId);
					if (link != null) {
						emissionPerM = emission / link.getLength();
					}
					bw1.write(";" + nf.format(emissionPerM));
				}
				bw1.newLine();
			}

			bw1.close();
			log.info("Output written to " + linkEmissionPerMAnalysisFile);
		}

		{
			File file2 = new File(vehicleTypeFile);

			BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2));

			bw2.write("vehicleId;vehicleType;emissionsConcept");
			bw2.newLine();

			for (Vehicle vehicle : scenario.getVehicles().getVehicles().values()) {
				String emissionsConcept = "null";
				if (vehicle.getType().getEngineInformation() != null && VehicleUtils.getHbefaEmissionsConcept(vehicle.getType().getEngineInformation()) != null) {
					emissionsConcept = VehicleUtils.getHbefaEmissionsConcept(vehicle.getType().getEngineInformation()).toString();
				}

				bw2.write(vehicle.getId() + ";" + vehicle.getType().getId().toString() + ";" + emissionsConcept);
				bw2.newLine();
			}

			bw2.close();
			log.info("Output written to " + vehicleTypeFile);
		}

	}

}

