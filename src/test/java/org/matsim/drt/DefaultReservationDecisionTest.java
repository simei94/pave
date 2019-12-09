package org.matsim.drt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class DefaultReservationDecisionTest {

    private Person personWithoutReservation;
    private Person personWithReserveration;

    private int workEndTime = 15*3600;
    private int lastLegTT = 10*60;

    @Before
    public void setUp() throws Exception {
        Population population = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();
        PopulationFactory popFactory = population.getFactory();


        personWithoutReservation = popFactory.createPerson(Id.createPersonId("normalPersonWithoutReservation"));
        Plan planWithoutReservation = createPlan(popFactory);
        personWithoutReservation.addPlan(planWithoutReservation);
        personWithoutReservation.setSelectedPlan(planWithoutReservation);

        personWithReserveration = popFactory.createPerson(Id.createPersonId("freightPersonWithReservation"));
        Plan planWithReservation = createPlan(popFactory);
        personWithReserveration.addPlan(planWithReservation);
        personWithReserveration.setSelectedPlan(planWithReservation);

    }

    private Plan createPlan(PopulationFactory popFactory) {
        Plan plan = popFactory.createPlan();
        Activity home = popFactory.createActivityFromLinkId("home", Id.createLinkId("1"));
        home.setEndTime(10*3600 * 1800);
        plan.addActivity(home);

        Leg drtLeg = popFactory.createLeg(TransportMode.drt);
        TripStructureUtils.setRoutingMode(drtLeg, TransportMode.drt);
        plan.addLeg(drtLeg);

        Activity work = popFactory.createActivityFromLinkId("work", Id.createLinkId("2"));
        work.setEndTime(workEndTime);
        plan.addActivity(work);

        drtLeg = popFactory.createLeg(TransportMode.drt);
        drtLeg.setDepartureTime(workEndTime);
        TripStructureUtils.setRoutingMode(drtLeg, TransportMode.drt);

        Route drtRoute = new DrtRoute(Id.createLinkId("1"),Id.createLinkId("2"));
        drtRoute.setTravelTime(lastLegTT);
        drtLeg.setRoute(drtRoute);

        plan.addLeg(drtLeg);

        Activity home2 = popFactory.createActivityFromLinkId("home", home.getLinkId());
        plan.addActivity(home2);
        return plan;
    }

    @Test
    public void isReservationRequested() {
        DefaultReservationDecision decision = new DefaultReservationDecision();
        Assert.assertFalse(decision.isReservationRequested(personWithoutReservation.getSelectedPlan()));
        Assert.assertTrue(decision.isReservationRequested(personWithReserveration.getSelectedPlan()));
    }

    @Test
    public void determineReservationValidityEnd() {
        DefaultReservationDecision decision = new DefaultReservationDecision();
        try{
            decision.determineReservationValidityEnd(personWithoutReservation.getSelectedPlan());
            Assert.fail("for a plan that does not request a reservation, there should be no reservation validity end time..");
        } catch (IllegalStateException e){
        }
        Assert.assertEquals("reservation validity end time should be departure time + travel time" , workEndTime + lastLegTT , decision.determineReservationValidityEnd(personWithReserveration.getSelectedPlan()), 0.001);
    }

}