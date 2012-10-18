package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 15:16
 */
public class ConsumptionEvent
{
  /**
   * The household in which the appliance is.
   */
  private Household household;

  /**
   * The appliance generating the load.
   */
  private Appliance appliance;

  /**
   * The load in Watts that the appliance in the specified household is consuming or generating.
   * If the value is positive it is consuming the load, if the value is negative it is generating the load.
   */
  private long loadWatts;

  public ConsumptionEvent(Household household, Appliance appliance, long loadWatts)
  {
    this.household = household;
    this.appliance = appliance;
    this.loadWatts = loadWatts;
  }

  public Household getHousehold()
  {
    return household;
  }

  public Appliance getAppliance()
  {
    return appliance;
  }

  public long getLoadWatts()
  {
    return loadWatts;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ConsumptionEvent");
    sb.append("{household=").append(household);
    sb.append(", appliance=").append(appliance);
    sb.append(", loadWatts=").append(loadWatts);
    sb.append('}');
    return sb.toString();
  }
}
