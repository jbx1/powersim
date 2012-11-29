package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.ApplianceKeyTuple;
import uk.ac.kcl.inf.aps.powersim.configuration.ApplianceConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.HouseholdConfig;
import uk.ac.kcl.inf.aps.powersim.configuration.ProfileConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.EnergyOnDemandHousehold;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 17:04
 */
public class EnergyOnDemandHouseholdConfig extends HouseholdConfig<EnergyOnDemandHousehold>
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandHouseholdConfig.class);
  private List<HouseholdAppliance> appliances;

  @Override
  public EnergyOnDemandHousehold getHouseholdInstance()
  {
    EnergyOnDemandHousehold household = new EnergyOnDemandHousehold(UUID.randomUUID().toString(), getCategory());

    Map<ApplianceKeyTuple, ApplianceConfig> applianceKeyTupleMap = getConfigurationLoader().getApplianceConfigurations();
    Map<String, ProfileConfig> profileConfigMap = getConfigurationLoader().getProfileConfigurations();

    for (HouseholdAppliance appliance: appliances)
    {
      ApplianceKeyTuple applianceKeyTuple = appliance.getApplianceKeyTuple();
      ApplianceConfig applianceConfig = applianceKeyTupleMap.get(applianceKeyTuple);
      if (applianceConfig == null)
      {
        log.warn("No Appliance Configuration found matching {}", applianceKeyTuple);
        continue;
      }

      ProfileConfig profileConfig = profileConfigMap.get(appliance.getProfile());
      if (profileConfig == null)
      {
        log.warn("No Profile Configuration found matching {}", appliance.getProfile());
      }
      log.debug("Registering {} with household {}", applianceKeyTuple, household.getUid());

      household.addApplianceFactoryConfiguration(applianceKeyTuple, applianceConfig, profileConfig, appliance.getQuantity());
    }

    return household;
  }

  public List<HouseholdAppliance> getAppliances()
  {
    return appliances;
  }

  public void setAppliances(List<HouseholdAppliance> appliances)
  {
    this.appliances = appliances;
  }

  public static class HouseholdAppliance
  {
    String type;
    String subType;
    String profile;
    int quantity;

    public String getType()
    {
      return type;
    }

    public void setType(String type)
    {
      this.type = type;
    }

    public String getSubType()
    {
      return subType;
    }

    public void setSubType(String subType)
    {
      this.subType = subType;
    }

    public String getProfile()
    {
      return profile;
    }

    public void setProfile(String profile)
    {
      this.profile = profile;
    }

    public int getQuantity()
    {
      return quantity;
    }

    public void setQuantity(int quantity)
    {
      this.quantity = quantity;
    }

    public ApplianceKeyTuple getApplianceKeyTuple()
    {
      return new ApplianceKeyTuple(type, subType);
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder();
      sb.append("HouseholdAppliance");
      sb.append("{type='").append(type).append('\'');
      sb.append(", subType='").append(subType).append('\'');
      sb.append(", profile='").append(profile).append('\'');
      sb.append(", quantity=").append(quantity);
      sb.append('}');
      return sb.toString();
    }
  }
}
