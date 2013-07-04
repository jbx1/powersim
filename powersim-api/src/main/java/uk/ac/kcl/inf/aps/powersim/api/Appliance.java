package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Represents an Appliance.
 * Should be extended by the respective Policy implementation.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 15:17
 */
public abstract class Appliance implements SimulationTimeslotConsumer
{
  /**
   * A unique identifier for this appliance. Ideally a UUID / GUID.
   * In reality could also be a serial number of the device, or MAC address if it is network reachable.
   */
  private String uid;

  /**
   * A string that identifies the type of the appliance. (E.g. Laptop, Fridge, TV, PV etc.)
   */
  private String type;

  /**
   * The household this appliance is residing in.
   */
  private Household household;

  public Appliance(String uid, String type)
  {
    this.uid = uid;
    this.type = type;
  }

  public String getUid()
  {
    return uid;
  }

  public String getType()
  {
    return type;
  }

  public void setHousehold(Household household)
  {
    this.household = household;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("{uid='").append(uid).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
