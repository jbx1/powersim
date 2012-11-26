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
}
