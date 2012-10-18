package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.simulator.persistence.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    Date now = new Date(System.currentTimeMillis());
    Date actualStart = new Date(now.getTime());
    Date simulatedStart = new Date(actualStart.getTime());     //todo: get the start time of the timeslot from configuration

    this.simulationData = registerSimulation("Test", actualStart, simulatedStart);

    for (Policy policy : policies)
    {
      log.info("Setting up policy {}", policy);
      List<Household> households = policy.setup();
      for (Household household : households)
      {
        registerHousehold(household, policy);
      }
      log.info("Policy {} registered {} households", policy, households.size());
    }

    //todo: use the configured simulated time
    this.currentTimeSlot = new Timeslot(simulatedStart, new Date(simulatedStart.getTime() + timeslotDuration));
    this.timeslotCount = 0;

    log.info("Starting simulation at {} with granularity {}ms", this.currentTimeSlot.getStartTime(), this.getTimeslotDuration());

    long elapsed = 0;
    long nowMillis = 0;

    long simStart = System.currentTimeMillis();
    do
    {
      timeslotCount++;
      log.debug("Saving timeslot data.");
      TimeslotData timeslotData = registerTimeslot(currentTimeSlot, simulationData);
      simulationContext = new SimulationContext(this, currentTimeSlot);  //todo: more complex information such as weather
//      log.info("Simulation Context {}", simulationContext);
      log.debug("Saved timeslot data.");

      AggregateLoadData aggregateLoadData = new AggregateLoadData();
      aggregateLoadData.setTimeslotData(timeslotData);

      //notify all policies with the new timeslot
      for (Policy policy : policies)
      {
        //handle Time slot is asynchronous, so all will return immediately
        policy.handleTimeSlot(simulationContext);
      }

      //wait for all policies to complete their tasks
      for (Policy policy : policies)
      {
        //wait at most 10 seconds for a policy to be ready
        policy.ready(10000);
      }

      log.debug("Saving aggregate data");
      aggregateLoadDataDao.create(aggregateLoadData);
      log.debug("Saved aggregate data");

      progressTimeslot();
      nowMillis = System.currentTimeMillis();
 //     log.debug("Elapsed time: {}", now.getTime() - actualStart.getTime());
      elapsed = nowMillis - simStart;
    }
    while (elapsed < 60000);

    log.info("Completed {} time ticks in {}ms", timeslotCount, elapsed);
    simulationData.setActualEndTime(now);
    simulationData.setSimulatedEndTime(currentTimeSlot.getEndTime());
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

  /**
   * Progresses the current timeslot by the time specified in timeslotDuration
   */
  private void progressTimeslot()
  {
    long startTime = currentTimeSlot.getEndTime().getTime() + 1;
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
   * Registers a household being governed by a policy to the database.
   * @param household - the household
   * @param policy - the policy
   * @return the Entity representing the household in the database
   */
  public HouseholdData registerHousehold(Household household, Policy policy)
  {
    HouseholdData householdData = new HouseholdData();
    householdData.setSimulationData(getSimulationData());
    householdData.setCategory(household.getCategory());
    householdData.setReferenceId(household.getUid());
    householdData.setPolicyDescriptor(policy.getDescriptor());

    //todo: register the list of households in memory
    //todo: create them all at once (bulk create in some way, using one transaction and prepared statments?)
    householdDataDao.create(householdData);

    return householdData;
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
    timeslotData.setStartTime(timeslot.getStartTime());
    timeslotData.setEndTime(timeslot.getEndTime());

    timeslotDataDao.create(timeslotData);

    return timeslotData;
  }

  @Override
  public String getName()
  {
    return getSimulationData().getName();
  }

  @Override
  public void handleConsumptionEvents(List<ConsumptionEvent> consumptionEvents)
  {
    //todo: register any new appliances
    //todo: update the aggregate load
    //todo: insert the consumption events
  }
}