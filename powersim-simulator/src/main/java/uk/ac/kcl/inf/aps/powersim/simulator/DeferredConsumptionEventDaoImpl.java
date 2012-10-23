package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.persistence.model.ApplianceData;
import uk.ac.kcl.inf.aps.powersim.persistence.model.ApplianceDataDao;
import uk.ac.kcl.inf.aps.powersim.persistence.model.ConsumptionData;
import uk.ac.kcl.inf.aps.powersim.persistence.model.ConsumptionDataDao;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class batches rows together until the deferred limit is reached so that all rows are dumped at once.
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/10/12
 *         Time: 14:33
 */
@Repository("deferredConsumptionDataDao")
public class DeferredConsumptionEventDaoImpl implements DeferredConsumptionEventDao
{
  protected static final Logger log = LoggerFactory.getLogger(DeferredConsumptionEventDaoImpl.class);

  private ArrayBlockingQueue<ConsumptionData> deferredConsumptionDataList;
  private ArrayBlockingQueue<ApplianceData> deferredApplianceDataList;

  private int deferredCapacity;

  @Autowired
  private ConsumptionDataDao consumptionDataDao;

  @Autowired
  private ApplianceDataDao applianceDataDao;

  @Autowired
  private ThreadPoolTaskExecutor databaseTaskExecutor;

  public DeferredConsumptionEventDaoImpl(int deferredCapacity)
  {
    log.info("ConsumptionDataDaoImpl created with deferred capacity {}", deferredCapacity);
    this.deferredCapacity = deferredCapacity;

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

  @Override
  public void shutdown()
  {
    log.info("Shutting down database task executor ...");
    databaseTaskExecutor.shutdown();

    try
    {
      while (!databaseTaskExecutor.getThreadPoolExecutor().awaitTermination(10, TimeUnit.SECONDS))
      {
        log.info("Still waiting for all tasks to terminate ...");
      }
    }
    catch (InterruptedException ex)
    {
      log.warn("Termination wait interrupted!", ex);
    }
    log.info("Database task scheduler shutdown complete.");
  }


  /**
   * Flush the deferred data to the database.
   */
  public void flushDeferred()
  {
    final ArrayBlockingQueue<ApplianceData> applianceDataToFlush = deferredApplianceDataList;
    final ArrayBlockingQueue<ConsumptionData> consumptionDataToFlush = deferredConsumptionDataList;

    //create new queues so that the old ones can be flushed in the meantime to the database
    deferredApplianceDataList = new ArrayBlockingQueue<ApplianceData>(this.deferredCapacity);
    deferredConsumptionDataList = new ArrayBlockingQueue<ConsumptionData>(this.deferredCapacity);

    //flush appliances within this thread to avoid other threads referring to the appliance without having it saved
    if (applianceDataToFlush.size() > 0)
    {
      log.debug("Adding {} appliance(s)", applianceDataToFlush.size());
      applianceDataDao.createBulk(applianceDataToFlush);
      applianceDataToFlush.clear();
    }

    log.trace("Flushing events in separate thread.");
    databaseTaskExecutor.execute(new Runnable()
    {
      public void run()
      {
        log.trace("Flushing {} consumption rows", consumptionDataToFlush.size());
        consumptionDataDao.createBulk(consumptionDataToFlush);
        consumptionDataToFlush.clear();
        log.trace("Flush ready");
      }
    });
  }

  //todo: move database registration stuff to here from SimulatorImpl
  //todo: rename to something Repository
}
