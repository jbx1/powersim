package uk.ac.kcl.inf.aps.powersim.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 16/11/12
 *         Time: 11:41
 */
@Repository("dbIndexManager")
public class DbIndexManagerImpl implements DbIndexManager
{
  protected static final Logger log = LoggerFactory.getLogger(DbIndexManagerImpl.class);

  @PersistenceContext
  protected EntityManager em;

  @Transactional
  public void turnOnApplianceIndex()
  {
    log.debug("Creating consumption_appliance_id_idx");
    Query query = em.createNativeQuery("create index consumption_appliance_id_idx on consumption(appliance_id);");
    query.executeUpdate();
  }


  @Transactional
  public void turnOnHouseholdIndex()
  {
    log.debug("Creating consumption_household_id_idx");
    Query query = em.createNativeQuery("create index consumption_household_id_idx on consumption(household_id);");
    query.executeUpdate();
  }

  @Transactional
  public void turnOnTimeslotIndex()
  {
    log.debug("Creating consumption_timeslot_id_idx");
    Query query = em.createNativeQuery("create index consumption_timeslot_id_idx on consumption(timeslot_id);");
    query.executeUpdate();
  }

  @Transactional
  public void turnOffApplianceIndex()
  {
    log.debug("Dropping consumption_appliance_id_idx");
    Query query = em.createNativeQuery("drop index consumption_appliance_id_idx");
    query.executeUpdate();
  }

  @Transactional
  public void turnOffHouseholdIndex()
  {
    log.debug("Dropping consumption_household_id_idx");
    Query query = em.createNativeQuery("drop index consumption_household_id_idx");
    query.executeUpdate();
  }

  @Transactional
  public void turnOffTimeslotIndex()
  {
    log.debug("Dropping consumption_timeslot_id_idx");
    Query query = em.createNativeQuery("drop index consumption_timeslot_id_idx");
    query.executeUpdate();
  }
}
