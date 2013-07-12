package uk.ac.kcl.inf.aps.powersim.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.*;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.*;

/**
 *
 * Date: 04/07/13
 * Time: 16:25
 * @author Josef Bajada &lt; josef.bajada@kcl.ac.uk &gt;
 */
public abstract class AbstractThreeStagePolicy implements Policy
{
  protected static final Logger log = LoggerFactory.getLogger(AbstractThreeStagePolicy.class);

  private List<Household> households;
  private int totalHouseholdCount;

  private final Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap;

  private Queue<ActivityRequest> activityRequests = new LinkedList<>();

  public AbstractThreeStagePolicy(Map<String, HouseholdFactoryConfiguration<? extends Household>> householdFactoryConfigurationMap)
  {
    this.householdFactoryConfigurationMap = householdFactoryConfigurationMap;

    Set<String> householdCategories = householdFactoryConfigurationMap.keySet();
    for (String householdcategory : householdCategories)
    {
      totalHouseholdCount += householdFactoryConfigurationMap.get(householdcategory).getHouseholdCount();
    }
    log.debug("Total households for {} {}", this.getDescriptor(), totalHouseholdCount);
  }

  @Override
  public List<? extends Household> setup()
  {
    households = new ArrayList<>(totalHouseholdCount);

    for (String householdCategory : householdFactoryConfigurationMap.keySet())
    {
      HouseholdFactoryConfiguration<? extends Household> householdConfiguration = householdFactoryConfigurationMap.get(householdCategory);
      int categoryHouseholdCount = householdConfiguration.getHouseholdCount();
      log.debug("Category {} requires {} households", householdCategory, categoryHouseholdCount);
      HouseholdFactory<? extends Household> householdFactory = householdConfiguration.getHouseholdFactory();

      for (int i = 0; i < categoryHouseholdCount; i++)
      {
        Household household = householdFactory.getHouseholdInstance();
        household.setPolicy(this);
        households.add(household);
        household.setup();
      }
    }

    log.debug("{} houses set up", households.size());
    return households;
  }

  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    //notify the households to prepare for the new timeslot
    preparationStage(context);

    //schedule any appliances to do their activities in this timeslot
    schedulingStage(context);

    //collect consumption events from appliances and households
    consumptionStage(context);
  }

  protected void preparationStage(SimulationContext context)
  {
    for (Household household : getHouseholds())
    {
      //   log.debug("Handling household {} ", household.getUid());
      household.prepareForTimeslot(context);
    }
  }

  protected abstract void schedulingStage(SimulationContext context);

  protected void consumptionStage(SimulationContext context)
  {
    for (Household household : households)
    {
      //   log.debug("Handling household {} ", household.getUid());
      household.consumeTimeSlot(context);
    }
  }

  @Override
  public void requestActivity(ActivityRequest activityRequest)
  {
    activityRequests.add(activityRequest);
  }

  protected Queue<ActivityRequest> getActivityRequests()
  {
    return activityRequests;
  }

  public List<Household> getHouseholds()
  {
    return households;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder("AbstractThreeStagePolicy{");
    sb.append("households=").append(households);
    sb.append(", totalHouseholdCount=").append(totalHouseholdCount);
    sb.append(", householdFactoryConfigurationMap=").append(householdFactoryConfigurationMap);
    sb.append(", activityRequests=").append(activityRequests);
    sb.append('}');
    return sb.toString();
  }
}
