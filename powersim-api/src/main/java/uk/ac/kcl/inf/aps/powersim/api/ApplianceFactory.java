package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 15:25
 */
public interface ApplianceFactory<A extends Appliance>
{
  public A getApplianceInstance();
}
