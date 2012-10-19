package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.simulator.persistence.GenericDaoImpl;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:17
 */
@Repository("consumptionDataDao")
public class ConsumptionDataDaoImpl extends GenericDaoImpl<ConsumptionData> implements ConsumptionDataDao
{
  @Transactional
  public void createBulk(List<ConsumptionData> consumptionDataList)
  {
    for (ConsumptionData consumptionData : consumptionDataList)
    {
      em.persist(consumptionData);
    }

    em.flush();
  }
}
