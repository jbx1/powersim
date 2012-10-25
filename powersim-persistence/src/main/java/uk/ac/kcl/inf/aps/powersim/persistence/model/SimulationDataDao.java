package uk.ac.kcl.inf.aps.powersim.persistence.model;

import uk.ac.kcl.inf.aps.powersim.persistence.GenericDao;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:05
 */
public interface SimulationDataDao extends GenericDao<SimulationData>
{
  public List<SimulationData> findAll();
}
