package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.ApplianceFactory;
import uk.ac.kcl.inf.aps.powersim.api.ApplianceProfiler;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 29/11/12
 *         Time: 12:04
 */
public class ApplianceFactoryConfiguration<T extends Appliance>
{
  private final ApplianceFactory<T> applianceFactory;
  private final ApplianceProfiler<T> applianceProfiler;
  private final int applianceCount;

  public ApplianceFactoryConfiguration(ApplianceFactory<T> applianceFactory, ApplianceProfiler<T> applianceProfiler, int applianceCount)
  {
    this.applianceFactory = applianceFactory;
    this.applianceProfiler = applianceProfiler;
    this.applianceCount = applianceCount;
  }

  public ApplianceFactory<T> getApplianceFactory()
  {
    return applianceFactory;
  }

  public int getApplianceCount()
  {
    return applianceCount;
  }

  public ApplianceProfiler<T> getApplianceProfiler()
  {
    return applianceProfiler;
  }
}