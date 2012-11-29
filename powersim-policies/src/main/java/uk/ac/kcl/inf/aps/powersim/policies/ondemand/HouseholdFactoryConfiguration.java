package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.HouseholdFactory;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 29/11/12
 *         Time: 12:07
 */
public class HouseholdFactoryConfiguration<T extends Household>
{
  private final HouseholdFactory<T> householdFactory;
  private final int householdCount;

  public HouseholdFactoryConfiguration(HouseholdFactory<T> householdFactory, int householdCount)
  {
    this.householdFactory = householdFactory;
    this.householdCount = householdCount;
  }

  public HouseholdFactory<T> getHouseholdFactory()
  {
    return householdFactory;
  }

  public int getHouseholdCount()
  {
    return householdCount;
  }
}
