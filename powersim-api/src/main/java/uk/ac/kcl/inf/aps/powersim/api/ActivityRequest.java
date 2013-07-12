package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Date: 04/07/13
 * Time: 16:41
 *
 * @author: Josef Bajada &lt; josef.bajada@kcl.ac.uk &gt;
 */
public class ActivityRequest implements Comparable<ActivityRequest>
{
  private final Household household;
  private final Appliance appliance;
  private final SimulationContext simulationContext;
  private final long wattage;
  private final long duration;

  //todo: add deadline

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
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    ActivityRequest that = (ActivityRequest) o;

    if (duration != that.duration)
    {
      return false;
    }
    if (wattage != that.wattage)
    {
      return false;
    }
    if (!appliance.equals(that.appliance))
    {
      return false;
    }
    if (!household.equals(that.household))
    {
      return false;
    }
    if (!simulationContext.equals(that.simulationContext))
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = household.hashCode();
    result = 31 * result + appliance.hashCode();
    result = 31 * result + simulationContext.hashCode();
    result = 31 * result + (int) (wattage ^ (wattage >>> 32));
    result = 31 * result + (int) (duration ^ (duration >>> 32));
    return result;
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

  @Override
  public int compareTo(ActivityRequest o)
  {
    long time = this.getSimulationContext().compareTo(o.getSimulationContext());

    if (time != 0)
      return (int) time;

    return this.getAppliance().getUid().compareTo(o.getAppliance().getUid());
  }
}
