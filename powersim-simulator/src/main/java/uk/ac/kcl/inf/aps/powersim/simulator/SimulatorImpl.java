package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.simulator.persistence.model.*;

import java.util.*;

import static uk.ac.kcl.inf.aps.powersim.simulator.Constants.DEFAULT_TIMESLOT_DURATION;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:29
 */
public class SimulatorImpl implements Runnable, Simulator, Simulation
{
  protected static final Logger log = LoggerFactory.getLogger(SimulatorImpl.class);


  private List<Policy> policies = new ArrayList<Policy>();

  private Thread thread;

  private Map<String, HouseholdData> householdDataMap = new TreeMap<>();
  private Map<String, ApplianceData> applianceDataMap = new TreeMap<>();

  /**
   * Duration of each timeslot (in milliseconds)
   */
  private long timeslotDuration = DEFAULT_TIMESLOT_DURATION;

  /**
   * The current simulation data.
   */
  private SimulationData simulationData;

  /**
   * The current simulation context.
   */
  private SimulationContext simulationContext;

  /**
   * The time slot currently being simulated
   */
  private Timeslot currentTimeSlot;


  /**
   * The database entity representing the current timeslot.
   */
  private TimeslotData currentTimeSlotData;

  /**
   * A counter counting how many timeslots where created
   */
  private int timeslotCount;

  /**
   * The current aggregate load data, for the current timeslot
   */
  private AggregateLoadData currentAggregateLoadData;

  //todo: move the following to a separate Data handler class
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
  private DeferredConsumptionEventDao deferredConsumptionEventDao;

  //todo: configuration (time intervals, duration, etc.) use YAML?

  public SimulatorImpl()
  {
  }

