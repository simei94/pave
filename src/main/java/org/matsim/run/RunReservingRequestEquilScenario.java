package org.matsim.run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.examples.ExamplesUtils;

public class RunReservingRequestEquilScenario {


    public static void main(String[] args) {
        Config config = ConfigUtils.createConfig();
        config.setContext(ExamplesUtils.getTestScenarioURL("freight-chessboard-9x9"));
        config.network().setInputFile("grid9x9.xml");

        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);
        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);

        DrtConfigGroup drtCfg = new DrtConfigGroup();
        drtCfg.setMaxWaitTime(2 * 3600);
        drtCfg.setMaxTravelTimeAlpha(5);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
//        drtCfg.setVehiclesFile(inputDir + "drtVehicles.xml");
        multiModeDrtConfigGroup.addParameterSet(drtCfg);
        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        Scenario scenario = DrtControlerCreator.createScenarioWithDrtRouteFactory(config);

        Controler controler = new Controler(scenario);

        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                bind(Passen)
            }
        });
    }



}
