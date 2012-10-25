package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:06
 */
@Repository("simulationDataDao")
public class SimulationDataDaoImpl extends GenericDaoImpl<SimulationData> implements SimulationDataDao
{
  @Override
  public List<SimulationData> findAll()
  {
    TypedQuery<SimulationData> query = em.createNamedQuery("SimulationData.findAll", SimulationData.class);
    return query.getResultList();
  }
}
