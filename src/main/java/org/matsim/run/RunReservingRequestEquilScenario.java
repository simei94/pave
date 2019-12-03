package org.matsim.run;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.contrib.drt.routing.DrtRouteFactory;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtModule;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RunReservingRequestEquilScenario {


    public static void main(String[] args) {
        URL configUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("mielec"), "mielec_drt_config.xml");
        Config config = ConfigUtils.loadConfig(configUrl);

        int threads = 8;

        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);
        config.global().setNumberOfThreads(threads);

        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);

        DrtConfigGroup drtCfg = new DrtConfigGroup();
        drtCfg.setMaxWaitTime(2 * 3600);
        drtCfg.setMaxTravelTimeAlpha(5);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
        drtCfg.setVehiclesFile("vehicles-10cd ..-cap-4.xml");
        drtCfg.setNumberOfThreads(threads);
        multiModeDrtConfigGroup.addParameterSet(drtCfg);
        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        Scenario scenario = DrtControlerCreator.createScenarioWithDrtRouteFactory(config);
        ScenarioUtils.loadScenario(scenario);

//        createRandomPopulation(scenario);

        Controler controler = new Controler(scenario);

        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new MultiModeDrtModule( ) ) ;

        controler.configureQSimComponents( DvrpQSimComponents.activateModes( TransportMode.drt ) ) ;

        controler.run();
    }


    private static  void createRandomPopulation(Scenario scenario){
        PopulationFactory popFactory = scenario.getPopulation().getFactory();

        for (int i = 0; i < 100; i++) {
            Person person = popFactory.createPerson(Id.createPersonId(i));

            List<Id<Link>> linkIds = scenario.getNetwork().getLinks().keySet().stream().collect(Collectors.toList());

            Random rnd =  MatsimRandom.getLocalInstance();
            Plan plan = popFactory.createPlan();
            Activity home = popFactory.createActivityFromLinkId("home", linkIds.get(rnd.nextInt(linkIds.size())));
            plan.addActivity(home);

            plan.addLeg(popFactory.createLeg(TransportMode.drt));
            plan.addActivity(popFactory.createActivityFromLinkId("work", linkIds.get(rnd.nextInt(linkIds.size()))));

            plan.addLeg(popFactory.createLeg(TransportMode.drt));
            plan.addActivity(home);

            person.addPlan(plan);
            scenario.getPopulation().addPerson(person);
        }
    }



}
