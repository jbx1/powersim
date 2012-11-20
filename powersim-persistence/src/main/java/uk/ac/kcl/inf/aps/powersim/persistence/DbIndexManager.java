package uk.ac.kcl.inf.aps.powersim.persistence;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 16/11/12
 *         Time: 11:48
 */
public interface DbIndexManager
{
  public void turnOnApplianceIndex();

  public void turnOffApplianceIndex();

  public void turnOnHouseholdIndex();

  public void turnOffHouseholdIndex();

  public void turnOnTimeslotIndex();

  public void turnOffTimeslotIndex();

  public void analyzeIndexes();
}
