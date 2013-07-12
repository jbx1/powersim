package uk.ac.kcl.inf.aps.powersim.configuration.ewma;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.configuration.HouseholdConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyHouseholdData;
import uk.ac.kcl.inf.aps.powersim.policies.ewma.ExponentialMovingAveragePolicy;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Date: 09/07/13
 * Time: 14:32
 *
 * @author: josef
 */
public class ExponentialMovingAveragePolicyConfig extends PolicyConfig<ExponentialMovingAveragePolicy>
{
  protected static final Logger log = LoggerFactory.getLogger(ExponentialMovingAveragePolicyConfig.class);

  private List<PolicyHouseholdData> households = new ArrayList<>();

  private int averageDecayTimeslots = 240;
  private int deviationDecayTimeslots = 240;
  private int schedulingWattageThreshold = 300000;
  private boolean includeDeferred = true;

  @Override
  public ExponentialMovingAveragePolicy getPolicyInstance()
  {
    log.info("Creating new EnergyOnDemandPolicy instance");
//return an EnergyOnDemandPolicy instance populated with our stuff
    Map<String, HouseholdFactoryConfiguration<? extends Household>> mapHouseholdCount = new TreeMap<>();

    log.debug("Loading the household configuration.");
    Map<String, HouseholdConfig<? extends Household>> householdConfigMap = getConfigurationLoader().getHouseholdConfigurations();

    log.debug("Preparing mapping for household categories.");
    for (PolicyHouseholdData policyHouseholdConfig : households)
    {
      HouseholdConfig<? extends Household> householdConfig = householdConfigMap.get(policyHouseholdConfig.getCategory());
      if (householdConfig == null)
      {
        log.warn("No Household configuration found for category {}", policyHouseholdConfig.getCategory());
      }

      HouseholdFactoryConfiguration<? extends Household> householdFactoryConfiguration = new HouseholdFactoryConfiguration<>(householdConfig, policyHouseholdConfig.getQuantity());
      mapHouseholdCount.put(policyHouseholdConfig.getCategory(), householdFactoryConfiguration);
    }

    ExponentialMovingAveragePolicy policy = new ExponentialMovingAveragePolicy(mapHouseholdCount);
    policy.setAverageDecayTimeslots(averageDecayTimeslots);
    policy.setDeviationDecayTimeslots(deviationDecayTimeslots);
    policy.setSchedulingWattageThreshold(schedulingWattageThreshold);
    policy.setIncludeDeferred(includeDeferred);

    return policy;
  }

  public List<PolicyHouseholdData> getHouseholds()
  {
    return households;
  }

  public void setHouseholds(List<PolicyHouseholdData> households)
  {
    this.households = households;
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
}
