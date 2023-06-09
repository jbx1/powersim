package uk.ac.kcl.inf.aps.powersim.persistence.model;

import uk.ac.kcl.inf.aps.powersim.persistence.GenericDao;

import java.util.List;
import java.util.Map;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:09
 */
public interface ApplianceDataDao extends GenericDao<ApplianceData>
{
  public void createBulk(Iterable<ApplianceData> applianceDataList);

  public int getApplianceCountForSimulation(long simulationId);

  public int deleteBySimulationId(long simulationId);

  public List<ApplianceData> getAppliancesForHousehold(long householdId);

  public Map<HouseholdData, ApplianceData> getAppliancesForHouseholds(List<HouseholdData> households);
}
