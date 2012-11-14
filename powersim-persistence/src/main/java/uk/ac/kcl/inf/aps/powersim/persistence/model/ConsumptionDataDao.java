package uk.ac.kcl.inf.aps.powersim.persistence.model;

import uk.ac.kcl.inf.aps.powersim.persistence.GenericDao;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:09
 */
public interface ConsumptionDataDao extends GenericDao<ConsumptionData>
{
  public void createBulk(Iterable<ConsumptionData> consumptionDataList);

  public int deleteBySimulationId(long simulationId);
}
