package uk.ac.kcl.inf.aps.powersim.policies;

import uk.ac.kcl.inf.aps.powersim.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 19/10/12
 *         Time: 15:55
 */
public class SimpleHousehold extends Household
{
  List<SimpleAppliance> appliances = new ArrayList<>();

  public SimpleHousehold(String uid, String category, Policy policy)
  {
    super(uid, category, policy);
    setupAppliances();
  }

  private void setupAppliances()
  {
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LIGHT));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.FRIDGE));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.AQUARIUM));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.WASHING_MACHINE));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.DISHWASHER));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.OVEN));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.TV));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.TOASTER));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.KETTLE));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LAPTOP));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.LAPTOP));
    appliances.add(new SimpleAppliance(UUID.randomUUID().toString(), SimpleApplianceType.PV));
  }

  /**
   * Handles the current timeslot (communicated via the Simulation Context), turning appliances on and off
   * and returning the wattage consumed for this timeslot.
   * @param simulationContext - the simulation context
   * @return the wattage consumed (or generated if negative) during the specified timeslot
   */
  public long handleTimeslot(SimulationContext simulationContext)
  {
    long load = 0;
    List<ConsumptionEvent> consumptionEvents = new ArrayList<>(appliances.size());
    for (SimpleAppliance appliance : appliances)
    {
      long applianceLoad = appliance.handleTimeslot(simulationContext);
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
