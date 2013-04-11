package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.ApplianceConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.DayBoundedGLAppliance;

import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:30
 */
public class VariableLoadApplianceConfig extends ApplianceConfig<DayBoundedGLAppliance>
{
  private long peak;

  public long getPeak()
  {
    return peak;
  }

  public void setPeak(long peak)
  {
    this.peak = peak;
  }

  @Override
  public DayBoundedGLAppliance getApplianceInstance()
  {
    return new DayBoundedGLAppliance(UUID.randomUUID().toString(), getType(), peak);
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("VariableLoadApplianceConfig");
    sb.append("{type='").append(this.getType()).append('\'');
    sb.append(", peak=").append(peak);
    sb.append('}');
    return sb.toString();
  }
}
