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

  private final Map<String, HouseholdFactoryConfiguration<EnergyOnDemandHousehold>> householdFactoryConfigurationMap;

  public EnergyOnDemandPolicy(Map<String, HouseholdFactoryConfiguration<EnergyOnDemandHousehold>> householdFactoryConfigurationMap)
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
      HouseholdFactoryConfiguration<EnergyOnDemandHousehold> householdConfiguration = householdFactoryConfigurationMap.get(householdCategory);
      int categoryHouseholdCount = householdConfiguration.getHouseholdCount();
      log.debug("Category {} requires {} households", householdCategory, categoryHouseholdCount);
      HouseholdFactory<EnergyOnDemandHousehold> householdFactory = householdConfiguration.getHouseholdFactory();

      for (int i = 0; i < categoryHouseholdCount; i++)
      {
        EnergyOnDemandHousehold household = householdFactory.getHouseholdInstance();
        household.setPolicy(this);
        households.add(household);
        household.setupAppliances();
      }
    }

    log.debug("{} houses set up", households.size());
    return households;
  }

  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    for (EnergyOnDemandHousehold household : households)
    {
      //   log.debug("Handling household {} ", household.getUid());
      household.consumeTimeSlot(context);
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
    final StringBuilder sb = new StringBuilder();
    sb.append("EnergyOnDemandPolicy");
    sb.append("{descriptor=").append(getDescriptor());
    sb.append(", totalHouseholdCount=").append(totalHouseholdCount);
    sb.append(", householdCategories=").append(householdFactoryConfigurationMap.size());
    sb.append('}');
    return sb.toString();
  }

}
