package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import uk.ac.kcl.inf.aps.powersim.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uk.ac.kcl.inf.aps.powersim.policies.ondemand.TwoStateApplianceUsageRating.*;
import static uk.ac.kcl.inf.aps.powersim.policies.ondemand.DayBoundedGLApplianceRating.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 16:48
 */
public class EnergyOnDemandHousehold extends Household implements SimulationTimeslotConsumer
{
  List<EnergyOnDemandAppliance> appliances = new ArrayList<>();

  public EnergyOnDemandHousehold(String uid, String category, Policy policy)
  {
    super(uid, category, policy);
    setupAppliances();
  }


  private void setupAppliances()
  {
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), KETTLE.name(), "default", KETTLE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), TOASTER.name(), "default", TOASTER));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), WASHING_MACHINE.name(),"default", WASHING_MACHINE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), DISHWASHER.name(), "default", DISHWASHER));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), FRIDGE.name(), "default", FRIDGE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), AQUARIUM.name(),"default", AQUARIUM));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), OVEN.name(), "default", OVEN));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), TV.name(), "default", TV));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LAPTOP.name(), "default", LAPTOP));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(),"default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), "default", LIGHT));

    appliances.add(DayBoundedGLAppliance.getInstance(UUID.randomUUID().toString(), PV.name(), "default", PV));
  }


  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
    long load = 0;
    List<ConsumptionEvent> consumptionEvents = new ArrayList<>(appliances.size());
    for (EnergyOnDemandAppliance appliance : appliances)
    {
      long applianceLoad = appliance.consumeTimeSlot(simulationContext);
      if (applianceLoad != 0)
      {
        consumptionEvents.add(new ConsumptionEvent(this, appliance, applianceLoad));
        load += applianceLoad;
      }

      //todo: do we need aggregate load per household explicitly?
    }

    simulationContext.getSimulation().notifyConsumptionEvents(consumptionEvents);
    return load;
  }
}
