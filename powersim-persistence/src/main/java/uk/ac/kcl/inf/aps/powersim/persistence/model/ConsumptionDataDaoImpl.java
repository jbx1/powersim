package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:17
 */
@Repository("consumptionDataDao")
public class ConsumptionDataDaoImpl extends GenericDaoImpl<ConsumptionData> implements ConsumptionDataDao
{
  protected static final Logger log = LoggerFactory.getLogger(ConsumptionDataDaoImpl.class);

  @Transactional
  public void createBulk(Iterable<ConsumptionData> consumptionDataList)
  {
    log.trace("Inserting batch of ConsumptionData rows");
    for (ConsumptionData consumptionData : consumptionDataList)
    {
      em.persist(consumptionData);
    }

    em.flush();
    log.trace("ConsumptionData batch insert complete");
  }
}
