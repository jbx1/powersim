package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This class batches rows together until the deferred limit is reached so that all rows are dumped at once.
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/10/12
 *         Time: 14:33
 */
@Repository("deferredConsumptionDataDao")
public class DeferredConsumptionEventDaoImpl implements DeferredConsumptionEventDao
{
  protected static final Logger log = LoggerFactory.getLogger(ConsumptionDataDao.class);

  private final ArrayBlockingQueue<ConsumptionData> deferredConsumptionDataList;
  private final ArrayBlockingQueue<ApplianceData> deferredApplianceDataList;

  @Autowired
  private ConsumptionDataDao consumptionDataDao;

  @Autowired
  private ApplianceDataDao applianceDataDao;

  public DeferredConsumptionEventDaoImpl(int deferredCapacity)
  {
    log.info("ConsumptionDataDaoImpl created with deferred capacity {}", deferredCapacity);
    this.deferredConsumptionDataList = new ArrayBlockingQueue<>(deferredCapacity);
    this.deferredApplianceDataList = new ArrayBlockingQueue<>(deferredCapacity);
  }

  public void createBulkDeferredConcumptionData(List<ConsumptionData> consumptionDataList)
  {
    if (consumptionDataList.size() > deferredConsumptionDataList.remainingCapacity())
    {
      flushDeferred();
    }

    deferredConsumptionDataList.addAll(consumptionDataList);
  }

  @Override
  public void createBulkDeferredApplianceData(List<ApplianceData> applianceDataList)
  {
    if (applianceDataList.size() > deferredApplianceDataList.remainingCapacity())
    {
      flushDeferred();
    }

    deferredApplianceDataList.addAll(applianceDataList);
  }

  @Override
  public void createDeferredApplianceData(ApplianceData applianceData)
  {
    if (deferredApplianceDataList.remainingCapacity() == 0)
    {
      flushDeferred();
    }

    deferredApplianceDataList.add(applianceData);
  }


  //todo: check for thread safety

  /**
   * Flush the deferred data to the database.
   */
  public void flushDeferred()
  {
    log.trace("Flushing {} rows", deferredConsumptionDataList.size());

    synchronized (deferredApplianceDataList)
    {
      applianceDataDao.createBulk(deferredApplianceDataList);
      deferredApplianceDataList.clear();
    }

    synchronized (deferredConsumptionDataList)
    {
      consumptionDataDao.createBulk(deferredConsumptionDataList);
      deferredConsumptionDataList.clear();
    }
  }
}
