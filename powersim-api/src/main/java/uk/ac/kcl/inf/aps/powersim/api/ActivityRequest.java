package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Date: 04/07/13
 * Time: 16:41
 *
 * @author: josef
 */
public class ActivityRequest
{
  private final Household household;
  private final Appliance appliance;
  private final SimulationContext simulationContext;
  private final long wattage;
  private final long duration;


  public ActivityRequest(Household household, Appliance appliance, SimulationContext simulationContext, long wattage, long duration)
  {
    this.household = household;
    this.appliance = appliance;
    this.simulationContext = simulationContext;
    this.wattage = wattage;
    this.duration = duration;
  }

  public Household getHousehold()
  {
    return household;
  }

  public Appliance getAppliance()
  {
    return appliance;
  }

  public SimulationContext getSimulationContext()
  {
    return simulationContext;
  }

  public long getWattage()
  {
    return wattage;
  }

  public long getDuration()
  {
    return duration;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("ActivityRequest{");
    sb.append("household=").append(household);
    sb.append(", appliance=").append(appliance);
    sb.append(", simulationContext=").append(simulationContext);
    sb.append(", wattage=").append(wattage);
    sb.append(", duration=").append(duration);
    sb.append('}');
    return sb.toString();
  }
}
