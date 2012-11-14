package uk.ac.kcl.inf.aps.powersim.persistence.model;

import uk.ac.kcl.inf.aps.powersim.persistence.GenericDao;
import uk.ac.kcl.inf.aps.powersim.persistence.reporting.SimulationTimeslotAggregateData;

import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 18:43
 */
public interface AggregateLoadDataDao extends GenericDao<AggregateLoadData>
{
  public List<SimulationTimeslotAggregateData> getAggregateLoadDataForSimulation(Long simulationId, Date startTime, Date endTime);

  public int deleteBySimulationId(long simulationId);
}
