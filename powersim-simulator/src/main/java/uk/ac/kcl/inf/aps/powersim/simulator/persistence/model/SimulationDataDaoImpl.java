package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.simulator.persistence.GenericDaoImpl;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:06
 */
@Repository("simulationDataDao")
public class SimulationDataDaoImpl extends GenericDaoImpl<SimulationData> implements SimulationDataDao
{

}
