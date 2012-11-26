package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.HouseholdConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.EnergyOnDemandHousehold;

import java.util.List;
import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 17:04
 */
public class EnergyOnDemandHouseholdConfig extends HouseholdConfig<EnergyOnDemandHousehold>
{
  List<HouseholdAppliance> appliances;

  @Override
  public EnergyOnDemandHousehold getHouseholdInstance()
  {
    return new EnergyOnDemandHousehold(UUID.randomUUID().toString(), getCategory(), getPolicy());
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
  }
}
