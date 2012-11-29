package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 16:48
 */
public class EnergyOnDemandHousehold extends Household implements SimulationTimeslotConsumer
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandHousehold.class);

  Map<ApplianceKeyTuple, ApplianceFactoryConfiguration<EnergyOnDemandAppliance>> applianceFactoryMap = new TreeMap<>();

  List<EnergyOnDemandAppliance> appliances = new ArrayList<>();

  public EnergyOnDemandHousehold(String uid, String category)
  {
    super(uid, category);
  }

  public void addApplianceFactoryConfiguration(ApplianceKeyTuple applianceKeyTuple, ApplianceFactory<EnergyOnDemandAppliance> applianceFactory, ApplianceProfiler<EnergyOnDemandAppliance> applianceProfiler, int applianceCount)
  {
    applianceFactoryMap.put(applianceKeyTuple, new ApplianceFactoryConfiguration<>(applianceFactory, applianceProfiler, applianceCount));
  }

  public void setupAppliances()
  {
    log.info("Setting up {} appliance types of {}", applianceFactoryMap.size(), this.getUid());

    for (ApplianceKeyTuple applianceKeyTuple : applianceFactoryMap.keySet())
    {
      log.trace("Configuring {}", applianceKeyTuple);
      ApplianceFactoryConfiguration<EnergyOnDemandAppliance> applianceFactoryConfiguration = applianceFactoryMap.get(applianceKeyTuple);

      log.trace("{} requires {} instances", applianceKeyTuple, applianceFactoryConfiguration.getApplianceCount());
      for (int i = 0; i < applianceFactoryConfiguration.getApplianceCount(); i++)
      {
        EnergyOnDemandAppliance energyOnDemandAppliance = applianceFactoryConfiguration.getApplianceFactory().getApplianceInstance();
        log.trace("Created new appliance {} for household {}", energyOnDemandAppliance, this.getUid());

        applianceFactoryConfiguration.getApplianceProfiler().profileAppliance(energyOnDemandAppliance);
        log.trace("Appliance {} profile set.", energyOnDemandAppliance);
        appliances.add(energyOnDemandAppliance);
      }
    }
  }


  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
    log.trace("Household {} consuming timeslot {}", this.getUid(), simulationContext.getTimeslot());
    long load = 0;
    List<ConsumptionEvent> consumptionEvents = new ArrayList<>(appliances.size());
    log.trace("Household has {} appliances", appliances.size());
    for (EnergyOnDemandAppliance appliance : appliances)
    {
      log.trace("Notifying appliance {}", appliance.getUid());
      long applianceLoad = appliance.consumeTimeSlot(simulationContext);
      if (applianceLoad != 0)
      {
        consumptionEvents.add(new ConsumptionEvent(this, appliance, applianceLoad));
        load += applianceLoad;
      }
    }

    simulationContext.getSimulation().notifyConsumptionEvents(consumptionEvents);
    return load;
  }
}
