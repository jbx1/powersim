package uk.ac.kcl.inf.aps.powersim.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.api.Timeslot;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 19/10/12
 *         Time: 16:01
 */
public class SimpleAppliance extends Appliance
{
  protected static final Logger log = LoggerFactory.getLogger(SimpleAppliance.class);

  private SimpleApplianceType applianceType;
  private boolean on = false;

  public SimpleAppliance(String uid, SimpleApplianceType applianceType)
  {
    super(uid, applianceType.toString());
    this.applianceType = applianceType;
    this.on = applianceType.isAlwaysOn();
  }

  public boolean isOn()
  {
    return on;
  }

  public long getCurrentWattageLoad(SimulationContext simulationContext)
  {
    //todo: calcualate wattage more intelligently rather than a flat wattage

    if (isOn())
      return applianceType.getWattage();
    else
      return 0;
  }

  /**
   * The device will turn on or off depending on the timeslot and the probability of it going on or off, according to type.
   * @param simulationContext - the simulation context (containing the timeslot and rest of the information)
   * @return the current wattage load consumed by the appliance
   */
  public long handleTimeslot(SimulationContext simulationContext)
  {
    if (applianceType.isAlwaysOn())
    {
      this.on = true; //redundant, but we do it for defensive programming
    }

    Timeslot timeslot = simulationContext.getTimeslot();
    if (isWithinOperatingTime(timeslot))
    {
      log.trace("Appliance {} within operating timeframe", applianceType);
      turnOnRandomly();
    }
    else
    {
      log.trace("Turning appliance {} off since time is out of its operating timeframe", applianceType.getDescription());
      this.on = false;
    }

    return getCurrentWattageLoad(simulationContext);
  }

  private boolean isWithinOperatingTime(Timeslot timeslot)
  {
    int timeslotStartHour = timeslot.getStartTime().get(Calendar.HOUR_OF_DAY);
    int timeslotEndHour = timeslot.getStartTime().get(Calendar.HOUR_OF_DAY);

    log.trace("Timeslot hour: {} Appliance {}", timeslotStartHour, applianceType.getDescription());

    if (applianceType.getStartHour() == applianceType.getEndHour())
    {//if the appliance start hour and end hour are the same, than the appliance can turn on/off any time during the day
      return true;
    }
    else if (applianceType.getStartHour() < applianceType.getEndHour())
    { //if the appliance start hour is smaller than the end hour, than we are not spanning over midnight
      //check that the timeslot start is greater or equal to the appliance start hour
      //and that the timeslot end is smaller or equal to the appliance end hour
      if ((timeslotStartHour >= applianceType.getStartHour()) &&
              (timeslotEndHour <= applianceType.getEndHour()))
      {
        return true;
      }
    }
    else if (applianceType.getStartHour() > applianceType.getEndHour())
    { //if the start hour is greater than the end hour, than the appliance operating time spans over midnight

      //check that the timeslot start hour is greater than or equal than the appliance start hour OR smaller than the end hour
      //and check that the timeslot end hour is also greater than the appliance start hour OR smaller than the end hour
      if (((timeslotStartHour >= applianceType.getStartHour()) || timeslotStartHour  <= applianceType.getEndHour()) &&
              (timeslotEndHour >= applianceType.getStartHour() || timeslotEndHour <= applianceType.getEndHour()))
      {
        return true;
      }
    }

    //if none of the above criteria were found to be true, appliance should be off
    return false;
  }

  /**
   * Based on a random generated value and the probability of the appliance turning on/off according to its current
   * status and applianceType, it will turn the appliance on or off.
   * @return true if the appliance turned/remained on, false if the appliance turned/remained off
   */
  private boolean turnOnRandomly()
  {
    //we use a ThreadLocalRandom for better performance in a multi-threaded environment
    //this will generate a number from 0 to 99, since the lowerbound is inclusive and the upperbound is exclusive
    int rand = ThreadLocalRandom.current().nextInt(0, 100);
    if (isOn() && (rand >= applianceType.getProbabilityPercOn()))
    {
      log.trace("Turning appliance {} off", applianceType);
      this.on = false;
    }
    else if (!isOn() && (rand < applianceType.getProbabilityPercOn()))
    {
      log.trace("Turning appliance {} on", applianceType);
      this.on = true;
    }

    return this.on;
  }
}
