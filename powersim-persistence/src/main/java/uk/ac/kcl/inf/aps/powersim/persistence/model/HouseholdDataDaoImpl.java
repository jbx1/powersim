package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

import javax.persistence.Query;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:20
 */
@Repository("householdDataDao")
public class HouseholdDataDaoImpl extends GenericDaoImpl<HouseholdData> implements HouseholdDataDao
{
  @Override
  @Transactional()
  public void createBulk(List<HouseholdData> householdDataList)
  {
    for (HouseholdData householdData : householdDataList)
    {
      em.persist(householdData);
    }

    em.flush();
  }

  @Override
  public int getHouseholdCountForSimulation(long simulationId)
  {
    Query query = em.createNamedQuery("HouseholdData.countForSimulation");
    query.setParameter("simulationId", simulationId);
    return ((Long) query.getSingleResult()).intValue();
  }
}
