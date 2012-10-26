package uk.ac.kcl.inf.aps.powersim.persistence.model;

import uk.ac.kcl.inf.aps.powersim.persistence.GenericDao;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:09
 */
public interface HouseholdDataDao extends GenericDao<HouseholdData>
{
  public void createBulk(List<HouseholdData> householdDataList);

  public int getHouseholdCountForSimulation(long simulationId);
}
