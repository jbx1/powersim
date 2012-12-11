package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:16
 */
@Repository("applianceDataDao")
public class ApplianceDataDaoImpl extends GenericDaoImpl<ApplianceData> implements ApplianceDataDao
{
  @Transactional
  @Override
  public void createBulk(Iterable<ApplianceData> applianceDataList)
  {
    for (ApplianceData applianceData : applianceDataList)
    {
      em.persist(applianceData);
    }

    em.flush();
  }

  @Override
  public int getApplianceCountForSimulation(long simulationId)
  {
    Query query = em.createNamedQuery("ApplianceData.countForSimulation");
    query.setParameter("simulationId", simulationId);
    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  @Transactional
  public int deleteBySimulationId(long simulationId)
  {
    Query query = em.createNamedQuery("ApplianceData.deleteBySimulationId");
    query.setParameter("simulationId", simulationId);
    return query.executeUpdate();
  }

  @Override
  public List<ApplianceData> getAppliancesForHousehold(long householdId)
  {
    TypedQuery<ApplianceData> query = em.createNamedQuery("ApplianceData.getForHousehold", ApplianceData.class);
    query.setParameter("householdId", householdId);

    return query.getResultList();
  }

  @Override
  public Map<HouseholdData, ApplianceData> getAppliancesForHouseholds(List<HouseholdData> households)
  {
    Query query = em.createNamedQuery("ApplianceData.getForHouseholds");
    query.setParameter("households", households);

    List<Object[]> householdsAndAppliances = query.getResultList();
    Map<HouseholdData, ApplianceData> householdApplianceMap = new TreeMap<>();

    for (Object[] row : householdsAndAppliances)
    {
      householdApplianceMap.put((HouseholdData) row[0], (ApplianceData) row[1]);
    }

    return householdApplianceMap;
  }
}
