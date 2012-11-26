package uk.ac.kcl.inf.aps.powersim.configuration;

import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.ApplianceProfiler;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:47
 */
public abstract class ProfileConfig<A extends Appliance> implements ApplianceProfiler<A>
{
  private String name;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }


  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ProfileConfig");
    sb.append("{name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
