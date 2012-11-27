package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.HouseholdFactory;
import uk.ac.kcl.inf.aps.powersim.api.Policy;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;

import java.util.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 16:46
 */
public class EnergyOnDemandPolicy implements Policy
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandPolicy.class);

  private List<EnergyOnDemandHousehold> households;

  private int totalHouseholdCount;

  private final Map<String, Integer> householdCategoryCounts;

  private final Map<String, HouseholdFactory<EnergyOnDemandHousehold>> householdFactories = new TreeMap<>();

  public EnergyOnDemandPolicy(Map<String, Integer> householdCategoryCounts)
  {
    this.householdCategoryCounts = householdCategoryCounts;

    Set<String> householdCategories = householdCategoryCounts.keySet();
    for (String householdcategory : householdCategories)
    {
      totalHouseholdCount += householdCategoryCounts.get(householdcategory);
    }
  }

  public void setHouseholdFactoryForCategory(String category, HouseholdFactory<EnergyOnDemandHousehold> householdFactory)
  {
    householdFactories.put(category, householdFactory);
  }

  public int getTotalHouseholdCount()
  {
    return totalHouseholdCount;
  }

  public int getHouseholdCount(String category)
  {
    return householdCategoryCounts.get(category);
  }

  @Override
  public List<? extends Household> setup()
  {
    households = new ArrayList<>(totalHouseholdCount);

    for (String householdCategory : householdCategoryCounts.keySet())
    {
      int categoryHouseholdCount = householdCategoryCounts.get(householdCategory);
      HouseholdFactory<EnergyOnDemandHousehold> householdFactory = householdFactories.get(householdCategory);

      for (int i = 0; i < categoryHouseholdCount; i++)
      {
        EnergyOnDemandHousehold household = householdFactory.getHouseholdInstance();
        household.setPolicy(this);
        households.add(household);
      }
    }

    return households;
  }

  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    //todo: handle multithreading
    for (EnergyOnDemandHousehold household : households)
    {
      //   log.debug("Handling household {} ", household.getUid());
      household.consumeTimeSlot(context);
    }
  }

  @Override
  public boolean ready(int timeout)
  {
    //todo: handle multithreading
    return true;
  }

  @Override
  public String getDescriptor()
  {
    return "energy-on-demand-policy";
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("EnergyOnDemandPolicy");
    sb.append("{descriptor=").append(getDescriptor());
    sb.append(", totalHouseholdCount=").append(totalHouseholdCount);
    sb.append(", householdCategories=").append(householdCategoryCounts.size());
    sb.append('}');
    return sb.toString();
  }

}
