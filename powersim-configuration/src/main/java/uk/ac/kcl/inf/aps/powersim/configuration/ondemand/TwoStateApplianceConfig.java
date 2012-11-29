package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.ApplianceConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.TwoStateAppliance;

import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:25
 */
public class TwoStateApplianceConfig extends ApplianceConfig<TwoStateAppliance>
{
  private long active;
  private long inactive;


  public long getActive()
  {
    return active;
  }

  public void setActive(long active)
  {
    this.active = active;
  }

  public long getInactive()
  {
    return inactive;
  }

  public void setInactive(long inactive)
  {
    this.inactive = inactive;
  }


  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("TwoStateApplianceConfig");
    sb.append("{type='").append(this.getType()).append('\'');
    sb.append(", subType='").append(this.getSubType()).append('\'');
    sb.append(", active=").append(active);
    sb.append(", inactive=").append(inactive);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public TwoStateAppliance getApplianceInstance()
  {
    return new TwoStateAppliance(UUID.randomUUID().toString(), this.getType(), this.getSubType(), active, inactive);
  }
}
