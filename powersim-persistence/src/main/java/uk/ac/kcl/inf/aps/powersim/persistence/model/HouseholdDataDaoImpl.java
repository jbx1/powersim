package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
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

  @Override
  public List<HouseholdData> getHouseholdsForSimulation(long simulationId, int offset, int limit)
  {
    TypedQuery<HouseholdData> query = em.createNamedQuery("HouseholdData.getForSimulation", HouseholdData.class);
    query.setParameter("simulationId", simulationId);
    query.setFirstResult(offset);
    query.setMaxResults(limit);

    return query.getResultList();
  }

  @Override
  public List<HouseholdData> getHouseholdsForPolicy(long simulationId, String policy, int offset, int limit)
  {
    TypedQuery<HouseholdData> query = em.createNamedQuery("HouseholdData.getForPolicy", HouseholdData.class);
    query.setParameter("simulationId", simulationId);
    query.setParameter("policyDescriptor", policy);
    query.setFirstResult(offset);
    query.setMaxResults(limit);

    return query.getResultList();
  }

  @Override
  public int getHouseholdCountForPolicy(long simulationId, String policy)
  {
    Query query = em.createNamedQuery("HouseholdData.countForPolicy");
    query.setParameter("simulationId", simulationId);
    query.setParameter("policyDescriptor", policy);

    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  public List<HouseholdData> getHouseholdsForCategory(long simulationId, String category, int offset, int limit)
  {
    TypedQuery<HouseholdData> query = em.createNamedQuery("HouseholdData.getForCategory", HouseholdData.class);
    query.setParameter("simulationId", simulationId);
    query.setParameter("category", category);
    query.setFirstResult(offset);
    query.setMaxResults(limit);

    return query.getResultList();
  }

  @Override
  public int getHouseholdCountForCategory(long simulationId, String category)
  {
    Query query = em.createNamedQuery("HouseholdData.countForCategory");
    query.setParameter("simulationId", simulationId);
    query.setParameter("category", category);

    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  public List<HouseholdData> getHouseholdsForPolicyAndCategory(long simulationId, String policy, String category, int offset, int limit)
  {
    TypedQuery<HouseholdData> query = em.createNamedQuery("HouseholdData.getForPolicyAndCategory", HouseholdData.class);
    query.setParameter("simulationId", simulationId);
    query.setParameter("policyDescriptor", policy);
    query.setParameter("category", category);
    query.setFirstResult(offset);
    query.setMaxResults(limit);

    return query.getResultList();
  }

  @Override
  public int getHouseholdCountForPolicyAndCategory(long simulationId, String policy, String category)
  {
    Query query = em.createNamedQuery("HouseholdData.countForPolicyAndCategory");
    query.setParameter("simulationId", simulationId);
    query.setParameter("policyDescriptor", policy);
    query.setParameter("category", category);

    return ((Long) query.getSingleResult()).intValue();
  }

  @Override
  public List<String> getCategoriesForSimulation(long simulationId)
  {
    TypedQuery<String> query = em.createNamedQuery("HouseholdData.getCategoriesForSimulation", String.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  }

  @Override
  public List<String> getCategoriesForPolicy(long simulationId, String policy)
  {
    TypedQuery<String> query = em.createNamedQuery("HouseholdData.getCategoriesForPolicy", String.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  }

  @Override
  public List<String> getPoliciesForSimulation(long simulationId)
  {
    TypedQuery<String> query = em.createNamedQuery("HouseholdData.getPoliciesForSimulation", String.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  }

  @Override
  @Transactional
  public int deleteBySimulationId(long simulationId)
  {
    Query query = em.createNamedQuery("HouseholdData.deleteBySimulationId");
    query.setParameter("simulationId", simulationId);
    return query.executeUpdate();
  }
}
