package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.ActivityRequest;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.api.Timeslot;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.StochasticProcess;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implements a two-state appliance. This can be either an On/Off appliance, or a High/low consumption appliance.
 * Two wattage specifications are provided, one for when the appliance is in the ON (active) state, and one for when the appliance
 * is in the OFF (inactive) state. For a purely On/Off state, the inactiveWattage is 0.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 17:06
 */
public class TwoStateAppliance extends EnergyOnDemandAppliance
{
  protected static final Logger log = LoggerFactory.getLogger(TwoStateAppliance.class);

  private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
  };

  private enum STATUS
  {
    INACTIVE,
    ACTIVE,
    PENDING_ACTIVATION;
  }

  private STATUS status = STATUS.INACTIVE;

  private ActivityRequest currentActivity = null;

  private Calendar nextSwitchTime = null;

  /**
   * The wattage consumed when the appliance is active.
   */
  private long activeWattage;

  /**
   * The wattage consumed when the appliance is inactive
   */
  private long inactiveWattage;


  private StochasticProcess activateStrategy;

  private StochasticProcess deactivateStrategy;


  public TwoStateAppliance(String uid, String type, long activateWattage, long inactiveWattage)
  {
    super(uid, type);
    this.activeWattage = activateWattage;
    this.inactiveWattage = inactiveWattage;
  }

  public boolean isActive()
  {
    return status == STATUS.ACTIVE;
  }

  public void setActivateStrategy(StochasticProcess activateStrategy)
  {
    this.activateStrategy = activateStrategy;
  }

  public void setDeactivateStrategy(StochasticProcess deactivateStrategy)
  {
    this.deactivateStrategy = deactivateStrategy;
  }


  @Override
  public void prepareForTimeslot(SimulationContext simulationContext)
  {
    //todo: ask the policy for permission to turn on
    switch (status)
    {
      case ACTIVE:
        checkForDeactivation(simulationContext);
        break;

      case INACTIVE:
        checkForActivation(simulationContext);
        break;

      case PENDING_ACTIVATION:
        break;
    }
  }

  /**
   * Instructs the appliance to update its state according to the simulation context and return the wattage consumed.
   * @param simulationContext - the simulation context, including the timeslot and other relevant information
   * @return the wattage consumed during the timeslot
   */
  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
    log.trace("Appliance {} consuming timeslot {}", this.getUid(), simulationContext.getTimeslot());

    return getCurrentWattageLoad(simulationContext);
  }

  /**
   * @param simulationContext - the simulation context, including the timeslot
   * @return The wattage consumed by the appliance according to the current state
   */
  private long getCurrentWattageLoad(SimulationContext simulationContext)
  {
    if (isActive())
    {
      log.trace("Appliance {} is ON consuming {}", this.getType(), activeWattage);
      return activeWattage;
    }
    else
    {
      log.trace("Appliance {} is OFF consuming {}", this.getType(), inactiveWattage);
      return inactiveWattage;
    }
  }

  private Calendar getNextActivationTime(Calendar from, long slotDuration)
  {
    return activateStrategy.getNextSimulatedEventTime(from, slotDuration);
  }

  private Calendar getNextDeactivationTime(Calendar from, long slotDuration)
  {
    return deactivateStrategy.getNextSimulatedEventTime(from, slotDuration);
  }

  /**
   * Turns the appliance ON randomly according to its TurnOnStrategy
   * @param simulationContext - the simulation context, including the timeslot
   */
  private void checkForActivation(SimulationContext simulationContext)
  {
    if (status == STATUS.INACTIVE)
    {
      //if we have no switching time, generate one
      if (nextSwitchTime == null)
      {
        nextSwitchTime = activateStrategy.getNextSimulatedEventTime(simulationContext.getTimeslot().getStartTime(), simulationContext.getTimeslot().getDuration());
        log.trace("Next switch ON time for {} is {}", this.getType(), sdf.get().format(nextSwitchTime.getTime()));
      }

      //check if the switching time is in the current timeslot
      if (isSwitchingTime(simulationContext))
      {
        nextSwitchTime = null; //we've used this switch, so turn it to 0 so that the next time a new one is computed if necessary
        log.trace("Appliance {}[{}] requesting activation", this.getType(), this.getUid());

        Calendar curTime = simulationContext.getTimeslot().getStartTime();
        Calendar deactivationTime = deactivateStrategy.getNextSimulatedEventTime(simulationContext.getTimeslot().getStartTime(), simulationContext.getTimeslot().getDuration());
        long duration = deactivationTime.getTimeInMillis() - curTime.getTimeInMillis();
        this.status = STATUS.PENDING_ACTIVATION;

        getHousehold().requestActivity(new ActivityRequest(getHousehold(), this, simulationContext, this.activeWattage, duration));
      }
    }
  }

  /**
   * Turns the appliance OFF randomly according to its TurnOnStrategy
   * @param simulationContext - the simulation context, including the timeslot
   */
  private void checkForDeactivation(SimulationContext simulationContext)
  {
    if (status == STATUS.ACTIVE)
    {
      if (nextSwitchTime == null)
      {
        nextSwitchTime = deactivateStrategy.getNextSimulatedEventTime(simulationContext.getTimeslot().getStartTime(), simulationContext.getTimeslot().getDuration());
        log.trace("Next OFF switch time for {} is {}", this.getType(), sdf.get().format(nextSwitchTime.getTime()));
      }

      if (isSwitchingTime(simulationContext))
      {
        log.trace("Turning appliance {}[{}] OFF", this.getType(), this.getUid());
        nextSwitchTime = null;
        this.status = STATUS.INACTIVE;

        getHousehold().notifyActivityTermination(this.currentActivity);
      }
    }
  }

  private boolean isSwitchingTime(SimulationContext simulationContext)
  {
    if (nextSwitchTime == null)
    {
      return false;
    }

    Timeslot timeslot = simulationContext.getTimeslot();
    if (timeslot.getEndTime().after(nextSwitchTime))
    {
      return true;
    }

    return false;
  }

  @Override
  public void activate(SimulationContext simulationContext, ActivityRequest activityRequest)
  {
    if (status == STATUS.PENDING_ACTIVATION)
    {
      Calendar calNextTime = Calendar.getInstance();
      calNextTime.setTimeInMillis(simulationContext.getTimeslot().getStartTime().getTimeInMillis() + activityRequest.getDuration());

      this.nextSwitchTime = calNextTime;
      log.trace("Activating {}[{}] till {}", new Object[]{this.getType(), this.getUid(), nextSwitchTime.toString()});

      this.currentActivity = activityRequest;
      status = STATUS.ACTIVE;
    }
  }

  @Override
  public void deactivate(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void suspend(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void resume(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

}
