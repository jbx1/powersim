package uk.ac.kcl.inf.aps.powersim.policies;

import uk.ac.kcl.inf.aps.powersim.api.ActivityRequest;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.*;

/**
 * Date: 09/07/13
 * Time: 14:08
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 */
public abstract class AbstractThreeStageSchedulingPolicy extends AbstractThreeStagePolicy
{
  private TreeMap<ActivityRequest, Boolean> currentActivities = new TreeMap<>();
  private Queue<ActivityRequest> waitingList = new LinkedList<>();

  public AbstractThreeStageSchedulingPolicy(Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap)
  {
    super(householdFactoryConfigurationMap);
  }

  protected void activateRequest(SimulationContext simulationContext, ActivityRequest activityRequest)
  {
    log.trace("Type {} activating now", activityRequest.getAppliance().getType());
    activityRequest.getAppliance().activate(simulationContext, activityRequest);

    //if the simulation context of when the activity request was performed is not the same as the one passed
    //it means that the activity was deferred to a later stage
    boolean deferred = !activityRequest.getSimulationContext().equals(simulationContext);
    currentActivities.put(activityRequest, deferred);
  }

  protected void postponeRequest(SimulationContext simulationContext, ActivityRequest activityRequest)
  {
    log.trace("Type {} postponing to a better time", activityRequest.getAppliance().getType());
    waitingList.add(activityRequest);
  }

  protected long getCurrentPeakWattage()
  {
    long peak = 0;
    for (ActivityRequest currentActivity : currentActivities.keySet())
    {
      peak += currentActivity.getWattage();
    }
    return peak;
  }

  protected long getCurrentNonDeferredPeakWattage()
  {
    long peak = 0;

    for (ActivityRequest currentActivity : currentActivities.keySet())
    {
      if (!currentActivities.get(currentActivity)) //check that it was not a deferred activity
      {
        peak += currentActivity.getWattage();
      }
    }
    return peak;

  }

  @Override
  public void notifyActivityTermination(ActivityRequest activityRequest)
  {
    currentActivities.remove(activityRequest);
  }

  protected Queue<ActivityRequest> getWaitingList()
  {
    return waitingList;
  }

  public TreeMap<ActivityRequest, Boolean> getCurrentActivities()
  {
    return currentActivities;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("AbstractThreeStageSchedulingPolicy{");
    sb.append("{").append(super.toString()).append("}");
    sb.append("currentActivities=").append(currentActivities);
    sb.append(", waitingList=").append(waitingList);
    sb.append('}');
    return sb.toString();
  }
}
