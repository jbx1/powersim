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
 *         Time: 16:09
 */
@Repository("timeslotDataDao")
public class TimeslotDataDaoImpl extends GenericDaoImpl<TimeslotData> implements TimeslotDataDao
{
  public List<TimeslotData> findAll(Long simulationId)
  {
    TypedQuery<TimeslotData> query = em.createNamedQuery("TimeslotData.findAll", TimeslotData.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  }

  @Override
  @Transactional
  public int deleteBySimulationId(long simulationId)
  {
    Query query = em.createNamedQuery("TimeslotData.deleteBySimulationId");
    query.setParameter("simulationId", simulationId);
    return query.executeUpdate();
  }
}