  public void start()
  {
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run()
  {
    log.info("Starting Simulation!");
    long now = System.currentTimeMillis();
    long actualStart = now;
    //todo: get the start time of the timeslot from configuration
    long simulatedStart = actualStart;

    this.simulationData = registerSimulation("Test", new Date(actualStart), new Date(simulatedStart));

    for (Policy policy : policies)
    {
      log.info("Setting up policy {}", policy);
      List<? extends Household> households = policy.setup();
      registerHouseholds(households, policy);
      log.info("Policy {} registered {} households", policy, households.size());
    }

    //todo: use the configured simulated time
    this.currentTimeSlot = new Timeslot(simulatedStart, simulatedStart + timeslotDuration);
    this.timeslotCount = 0;

    log.info("Starting simulation at {} with granularity {}ms", this.currentTimeSlot.getStartTime().getTime(), this.getTimeslotDuration());

    long elapsed = 0;
    long nowMillis = 0;

    long simStart = System.currentTimeMillis();
    do
    {
      timeslotCount++;
      log.trace("Registering timeslot to database");
      currentTimeSlotData = registerTimeslot(currentTimeSlot, simulationData);
      simulationContext = new SimulationContext(this, currentTimeSlot);  //todo: more complex information such as weather
      log.trace("Simulation Context {}", simulationContext);

      this.currentAggregateLoadData = new AggregateLoadData();
      this.currentAggregateLoadData.setTimeslotData(currentTimeSlotData);

      //notify all policies with the new timeslot
      log.trace("Notifying all policies of the new timeslot");
      for (Policy policy : policies)
      {
        //handle Time slot is asynchronous, so all will return immediately
        policy.handleTimeSlot(simulationContext);
      }

      log.trace("Waiting for all policies to complete tasks");
      //wait for all policies to complete their tasks
      for (Policy policy : policies)
      {
        //wait at most 1 second for a policy to be ready
        policy.ready(1000); //todo: configurable
      }
      log.debug("All policies complete from timeslot {}", timeslotCount);

      log.trace("Flushing any remaining deferred consumption events ");
      deferredConsumptionEventDao.flushDeferred();

      log.trace("Saving aggregate data.");
      aggregateLoadDataDao.create(currentAggregateLoadData);
//      log.debug("Saved aggregate data");

      log.trace("Progressing timeslot");
      progressTimeslot();
      nowMillis = System.currentTimeMillis();
 //     log.debug("Elapsed time: {}", now.getTime() - actualStart.getTime());
      elapsed = nowMillis - simStart;
    }
    while (elapsed < 60000);

    log.info("Completed {} time ticks in {}ms", timeslotCount, elapsed);
    simulationData.setActualEndTime(new Date(now));
    simulationData.setSimulatedEndTime(currentTimeSlot.getEndTime().getTime());
    simulationDataDao.update(simulationData);

    log.info("Simulation Ready!");
  }

  /**
   * @return the SimulationData entity in the database associated with this simulation.
   */
  public synchronized SimulationData getSimulationData()
  {
    return simulationData;
  }

  public Timeslot getCurrentTimeSlot()
  {
    return currentTimeSlot;
  }

  public void setCurrentTimeSlot(Timeslot currentTimeSlot)
  {
    this.currentTimeSlot = currentTimeSlot;
  }

  public AggregateLoadData getCurrentAggregateLoadData()
  {
    return currentAggregateLoadData;
  }

  public void setCurrentAggregateLoadData(AggregateLoadData currentAggregateLoadData)
  {
    this.currentAggregateLoadData = currentAggregateLoadData;
  }

  public long getTimeslotDuration()
  {
    return timeslotDuration;
  }

  public TimeslotData getCurrentTimeSlotData()
  {
    return currentTimeSlotData;
  }

  /**
   * Progresses the current timeslot by the time specified in timeslotDuration
   */
  private void progressTimeslot()
  {
    long startTime = currentTimeSlot.getEndTime().getTimeInMillis() + 1;
    long endTime = startTime + getTimeslotDuration();

    this.currentTimeSlot = new Timeslot(new Date(startTime), new Date(endTime));
  }

  /**
   * Registers a new simulation in the database.
   * @param name - a user-friendly name of the simulation
   * @param actualStart - the actual timestamp of when the simulation actually started
   * @param simulatedStart - the timestamp being simulated
   * @return the Entity representing the simulation in the database
   */
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
   * @param households - the household list
   * @param policy - the policy
   * @return the Entity representing the household in the database
   */
  public void registerHouseholds(List<? extends Household> households, Policy policy)
  {
    List<HouseholdData> householdDataList = new ArrayList<>(households.size());
    for (Household household : households)
    {
      HouseholdData householdData = new HouseholdData();
      householdData.setSimulationData(getSimulationData());
      householdData.setCategory(household.getCategory());
      householdData.setReferenceId(household.getUid());
      householdData.setPolicyDescriptor(policy.getDescriptor());

      householdDataList.add(householdData);
      householdDataMap.put(household.getUid(), householdData);
    }

    //todo: create them all at once (bulk create in some way, using one transaction and prepared statments?)
    householdDataDao.createBulk(householdDataList);
  }

  public ApplianceData registerAppliance(Household household, Appliance appliance)
          throws HouseholdNotRegisteredException
  {
    HouseholdData householdData = householdDataMap.get(household.getUid());
    if (householdData == null)
    {
      log.error("{} was not registered before! Ignoring {}", household, appliance);
      throw new HouseholdNotRegisteredException(household);
    }

    ApplianceData applianceData = new ApplianceData();

    applianceData.setHouseholdData(householdData);
    applianceData.setReferenceId(appliance.getUid());
    applianceData.setType(appliance.getType());

//    applianceDataDao.create(applianceData);
    deferredConsumptionEventDao.createDeferredApplianceData(applianceData);
    applianceDataMap.put(appliance.getUid(), applianceData);

    return applianceData;
  }

  /**
   * Registers a new timeslot for a simulation.
   * @param timeslot - the Timeslot
   * @param simulationData - the simulationdata Entity returned previously by registering the new simulation.
   * @return the Entity representing the Timeslot being simulated.
   */
  public TimeslotData registerTimeslot(Timeslot timeslot, SimulationData simulationData)
  {
    TimeslotData timeslotData = new TimeslotData();
    timeslotData.setSimulationData(simulationData);
    timeslotData.setStartTime(timeslot.getStartTime().getTime());
    timeslotData.setEndTime(timeslot.getEndTime().getTime());

    timeslotDataDao.create(timeslotData);

    return timeslotData;
  }

  @Override
  public String getName()
  {
    return getSimulationData().getName();
  }

  @Override
  public void notifyConsumptionEvents(List<ConsumptionEvent> consumptionEvents)
  {
    List<ConsumptionData> consumptionDataList = new ArrayList<>(consumptionEvents.size());

    for (ConsumptionEvent consumptionEvent : consumptionEvents)
    {
      try
      {
        ApplianceData applianceData = applianceDataMap.get(consumptionEvent.getAppliance().getUid());
        if (applianceData == null)
        {
          applianceData = registerAppliance(consumptionEvent.getHousehold(), consumptionEvent.getAppliance());
        }

        ConsumptionData consumptionData = new ConsumptionData();
        consumptionData.setAppliance(applianceData);
        consumptionData.setTimeslotData(getCurrentTimeSlotData());
        consumptionData.setLoadWatts(consumptionEvent.getLoadWatts());

        consumptionDataList.add(consumptionData);

        //update the aggregate data, if the load is negative it means that the load was generated by the appliance
        //if the load is positive it means that the load was consumed by the appliance
        long loadWatts = consumptionEvent.getLoadWatts();
        if (loadWatts < 0)
        {
          //load was actually generated by appliance
          long currentGenerated = this.currentAggregateLoadData.getGenerated();
          this.currentAggregateLoadData.setGenerated(currentGenerated - loadWatts); //loadWatts is already negative
        }
        else
        {
          //load was actually consumed by appliance
          long currentConsumed = this.currentAggregateLoadData.getConsumed();
          this.currentAggregateLoadData.setConsumed(currentConsumed + loadWatts);
        }
      }
      catch (HouseholdNotRegisteredException ex)
      {
        log.warn("Unable to register appliance {} since household was not found, ignoring consumption event.", ex);
        //todo: put it in an ignore list?
      }
    }

    deferredConsumptionEventDao.createBulkDeferredConcumptionData(consumptionDataList);
  }

  public List<Policy> getPolicies()
  {
    return policies;
  }

  public void setPolicies(List<Policy> policies)
  {
    this.policies = policies;
  }
}