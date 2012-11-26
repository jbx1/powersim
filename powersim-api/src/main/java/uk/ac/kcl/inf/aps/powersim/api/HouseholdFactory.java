package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 15:24
 */
public interface HouseholdFactory<H extends Household>
{
  public H getHouseholdInstance();

  public String getCategory();
}
