package uk.ac.kcl.inf.aps.powersim.configuration;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 14:41
 */
public class TestSimulationConfigurationLoader
{
  private static final Logger log = LoggerFactory.getLogger(TestSimulationConfigurationLoader.class);

  private SimulationConfigurationLoaderImpl configurationLoader;

  @Before
  public void setup()
  {
    try
    {
      configurationLoader = new SimulationConfigurationLoaderImpl("default");
    }
    catch (FileNotFoundException ex)
    {
      log.warn("Error loading configuration", ex);
    }
  }

  @Test
  public void testApplianceConfig()
  {
    Map<String, ApplianceConfig> configList = configurationLoader.getApplianceConfigurations();
    for (ApplianceConfig config : configList.values())
    {
      log.info("ApplianceConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testHouseholdConfig()
  {
    Map<String, HouseholdConfig> configList = configurationLoader.getHouseholdConfigurations();
    for (HouseholdConfig config : configList.values())
    {
      log.info("HosueholdConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testPolicyConfig()
  {
    Map<String, PolicyConfig> configList = configurationLoader.getPolicyConfigurations();
    for (PolicyConfig config : configList.values())
    {
      log.info("PolicyConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testProfileConfig()
  {
    Map<String, ProfileConfig> configList = configurationLoader.getProfileConfigurations();
    for (ProfileConfig config : configList.values())
    {
      log.info("ProfileConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testSimulationConfig()
  {
    SimulationConfig simulationConfig = configurationLoader.getSimulationConfiguration();
    log.info("SimulationConfig instance {} ", simulationConfig.getClass());
  }

}
