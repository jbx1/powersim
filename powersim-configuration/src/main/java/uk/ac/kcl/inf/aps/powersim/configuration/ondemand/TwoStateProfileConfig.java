package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.ProfileConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.TwoStateAppliance;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:48
 */
public class TwoStateProfileConfig extends ProfileConfig<TwoStateAppliance>
{
  private StochasticProcessConfig activation;
  private StochasticProcessConfig deactivation;

  public StochasticProcessConfig getActivation()
  {
    return activation;
  }

  public void setActivation(StochasticProcessConfig activation)
  {
    this.activation = activation;
  }

  public StochasticProcessConfig getDeactivation()
  {
    return deactivation;
  }

  public void setDeactivation(StochasticProcessConfig deactivation)
  {
    this.deactivation = deactivation;
  }

  @Override
  public void profileAppliance(TwoStateAppliance appliance)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("TwoStateProfileConfig");
    sb.append("{activation=").append(activation);
    sb.append(", deactivation=").append(deactivation);
    sb.append('}');
    return sb.toString();
  }
}
