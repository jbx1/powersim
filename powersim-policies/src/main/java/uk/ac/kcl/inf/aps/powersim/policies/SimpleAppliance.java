package uk.ac.kcl.inf.aps.powersim.policies;

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

  public long getCurrentWattageLoad()
  {
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
    int timeslotStartHour = timeslot.getStartTime().get(Calendar.HOUR_OF_DAY);
    int timeslotEndHour = timeslot.getStartTime().get(Calendar.HOUR_OF_DAY);

    if (applianceType.getStartHour() == applianceType.getEndHour())
    { //the start time and end time are equal, so the appliance can turn on/off all day or night
      turnOnRandomly();
    }
    else if (applianceType.getStartHour() < applianceType.getEndHour())
    { //the start and end time don't span over midnight
      //todo: average timeslots
      if ((timeslotStartHour > applianceType.getStartHour()) &&
            (timeslotEndHour < applianceType.getEndHour()))
      {
        turnOnRandomly();
      }
    }
    else if (applianceType.getStartHour() > applianceType.getEndHour())
    { //the start and end time span over midnight

      if (((timeslotStartHour > applianceType.getStartHour()) || timeslotStartHour  < applianceType.getEndHour()) &&
          (timeslotEndHour > applianceType.getStartHour() || timeslotEndHour < applianceType.getEndHour()))
      {
        turnOnRandomly();
      }
    }
    else
    { //we're not at the right time for the appliance to turn on
      this.on = false;
    }

    return getCurrentWattageLoad();
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
      this.on = false;
    }
    else if (!isOn() && (rand < applianceType.getProbabilityPercOn()))
    {
      this.on = true;
    }

    return this.on;
  }
}
