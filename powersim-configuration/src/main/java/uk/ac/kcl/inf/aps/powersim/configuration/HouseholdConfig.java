package uk.ac.kcl.inf.aps.powersim.configuration;

import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.HouseholdFactory;
import uk.ac.kcl.inf.aps.powersim.api.Policy;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 14:12
 */
public abstract class HouseholdConfig<H extends Household> implements HouseholdFactory<H>
{
  public String category;
  public Policy policy;

  private SimulationConfigurationLoader configurationLoader;

  @Override
  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public Policy getPolicy()
  {
    return policy;
  }

  public void setPolicy(Policy policy)
  {
    this.policy = policy;
  }

  public void setConfigurationLoader(SimulationConfigurationLoader configurationLoader)
  {
    this.configurationLoader = configurationLoader;
  }

  public SimulationConfigurationLoader getConfigurationLoader()
  {
    return configurationLoader;
  }


  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("HouseholdConfig");
    sb.append("{category='").append(category).append('\'');
    sb.append(", policy=").append(policy);
    sb.append('}');
    return sb.toString();
  }
}
