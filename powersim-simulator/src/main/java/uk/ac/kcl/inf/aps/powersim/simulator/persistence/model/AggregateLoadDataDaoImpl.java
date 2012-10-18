package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.simulator.persistence.GenericDaoImpl;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 18:44
 */
@Repository("aggregateLoadDataDao")
public class AggregateLoadDataDaoImpl extends GenericDaoImpl<AggregateLoadData> implements AggregateLoadDataDao
{
}
