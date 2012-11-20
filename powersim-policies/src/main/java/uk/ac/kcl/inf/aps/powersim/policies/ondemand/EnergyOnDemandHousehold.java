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
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), KETTLE.name(), KETTLE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), TOASTER.name(), TOASTER));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), WASHING_MACHINE.name(), WASHING_MACHINE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), DISHWASHER.name(), DISHWASHER));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), FRIDGE.name(), FRIDGE));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), AQUARIUM.name(), AQUARIUM));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), OVEN.name(), OVEN));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), TV.name(), TV));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LAPTOP.name(), LAPTOP));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));
    appliances.add(TwoStateAppliance.getInstance(UUID.randomUUID().toString(), LIGHT.name(), LIGHT));

    appliances.add(DayBoundedGLAppliance.getInstance(UUID.randomUUID().toString(), PV.name(), PV));
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
