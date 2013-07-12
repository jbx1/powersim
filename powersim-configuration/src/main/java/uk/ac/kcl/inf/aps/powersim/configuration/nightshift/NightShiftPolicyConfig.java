package uk.ac.kcl.inf.aps.powersim.configuration.nightshift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.configuration.HouseholdConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.PolicyHouseholdData;
import uk.ac.kcl.inf.aps.powersim.policies.nightshift.NightShiftPolicy;
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
public class NightShiftPolicyConfig extends PolicyConfig<NightShiftPolicy>
{
  protected static final Logger log = LoggerFactory.getLogger(NightShiftPolicyConfig.class);

  private List<PolicyHouseholdData> households = new ArrayList<>();

  private long nightAggregatePeakWattage = 400000;
  private long nightPeakWattageMaxStep = 10000;
  private int nightStartHour = 1;
  private int nightStopHour = 6;

  @Override
  public NightShiftPolicy getPolicyInstance()
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

    NightShiftPolicy policy = new NightShiftPolicy(mapHouseholdCount);
    policy.setNightAggregatePeakWattage(this.nightAggregatePeakWattage);
    policy.setNightStartHour(this.nightStartHour);
    policy.setNightStopHour(this.nightStopHour);
    policy.setNightPeakWattageMaxStep(this.nightPeakWattageMaxStep);

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

  public long getNightAggregatePeakWattage()
  {
    return nightAggregatePeakWattage;
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

  public void setNightAggregatePeakWattage(long nightAggregatePeakWattage)
  {
    this.nightAggregatePeakWattage = nightAggregatePeakWattage;
  }

  public long getNightPeakWattageMaxStep()
  {
    return nightPeakWattageMaxStep;
  }

  public void setNightPeakWattageMaxStep(long nightPeakWattageMaxStep)
  {
    this.nightPeakWattageMaxStep = nightPeakWattageMaxStep;
  }
}
