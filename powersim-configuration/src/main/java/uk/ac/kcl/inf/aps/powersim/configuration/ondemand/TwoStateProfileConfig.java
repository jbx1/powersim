package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.configuration.ProfileConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.TwoStateAppliance;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:48
 */
public class TwoStateProfileConfig extends ProfileConfig<TwoStateAppliance>
{
  protected static final Logger log = LoggerFactory.getLogger(TwoStateProfileConfig.class);

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
    log.debug("Setting activation and deactivation strategies for {}", appliance);
    appliance.setActivateStrategy(activation.getStochasticProcessInstance());
    appliance.setDeactivateStrategy(deactivation.getStochasticProcessInstance());
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
