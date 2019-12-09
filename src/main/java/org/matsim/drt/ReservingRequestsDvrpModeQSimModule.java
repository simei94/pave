package org.matsim.drt;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.insertion.InsertionCostCalculator;
import org.matsim.contrib.drt.optimizer.insertion.PrecalculablePathDataProvider;
import org.matsim.contrib.drt.optimizer.insertion.UnplannedRequestInserter;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.scheduler.DrtScheduleInquiry;
import org.matsim.contrib.drt.scheduler.RequestInsertionScheduler;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.passenger.PassengerRequestCreator;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;

public class ReservingRequestsDvrpModeQSimModule extends AbstractDvrpModeQSimModule {
    DrtConfigGroup drtCfg;

    public ReservingRequestsDvrpModeQSimModule(DrtConfigGroup drtCfg) {
        super(drtCfg.getMode());
        this.drtCfg = drtCfg;
    }

    @Override
    protected void configureQSim() {
        //override the request inserter

        addModalComponent(ReservedVehiclePoolRequestInserter.class, modalProvider(
                getter -> new ReservedVehiclePoolRequestInserter(drtCfg, getter.getModal(Fleet.class),
                        getter.get(MobsimTimer.class), getter.get(EventsManager.class),
                        getter.getModal(RequestInsertionScheduler.class),
                        getter.getModal(VehicleData.EntryFactory.class),
                        getter.getModal(PrecalculablePathDataProvider.class),
                        getter.getModal(InsertionCostCalculator.PenaltyCalculator.class),
                        getter.getModal(DrtScheduleInquiry.class))));
        bindModal(UnplannedRequestInserter.class).to(modalKey(ReservedVehiclePoolRequestInserter.class));

        bind(ReservationDecision.class).toInstance(new DefaultReservationDecision());

        bindModal(PassengerRequestCreator.class).toProvider(new Provider<PassengerRequestCreator>() {
            @Inject
            private EventsManager events;
            @Inject
            private MobsimTimer timer;

            @Inject
            private ReservationDecision reservationDecision;

            @Override
            public PassengerRequestCreator get() {
                return new ReservingRequestCreator(getMode(), events, timer, reservationDecision);
            }
        }).asEagerSingleton();

    }

}
