package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Represents a household aggregation point where there is a Smart Meter controlling the load of a number of appliances.
 * Should be extended by the individual Policy implementation, depending on what information is needed.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 15:17
 */
public abstract class Household implements SimulationTimeslotConsumer, ActivityManager
{
  /**
   * A unique identifier of the household. Has to be unique across the simulation.
   * Should be a UUID / GUID.
   * In reality it could be the Smart Meter ID or Account ID for that household.
   */
  private String uid;

  /**
   * The category of the household, such as 'Executive', 'Office' etc.
   */
  private String category;

  /**
   * The policy implementation handling this household.
   */
  private Policy policy;

  public Household(String uid, String category)
  {
    this.uid = uid;
    this.category = category;
  }

  public String getUid()
  {
    return uid;
  }

  public String getCategory()
  {
    return category;
  }

  public Policy getPolicy()
  {
    return policy;
  }

  public void setPolicy(Policy policy)
  {
    this.policy = policy;
  }

  public abstract void setup();

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("Household");
    sb.append("{uid='").append(uid).append('\'');
    sb.append(", category='").append(category).append('\'');
    sb.append(", policy=").append(policy.getDescriptor());
    sb.append('}');
    return sb.toString();
  }
}
