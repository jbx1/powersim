package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.configuration.SimulationConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.SimulationConfigurationLoader;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:29
 */
public class SimulatorImpl implements Runnable, Simulator, Simulation
{
  protected static final Logger log = LoggerFactory.getLogger(SimulatorImpl.class);

  private List<Policy> policies = new ArrayList<>();


  private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
  };

  /**
   * The simulation configuration including name, duration and granularity
   */
  private SimulationConfig simulationConfig;

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

  /**
   * Whether Indices on the Consumption Table should be turned off during Simulation
   */
  private boolean turnOffIndices = true;

  @Autowired
  private SimulationRepository simulationRepository;


  public SimulatorImpl(SimulationConfigurationLoader simulationConfigurationLoader)
  {
    this.simulationConfig = simulationConfigurationLoader.getSimulationConfiguration();
    setPolicies(simulationConfigurationLoader.getSimulationPolicies(simulationConfig));
  }

  public String getName()
  {
    return simulationConfig.getName();
  }

  public List<Policy> getPolicies()
  {
    return policies;
  }

  public void setPolicies(List<Policy> policies)
  {
    this.policies = policies;
  }

  public boolean isTurnOffIndices()
  {
    return turnOffIndices;
  }

  public void setTurnOffIndices(boolean turnOffIndices)
  {
    this.turnOffIndices = turnOffIndices;
  }

  public void start()
  {
    Thread thread = new Thread(this);
    thread.setName("Simulator");
    thread.start();
  }

  @Override
  public void run()
  {
    log.info("Preparing simulation, turning off indexes for faster data insertion.");
    if (isTurnOffIndices())
    {
      simulationRepository.turnOffConsumptionIndexes();
    }

    //todo: start simulation some time earlier to simulate appliances that are already on before the simulation starts

    log.info("Preparing Simulation!");
    long nowMillis = System.currentTimeMillis();
    long actualStart = nowMillis;

    //set the start simulation time as today, but starting from midnight
    Calendar calSimulatedStart = Calendar.getInstance();
    calSimulatedStart.setTime(simulationConfig.getSimulatedStartTime());

    long simulatedStart = calSimulatedStart.getTimeInMillis();

    this.simulationData = simulationRepository.registerSimulation(getName(), new Date(actualStart), new Date(simulatedStart));

    for (Policy policy : policies)
    {
      log.info("Setting up policy {}", policy);
      List<? extends Household> households = policy.setup();
      simulationRepository.registerHouseholds(simulationData, policy, households);
      log.info("Policy {} registered {} households", policy, households.size());
    }

    this.currentTimeSlot = new Timeslot(simulatedStart, simulatedStart + getTimeslotDuration());
    this.timeslotCount = 0;


    log.info("*** Starting Simulation Execution at {} with granularity {}ms", this.currentTimeSlot.getStartTime().getTime(), this.getTimeslotDuration());

    long elapsed = 0;
    long simulatedElapsed = 0;
    long simStart = System.currentTimeMillis();

    do
    {
      timeslotCount++;
      log.trace("Registering timeslot to database");
      currentTimeSlotData = simulationRepository.registerTimeslot(simulationData, currentTimeSlot);
      simulationContext = new SimulationContext(this, currentTimeSlot);
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
        policy.ready(1000);
      }
      log.debug("All policies complete from timeslot {} simulated current time {}", timeslotCount, sdf.get().format(currentTimeSlot.getEndTime().getTime()));

      log.trace("Flushing any remaining deferred consumption events ");
      simulationRepository.flushDeferred();

      log.trace("Saving aggregate load {} ", currentAggregateLoadData);
      simulationRepository.saveAggregateLoadData(currentAggregateLoadData);
//      log.debug("Saved aggregate data");

      log.trace("Progressing timeslot");
      progressTimeslot();
      nowMillis = System.currentTimeMillis();
 //     log.debug("Elapsed time: {}", now.getTime() - actualStart.getTime());
      elapsed = nowMillis - simStart;
      simulatedElapsed = currentTimeSlot.getStartTime().getTimeInMillis() - simulatedStart;
    }
    while (simulatedElapsed < getSimulationDuration());      //run for 24 hours

    log.info("Completed {} time ticks in {}ms", timeslotCount, elapsed);
    simulationData.setActualEndTime(new Date(nowMillis));
    simulationData.setSimulatedEndTime(currentTimeSlot.getEndTime().getTime());
    simulationRepository.updateSimulationData(simulationData);

    simulationRepository.shutdown();
    log.info("*** Simulation execution ready!");

    if (isTurnOffIndices())         //if they were turned off before simulation, we need to recreate them
    {
      simulationRepository.turnOnConsumptionIndexes();
    }

    log.info("Simulation process complete.");
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
    return simulationConfig.getGranularityMins() * 60000;
  }

  public long getSimulationDuration()
  {
    return simulationConfig.getDurationMins() * 60000;
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


  @Override
  public void notifyConsumptionEvents(List<ConsumptionEvent> consumptionEvents)
  {
    List<ConsumptionData> consumptionDataList = new ArrayList<>(consumptionEvents.size());

    for (ConsumptionEvent consumptionEvent : consumptionEvents)
    {
      try
      {
        Household household = consumptionEvent.getHousehold();
        HouseholdData householdData = simulationRepository.getRegisteredHousehold(household);

        ApplianceData applianceData = simulationRepository.registerAppliance(simulationData, consumptionEvent.getAppliance());

        ConsumptionData consumptionData = new ConsumptionData();
        consumptionData.setHouseholdData(householdData);
        consumptionData.setApplianceData(applianceData);
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
      }
    }

    simulationRepository.createBulkDeferredConcumptionData(consumptionDataList);
  }
}