package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.PolicyConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.EnergyOnDemandPolicy;

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
  private List<PolicyHouseholdConfig> households = new ArrayList<>();

  @Override
  public EnergyOnDemandPolicy getPolicyInstance()
  {
    //return an EnergyOnDemandPolicy instance populated with our stuff
    Map<String, Integer> mapHouseholdCount = new TreeMap<>();

    for (PolicyHouseholdConfig householdConfig : households)
    {
      mapHouseholdCount.put(householdConfig.getCategory(), householdConfig.getQuantity());
    }

    return new EnergyOnDemandPolicy(mapHouseholdCount);
  }

  public List<PolicyHouseholdConfig> getHouseholds()
  {
    return households;
  }

  public void setHouseholds(List<PolicyHouseholdConfig> households)
  {
    this.households = households;
  }

  public static class PolicyHouseholdConfig
  {
    String category;
    int quantity;

    public PolicyHouseholdConfig()
    {
    }

    public String getCategory()
    {
      return category;
    }

    public void setCategory(String category)
    {
      this.category = category;
    }

    public int getQuantity()
    {
      return quantity;
    }

    public void setQuantity(int quantity)
    {
      this.quantity = quantity;
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder();
      sb.append("PolicyHouseholdConfig");
      sb.append("{category='").append(category).append('\'');
      sb.append(", quantity=").append(quantity);
      sb.append('}');
      return sb.toString();
    }
  }
}
