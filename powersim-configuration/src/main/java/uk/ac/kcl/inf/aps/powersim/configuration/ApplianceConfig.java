package uk.ac.kcl.inf.aps.powersim.configuration;

import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.ApplianceFactory;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 14:13
 */
public abstract class ApplianceConfig<A extends Appliance> implements ApplianceFactory<A>
{
  String type;
  String subType;

  public String getType()
  {
    return type;
  }

  public String getSubType()
  {
    return subType;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public void setSubType(String subType)
  {
    this.subType = subType;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ApplianceConfig");
    sb.append("{type='").append(type).append('\'');
    sb.append(", subType='").append(subType).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
