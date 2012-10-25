package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;
import uk.ac.kcl.inf.aps.powersim.persistence.reporting.SimulationTimeslotAggregateData;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 18:44
 */
@Repository("aggregateLoadDataDao")
public class AggregateLoadDataDaoImpl extends GenericDaoImpl<AggregateLoadData> implements AggregateLoadDataDao
{
/*  @Override
  public List<AggregateLoadData> getAggregateLoadDataForSimulation(Long simulationId, Date startTime, Date endTime)
  {
    TypedQuery<AggregateLoadData> query = em.createNamedQuery("AggregateLoadData.getAggregateLoadForSimulation", AggregateLoadData.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  } */


  @Override
  public List<SimulationTimeslotAggregateData> getAggregateLoadDataForSimulation(Long simulationId, Date startTime, Date endTime)
  {
    TypedQuery<SimulationTimeslotAggregateData> query = em.createNamedQuery("AggregateLoadData.getAggregateLoadForSimulation", SimulationTimeslotAggregateData.class);
    query.setParameter("simulationId", simulationId);
    return query.getResultList();
  }
}
