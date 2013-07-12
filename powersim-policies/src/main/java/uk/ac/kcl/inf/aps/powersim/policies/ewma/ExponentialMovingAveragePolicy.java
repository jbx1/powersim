package uk.ac.kcl.inf.aps.powersim.policies.ewma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.ActivityRequest;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.policies.AbstractThreeStageSchedulingPolicy;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Date: 09/07/13
 * Time: 14:03
 *
 * @author: Josef Bajada &lt; josef.bajada@kcl.ac.uk &gt;
 */
public class ExponentialMovingAveragePolicy extends AbstractThreeStageSchedulingPolicy
{
  protected static final Logger log = LoggerFactory.getLogger(ExponentialMovingAveragePolicy.class);

  private int averageDecayTimeslots = 240;
  private int deviationDecayTimeslots = 240;
  private int schedulingWattageThreshold = 300000;
  private boolean includeDeferred = true; //should the weighted moving average use just the interactive loads or also the deferred

  private double ewma = 0;  // the exponentially weighted moving average
  private double ewmad = 0; // the exponentially weigted moving average deviation

  public ExponentialMovingAveragePolicy(Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap)
  {
    super(householdFactoryConfigurationMap);
  }

  @Override
  protected void schedulingStage(SimulationContext context)
  {
    //energy is provided on demand, so all activity requests are served
    Queue<ActivityRequest> activityRequests = getActivityRequests();

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

    //get the current peak
    long curPeak = getCurrentPeakWattage();

    if (ewma > 0)
    {
      //service any waiting activities (if possible)
      curPeak = serviceWaitingActivities(context, curPeak);
      if (!includeDeferred)
        curPeak = getCurrentNonDeferredPeakWattage(); //count the activities that were not started later than requested

      //calculate alpha (we do it once each timeslot in case decayTimeslots was changed)
      double alpha = 2 / ((double) averageDecayTimeslots + 1);
      //Update the ewma to take into consideration the new peak
      this.ewma = (alpha * curPeak) + ((1 - alpha) * this.ewma);

      //if deviationDecayTimeslots is set (> 0), we need to update also the exponentially weighted moving average deviation
      if (deviationDecayTimeslots > 0)
      {
        //update the ewmad to take into consideration the new deviation and new ewma
        double deviation = curPeak - ewma;
        log.trace("New deviation: " + deviation);
        double alphaDev = 2 / ((double) deviationDecayTimeslots + 1);
        log.trace("Alpha {} Alpha-deviation {}", alpha, alphaDev);
        this.ewmad = (alphaDev * deviation) + ((1 - alphaDev) * this.ewmad);
      }
    }
    else
    {
      //we don't have a weighted moving average yet, just set it to the current peak and return
      this.ewma = curPeak;
    }

    log.trace("New EWMA {} EWMAD {}", ewma, ewmad);
  }

  /**
   * Activates any waiting appliances if there is any scheduling capacity and returns the new peak wattage.
   * @param context - the simulation context
   * @param curPeak - the peak wattage before scheduling
   * @return the new peak wattage after starting any waiting appliances
   */
  public long serviceWaitingActivities(SimulationContext context, long curPeak)
  {
    //calculate the limit based on the moving average and moving average deviation
    int limit = (int) (ewma - ewmad);
    //make sure it doesn't exceed the scheduling wattage treshold
    limit = limit < schedulingWattageThreshold ? limit : schedulingWattageThreshold;

    log.trace("EWMA {} - EWMAD {} = Limit {}", new Object[]{ewma, ewmad, limit});

    //we have a moving average, so if it is higher than the current peak start a schedulable appliance
    Queue<ActivityRequest> waitingList = getWaitingList();
    while (!waitingList.isEmpty())
    {
      if (limit - curPeak >= waitingList.peek().getWattage())
      {
        ActivityRequest activityRequest = waitingList.remove();
        log.trace("Limit {} - CurPeak {} = {} > activity {}, activating now", new Object[]{limit, curPeak, limit - curPeak, activityRequest});
        activateRequest(context, activityRequest);
        curPeak += activityRequest.getWattage();
      }
      else
      {
        break;
      }
    }

    return curPeak;
  }

  public int getAverageDecayTimeslots()
  {
    return averageDecayTimeslots;
  }

  public void setAverageDecayTimeslots(int averageDecayTimeslots)
  {
    this.averageDecayTimeslots = averageDecayTimeslots;
  }

  public int getDeviationDecayTimeslots()
  {
    return deviationDecayTimeslots;
  }

  public void setDeviationDecayTimeslots(int deviationDecayTimeslots)
  {
    this.deviationDecayTimeslots = deviationDecayTimeslots;
  }

  public int getSchedulingWattageThreshold()
  {
    return schedulingWattageThreshold;
  }

  public void setSchedulingWattageThreshold(int schedulingWattageThreshold)
  {
    this.schedulingWattageThreshold = schedulingWattageThreshold;
  }

  public boolean isIncludeDeferred()
  {
    return includeDeferred;
  }

  public void setIncludeDeferred(boolean includeDeferred)
  {
    this.includeDeferred = includeDeferred;
  }

  @Override
  public boolean ready(int timeout)
  {
    return true;
  }

  @Override
  public String getDescriptor()
  {
    return "exponential-moving-average";
  }
}
