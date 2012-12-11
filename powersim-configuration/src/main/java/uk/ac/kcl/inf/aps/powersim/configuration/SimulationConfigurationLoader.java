package uk.ac.kcl.inf.aps.powersim.configuration;

import uk.ac.kcl.inf.aps.powersim.api.Policy;

import java.util.List;
import java.util.Map;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 29/11/12
 *         Time: 11:54
 */
public interface SimulationConfigurationLoader
{
  public SimulationConfig getSimulationConfiguration();

  public List<Policy> getSimulationPolicies(SimulationConfig simulationConfig);

  public Map<String, PolicyConfig> getPolicyConfigurations();

  public Map<String, HouseholdConfig> getHouseholdConfigurations();

  public Map<String, ApplianceConfig> getApplianceConfigurations();

  public Map<String, ProfileConfig> getProfileConfigurations();
}
