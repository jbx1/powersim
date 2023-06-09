package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.configuration.HouseholdConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyHouseholdData;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.EnergyOnDemandHousehold;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.EnergyOnDemandPolicy;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.HouseholdFactoryConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 15:01
 */
public class EnergyOnDemandPolicyConfig extends PolicyConfig<EnergyOnDemandPolicy>
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandPolicyConfig.class);

  private List<PolicyHouseholdData> households = new ArrayList<>();

  @Override
  public EnergyOnDemandPolicy getPolicyInstance()
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

    return new EnergyOnDemandPolicy(mapHouseholdCount);
  }

  public List<PolicyHouseholdData> getHouseholds()
  {
    return households;
  }

  public void setHouseholds(List<PolicyHouseholdData> households)
  {
    this.households = households;
  }
}
