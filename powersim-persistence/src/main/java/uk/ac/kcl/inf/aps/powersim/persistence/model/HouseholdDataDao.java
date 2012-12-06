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

  public List<HouseholdData> getHouseholdsForSimulation(long simulationId, int offset, int limit);

  public List<HouseholdData> getHouseholdsForPolicy(long simulationId, String policy, int offset, int limit);

  public List<HouseholdData> getHouseholdsForCategory(long simulationId, String category, int offset, int limit);

  public List<HouseholdData> getHouseholdsForPolicyAndCategory(long simulationId, String policy, String category, int offset, int limit);

  public List<String> getCategoriesForSimulation(long simulationId);

  public List<String> getCategoriesForPolicy(long simulationId, String policy);

  public List<String> getPoliciesForSimulation(long simulationId);

  public int deleteBySimulationId(long simulationId);
}
