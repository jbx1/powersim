package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Interface to be implemented by any object responsible for setting the load profile
 * of an appliance.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 17:00
 */
public interface ApplianceProfiler<A extends Appliance>
{
  public String getName();

  public void profileAppliance(A appliance);
}
