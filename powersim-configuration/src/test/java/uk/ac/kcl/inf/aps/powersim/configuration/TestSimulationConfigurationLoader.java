package uk.ac.kcl.inf.aps.powersim.configuration;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 14:41
 */
public class TestSimulationConfigurationLoader
{
  private static final Logger log = LoggerFactory.getLogger(TestSimulationConfigurationLoader.class);

  private SimulationConfigurationLoader configurationLoader;

  @Before
  public void setup()
  {
    try
    {
      configurationLoader = new SimulationConfigurationLoader("default");
    }
    catch (FileNotFoundException ex)
    {
      log.warn("Error loading configuration", ex);
    }
  }

  @Test
  public void testApplianceConfig()
  {
    List<ApplianceConfig> configList = configurationLoader.loadApplianceConfigurations();
    for (ApplianceConfig config : configList)
    {
      log.info("ApplianceConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testHouseholdConfig()
  {
    List<HouseholdConfig> configList = configurationLoader.loadHouseholdConfigurations();
    for (HouseholdConfig config : configList)
    {
      log.info("HosueholdConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testPolicyConfig()
  {
    List<PolicyConfig> configList = configurationLoader.loadPolicyConfigurations();
    for (PolicyConfig config : configList)
    {
      log.info("PolicyConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testProfileConfig()
  {
    List<ProfileConfig> configList = configurationLoader.loadProfileConfigurations();
    for (ProfileConfig config : configList)
    {
      log.info("ProfileConfig instance {} ", config.getClass());
    }
  }

  @Test
  public void testSimulationConfig()
  {
    SimulationConfig simulationConfig = configurationLoader.loadSimulationConfiguration();
    log.info("SimulationConfig instance {} ", simulationConfig.getClass());
  }

}
