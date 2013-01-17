package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.Policy;
import uk.ac.kcl.inf.aps.powersim.api.Timeslot;
import uk.ac.kcl.inf.aps.powersim.persistence.DbIndexManager;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * This class batches rows together until the deferred limit is reached so that all rows are dumped at once.
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/10/12
 *         Time: 14:33
 */
@Repository("simulationRepository")
public class SimulationRepositoryImpl implements SimulationRepository
{
  protected static final Logger log = LoggerFactory.getLogger(SimulationRepositoryImpl.class);

  private ArrayBlockingQueue<ConsumptionData> deferredConsumptionDataList;
  private ArrayBlockingQueue<ApplianceData> deferredApplianceDataList;

  private Map<String, HouseholdData> householdDataMap = new TreeMap<>();

  private Map<String, ApplianceData> applianceDataMap = new TreeMap<>();

  private int deferredCapacity;

  @Autowired
  private ConsumptionDataDao consumptionDataDao;

  @Autowired
  private SimulationDataDao simulationDataDao;

  @Autowired
  private HouseholdDataDao householdDataDao;

  @Autowired
  private TimeslotDataDao timeslotDataDao;

  @Autowired
  private AggregateLoadDataDao aggregateLoadDataDao;

  @Autowired
  private ApplianceDataDao applianceDataDao;

  @Autowired
  private DbIndexManager dbIndexManager;

  @Autowired
  private ThreadPoolTaskExecutor databaseTaskExecutor;


  public SimulationRepositoryImpl(int deferredCapacity)
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
  @Override
  public void flushDeferred()
  {
    final ArrayBlockingQueue<ApplianceData> applianceDataToFlush = deferredApplianceDataList;
    final ArrayBlockingQueue<ConsumptionData> consumptionDataToFlush = deferredConsumptionDataList;

    //create new queues so that the old ones can be flushed in the meantime to the database
    deferredApplianceDataList = new ArrayBlockingQueue<>(this.deferredCapacity);
    deferredConsumptionDataList = new ArrayBlockingQueue<>(this.deferredCapacity);

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

  @Override
  public void turnOffConsumptionIndexes()
  {
    log.info("Dropping indexes on Consumption table for faster inserting (deleting/analysing will be slower)...");

    try
    {
      dbIndexManager.turnOffApplianceIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to remove appliance index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    try
    {
      dbIndexManager.turnOffHouseholdIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to remove household index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    try
    {
      dbIndexManager.turnOffTimeslotIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to remove timeslot index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    log.info("Consumption Table Indexes dropped.");
  }


  @Override
  public void turnOnConsumptionIndexes()
  {
    log.info("Creating indexes on Consumption table for faster analysis and deletion...");

    try
    {
      dbIndexManager.turnOnApplianceIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to create appliance index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    try
    {
      dbIndexManager.turnOnHouseholdIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to create household index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    try
    {
      dbIndexManager.turnOnTimeslotIndex();
    }
    catch (Exception ex)
    {
      log.warn("Unable to create timeslot index, proceeding anyway. Due to: {}", ex.getMessage());
    }

    try
    {
      dbIndexManager.analyzeIndexes();
    }
    catch (Exception ex)
    {
      log.warn("Unable to run ANALYZE on database.");
    }

    log.info("Consumption Table Indexes created.");
  }

  /**
   * Registers a new simulation in the database.
   * @param name - a user-friendly name of the simulation
   * @param actualStart - the actual timestamp of when the simulation actually started
   * @param simulatedStart - the timestamp being simulated
   * @return the Entity representing the simulation in the database
   */
  @Override
  public SimulationData registerSimulation(String name, Date actualStart, Date simulatedStart)
  {
    SimulationData simulationData = new SimulationData();
    simulationData.setName(name);

    simulationData.setActualStartTime(actualStart);
    simulationData.setSimulatedStartTime(simulatedStart);
    simulationDataDao.create(simulationData);

    return simulationData;
  }

  /**
   * Registers households being governed by a policy to the database.
   * @param simulationData - the simulationdata Entity returned previously by registering the new simulation.
   * @param households - the household list
   * @param policy - the policy
   */
  @Override
  public void registerHouseholds(SimulationData simulationData, Policy policy, List<? extends Household> households)
  {
    log.debug("Registering {} households", households.size());

    List<HouseholdData> householdDataList = new ArrayList<>(households.size());
    for (Household household : households)
    {
      HouseholdData householdData = new HouseholdData();
      householdData.setSimulationData(simulationData);
      householdData.setCategory(household.getCategory());
      householdData.setReferenceId(household.getUid());
      householdData.setPolicyDescriptor(policy.getDescriptor());

      householdDataList.add(householdData);
      householdDataMap.put(household.getUid(), householdData);
    }

    householdDataDao.createBulk(householdDataList);
  }

  /**
   * Registers an appliance to the database and keeps track of it throughout the simulation.
   * If the appliance is already registers it just returns the respective Entity created beforehand.
   * @param simulationData - the simulationdata Entity returned previously by registering the new simulation.
   * @param appliance - the appliance to be registered
   * @return the Entity saved to the database
   * @throws HouseholdNotRegisteredException
   */                                                          
  @Override
  public ApplianceData registerAppliance(SimulationData simulationData, Appliance appliance)
          throws HouseholdNotRegisteredException
  {
    ApplianceData applianceData = applianceDataMap.get(appliance.getUid());
    if (applianceData == null)
    {                                                          
       applianceData = new ApplianceData();

       applianceData.setReferenceId(appliance.getUid());
       applianceData.setType(appliance.getType());
       applianceData.setSimulationData(simulationData);

       createDeferredApplianceData(applianceData);
       applianceDataMap.put(appliance.getUid(), applianceData);
    }

    return applianceData;
  }

  @Override
  public HouseholdData getRegisteredHousehold(Household household)
          throws HouseholdNotRegisteredException
  {
    HouseholdData householdData = householdDataMap.get(household.getUid());
    if (householdData == null)
    {
      log.error("{} was not registered before! Ignoring event!", household);
      throw new HouseholdNotRegisteredException(household);
    }
    return householdData;
  }

  /**
   * Registers a new timeslot for a simulation.
   * @param simulationData - the simulationdata Entity returned previously by registering the new simulation.
   * @param timeslot - the Timeslot
   * @return the Entity representing the Timeslot being simulated.
   */
  @Override
  public TimeslotData registerTimeslot(SimulationData simulationData, Timeslot timeslot)
  {
    TimeslotData timeslotData = new TimeslotData();
    timeslotData.setSimulationData(simulationData);
    timeslotData.setStartTime(timeslot.getStartTime().getTime());
    timeslotData.setEndTime(timeslot.getEndTime().getTime());

    timeslotDataDao.create(timeslotData);

    return timeslotData;
  }

  @Override
  public void saveAggregateLoadData(AggregateLoadData aggregateLoadData)
  {
    aggregateLoadDataDao.create(aggregateLoadData);
  }

  @Override
  public void updateSimulationData(SimulationData simulationData)
  {
    simulationDataDao.update(simulationData);
  }
}
