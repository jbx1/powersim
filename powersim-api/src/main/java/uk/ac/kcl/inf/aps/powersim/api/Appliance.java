package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Represents an Appliance.
 * Should be extended by the respective Policy implementation.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 15:17
 */
public abstract class Appliance
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
   * A string that identifies the sub-type of the appliance. (E.g. CRT, LED, Incandescent etc.)
   */
  private String subtype;

  public Appliance(String uid, String type, String subtype)
  {
    this.uid = uid;
    this.type = type;
    this.subtype = subtype;
  }

  public String getUid()
  {
    return uid;
  }

  public String getType()
  {
    return type;
  }

  public String getSubtype()
  {
    return subtype;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("{uid='").append(uid).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", subtype='").append(subtype).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
