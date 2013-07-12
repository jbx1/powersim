package uk.ac.kcl.inf.aps.powersim.policies.nightshift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.policies.AbstractThreeStageSchedulingPolicy;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.*;

/**
 * Date: 05/07/13
 * Time: 11:26
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 */
public class NightShiftPolicy extends AbstractThreeStageSchedulingPolicy
{
  protected static final Logger log = LoggerFactory.getLogger(NightShiftPolicy.class);

  private long nightAggregatePeakWattage = 400000;
  private long nightPeakWattageMaxStep = 10000;
  private int nightStartHour = 1;
  private int nightStopHour = 6;


  public NightShiftPolicy(Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap)
  {
    super(householdFactoryConfigurationMap);
  }

  @Override
  public List<? extends Household> setup()
  {
    return super.setup();
  }

  @Override
  protected void schedulingStage(SimulationContext context)
  {
    //energy is provided on demand, so all activity requests are served
    Queue<ActivityRequest> activityRequests = getActivityRequests();
    log.debug("Providing energy to all {} activity requests", activityRequests.size());

    while (!activityRequests.isEmpty())
    {
      ActivityRequest activityRequest = activityRequests.remove();

      switch (activityRequest.getAppliance().getType())
      {
        //todo: remove these from being hardcoded and put an attribute on their schedulability, or use deadlines
        case "washing-machine":
        case "clothes-dryer":
        case "dish-washer":
          postponeRequest(context, activityRequest);
          break;

        default:
          activateRequest(context, activityRequest);
          break;
      }
    }

    //check if it is night time, and if it is start activating requests in the waiting list
    Calendar curTime = context.getTimeslot().getStartTime();
    int curHour = curTime.get(Calendar.HOUR_OF_DAY);
    if ((curHour >= getNightStartHour()) && (curHour < getNightStopHour()))
    {
      long curPeak = getCurrentPeakWattage();
      long curStepPeak = 0;

      Queue<ActivityRequest> waitingList = getWaitingList();
      while (!waitingList.isEmpty())
      {
        if ((getNightAggregatePeakWattage() - curPeak >= waitingList.peek().getWattage()) &&
              getNightPeakWattageMaxStep() - curStepPeak >= waitingList.peek().getWattage())
        {
          ActivityRequest activityRequest = waitingList.remove();
          activateRequest(context, activityRequest);

          curPeak += activityRequest.getWattage();
          curStepPeak += activityRequest.getWattage();
        }
        else
        {
          break;
        }
      }
    }
  }


  @Override
  public boolean ready(int timeout)
  {
    return true;
  }

  @Override
  public String getDescriptor()
  {
    return "night-shift";
  }

  public long getNightAggregatePeakWattage()
  {
    return nightAggregatePeakWattage;
  }

  public void setNightAggregatePeakWattage(long nightAggregatePeakWattage)
  {
    this.nightAggregatePeakWattage = nightAggregatePeakWattage;
  }

  public int getNightStartHour()
  {
    return nightStartHour;
  }

  public void setNightStartHour(int nightStartHour)
  {
    this.nightStartHour = nightStartHour;
  }

  public int getNightStopHour()
  {
    return nightStopHour;
  }

  public void setNightStopHour(int nightStopHour)
  {
    this.nightStopHour = nightStopHour;
  }

  public long getNightPeakWattageMaxStep()
  {
    return nightPeakWattageMaxStep;
  }

  public void setNightPeakWattageMaxStep(long nightPeakWattageMaxStep)
  {
    this.nightPeakWattageMaxStep = nightPeakWattageMaxStep;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("NightShiftPolicy{");
    sb.append(super.toString());
    sb.append('}');
    return sb.toString();
  }


}
