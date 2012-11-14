package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.api.SimulationTimeslotConsumer;
import uk.ac.kcl.inf.aps.powersim.api.Timeslot;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.NonHomogenousPoissonProcess;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.NormalDistProcess;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.StochasticProcess;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Implements a two-state appliance. This can be either an On/Off appliance, or a High/low consumption appliance.
 * Two wattage specifications are provided, one for when the appliance is in the ON state, and one for when the appliance
 * is in the OFF (Low) state. For a purely On/Off state, the offWattage is 0.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 17:06
 */
public class TwoStateAppliance extends Appliance implements SimulationTimeslotConsumer
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

  private boolean active = false;

  private Calendar nextSwitchTime = null;

  /**
   * The wattage consumed when the appliance is active.
   */
  private long onWattage;

  /**
   * The wattage consumed when the appliance is inactive
   */
  private long offWattage;


  private StochasticProcess activateStrategy;

  private StochasticProcess deactivateStrategy;

  public TwoStateAppliance(String uid, String type, long onWattage, long offWattage, StochasticProcess activateStrategy, StochasticProcess deactivateStrategy)
  {
    super(uid, type);
    this.onWattage = onWattage;
    this.offWattage = offWattage;
    this.activateStrategy = activateStrategy;
    this.deactivateStrategy = deactivateStrategy;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActivateStrategy(StochasticProcess activateStrategy)
  {
    this.activateStrategy = activateStrategy;
  }

  public void setDeactivateStrategy(StochasticProcess deactivateStrategy)
  {
    this.deactivateStrategy = deactivateStrategy;
  }


  /**
   * Instructs the appliance to update its state according to the simulation context and return the wattage consumed.
   * @param simulationContext - the simulation context, including the timeslot and other relevant information
   * @return the wattage consumed during the timeslot
   */
  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
    if (isActive())
    {
      turnOffRandomly(simulationContext);
    }
    else
    {
      turnOnRandomly(simulationContext);
    }

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
      log.trace("Appliance {} is ON consuming {}", this.getType(), onWattage);
      return onWattage;
    }
    else
    {
      log.trace("Appliance {} is OFF consuming {}", this.getType(), offWattage);
      return offWattage;
    }
  }


  /**
   * Turns the appliance ON randomly according to its TurnOnStrategy
   * @param simulationContext - the simulation context, including the timeslot
   */
  private void turnOnRandomly(SimulationContext simulationContext)
  {
    if (nextSwitchTime == null)
    {
      nextSwitchTime = activateStrategy.getNextSimulatedEventTime(simulationContext.getTimeslot().getStartTime(), simulationContext.getTimeslot().getDuration());
      log.trace("Next switch ON time for {} is {}", this.getType(), sdf.get().format(nextSwitchTime.getTime()));
    }

    if (isSwitchingTime(simulationContext))
    {
      nextSwitchTime = null; //we've used this switch, so turn it to 0 so that the next time a new one is computed
      log.trace("Turning appliance {}[{}] ON", this.getType(), this.getUid());
      this.active = true;
    }
  }

  /**
   * Turns the appliance OFF randomly according to its TurnOnStrategy
   * @param simulationContext - the simulation context, including the timeslot
   */
  private void turnOffRandomly(SimulationContext simulationContext)
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
      this.active = false;
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

  public static TwoStateAppliance getNHPoissonNormalDistInstance(String uuid, String name, ApplianceUsageRating usageRating)
  {
    TwoStateAppliance appliance = new TwoStateAppliance(uuid, name,
            usageRating.getOnWattage(), usageRating.getOffWattage(),
            new NonHomogenousPoissonProcess(usageRating.getMapSwitchOnFreq()),
            new NormalDistProcess(usageRating.getMeanDurationMins(), usageRating.getStdDev()));

    return appliance;
  }
}
