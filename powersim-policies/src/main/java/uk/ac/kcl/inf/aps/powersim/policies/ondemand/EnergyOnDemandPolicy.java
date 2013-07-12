package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.policies.AbstractThreeStagePolicy;

import java.util.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 16:46
 */
public class EnergyOnDemandPolicy extends AbstractThreeStagePolicy
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandPolicy.class);

  public EnergyOnDemandPolicy(Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap)
  {
    super(householdFactoryConfigurationMap);
  }


  @Override
  public List<? extends Household> setup()
  {
    return super.setup();
  }

  protected void schedulingStage(SimulationContext context)
  {
    //energy is provided on demand, so all activity requests are served
    Queue<ActivityRequest> activityRequests = getActivityRequests();
    log.debug("Providing energy to all {} activity requests", activityRequests.size());

    while (!activityRequests.isEmpty())
    {
     ActivityRequest activityRequest = activityRequests.remove();
     activityRequest.getAppliance().activate(context, activityRequest);
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
    return "energy-on-demand";
  }


  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("EnergyOnDemandPolicy{");
    sb.append(super.toString());
    sb.append('}');
    return sb.toString();
  }

  @Override
  public void notifyActivityTermination(ActivityRequest activityRequest)
  {
    //todo: account for this termination
  }
}
