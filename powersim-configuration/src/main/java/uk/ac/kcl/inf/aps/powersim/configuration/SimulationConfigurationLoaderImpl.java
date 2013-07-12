package uk.ac.kcl.inf.aps.powersim.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.Policy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 14:21
 */
public class SimulationConfigurationLoaderImpl implements SimulationConfigurationLoader
{
  public static final Logger log = LoggerFactory.getLogger(SimulationConfigurationLoaderImpl.class);

  public static final String ROOT_CONFIG_DIR = "./config";
  public static final String DEFAULT_SIMULATION_CONFIG = "default";

  public static final String APPLIANCE_CONFIG_REGEX = "appliance(-.+)?\\.cfg";
  public static final String HOUSEHOLD_CONFIG_REGEX = "household(-.+)?\\.cfg";
  public static final String PROFILE_CONFIG_REGEX = "profile(-.+)?\\.cfg";
  public static final String POLICY_CONFIG_REGEX = "policy(-.+)?\\.cfg";
  public static final String SIMULATION_CONFIG = "simulation.cfg";

  private final Map<String, HouseholdConfig<? extends Household>> householdConfigurations;
  private final Map<String, ApplianceConfig> applianceConfigurations;
  private final Map<String, ProfileConfig> profileConfigurations;
  private final Map<String, PolicyConfig> policyConfigurations;
  private final SimulationConfig simulationConfiguration;

  public final File configDirectory;

  public SimulationConfigurationLoaderImpl(String configName)
          throws FileNotFoundException
  {
    configDirectory = new File(ROOT_CONFIG_DIR, configName);
    if (!configDirectory.exists() || !configDirectory.isDirectory())
    {
      log.error("The configuration direction {} does not exist or is not a directory!", configDirectory.getAbsolutePath());
      throw new FileNotFoundException(configDirectory.getAbsolutePath() + " does not exist or is not a directory.");
    }

    this.simulationConfiguration = loadSimulationConfiguration();
    this.householdConfigurations = loadHouseholdConfigurations();
    this.applianceConfigurations = loadApplianceConfigurations();
    this.profileConfigurations = loadProfileConfigurations();
    this.policyConfigurations = loadPolicyConfigurations();

  }

  public SimulationConfigurationLoaderImpl()
          throws FileNotFoundException
  {
    this(DEFAULT_SIMULATION_CONFIG);
  }


  public List<Policy> getSimulationPolicies(SimulationConfig simulationConfig)
  {
    Map<String, PolicyConfig> policyConfigMap = getPolicyConfigurations();

    List<Policy> policies = new ArrayList<>();
    for (String policyName : simulationConfig.getPolicies())
    {
      PolicyConfig policyConfig = policyConfigMap.get(policyName);
      if (policyConfig == null)
      {
        log.warn("No Policy configuration found for {}", policyName);
        continue;
      }

      Policy policy = policyConfig.getPolicyInstance();
      policies.add(policy);
    }

    return policies;
  }

  private SimulationConfig loadSimulationConfiguration()
  {
    log.debug("Loading simulation configuration");

    SimulationConfig simulationConfig = null;
    File file = new File(configDirectory, SIMULATION_CONFIG);

    if (file.exists() && file.isFile())
    {
      log.debug("Parsing {}", file.getAbsolutePath());
      try (Reader reader = new BufferedReader(new FileReader(file)))
      {
        Yaml yaml = new Yaml();
        simulationConfig = yaml.loadAs(reader, SimulationConfig.class);
        log.info(simulationConfig.toString());
      }
      catch (IOException ex)
      {
        log.warn("I/O Error while loading configuration!", ex);
      }
    }

    return simulationConfig;
  }

  private Map<String, HouseholdConfig<? extends Household>> loadHouseholdConfigurations()
  {
    log.debug("Loading appliance configurations");
    Map<String, HouseholdConfig<? extends Household>> householdConfigMap = new TreeMap<>();

    File[] files = configDirectory.listFiles(new FileFilter()
    {
      @Override
      public boolean accept(File pathname)
      {
        return (pathname.getName().matches(HOUSEHOLD_CONFIG_REGEX));
      }
    });

    if (files != null)
    {
      log.debug("Found {} files", files.length);

      for (File file : files)
      {
        log.debug("Parsing {}", file.getAbsolutePath());
        try (Reader reader = new BufferedReader(new FileReader(file)))
        {
          Yaml yaml = new Yaml();
          Iterable<Object> configList = yaml.loadAll(reader);
          for (Object object : configList)
          {
            HouseholdConfig configBean = (HouseholdConfig) object;
            log.info(configBean.toString());
            configBean.setConfigurationLoader(this);
            householdConfigMap.put(configBean.getCategory(), configBean);
          }
        }
        catch (IOException ex)
        {
          log.warn("I/O Error while loading configuration!", ex);
        }
      }
    }
    else
    {
      log.debug("Files array is null!");
    }

    return householdConfigMap;

  }

  private Map<String, ApplianceConfig> loadApplianceConfigurations()
  {
    log.debug("Loading appliance configurations");
    Map<String, ApplianceConfig> applianceConfigMap = new TreeMap<>();

    File[] files = configDirectory.listFiles(new FileFilter()
    {
      @Override
      public boolean accept(File pathname)
      {
        return (pathname.getName().matches(APPLIANCE_CONFIG_REGEX));
      }
    });

    if (files != null)
    {
      log.debug("Found {} files", files.length);

      for (File file : files)
      {
        log.debug("Parsing {}", file.getAbsolutePath());
        try (Reader reader = new BufferedReader(new FileReader(file)))
        {
          Yaml yaml = new Yaml();
          Iterable<Object> configList = yaml.loadAll(reader);
          for (Object object : configList)
          {
            ApplianceConfig configBean = (ApplianceConfig) object;
            log.info(configBean.toString());
            applianceConfigMap.put(configBean.getType(), configBean);
          }
        }
        catch (IOException ex)
        {
          log.warn("I/O Error while loading configuration!", ex);
        }
      }
    }
    else
    {
      log.debug("Files array is null!");
    }

    return applianceConfigMap;
  }


  private Map<String, ProfileConfig> loadProfileConfigurations()
  {
    log.debug("Loading profile configurations");
    Map<String, ProfileConfig> profileConfigMap = new TreeMap<>();

    File[] files = configDirectory.listFiles(new FileFilter()
    {
      @Override
      public boolean accept(File pathname)
      {
        return (pathname.getName().matches(PROFILE_CONFIG_REGEX));
      }
    });

    if (files != null)
    {
      log.debug("Found {} files", files.length);

      for (File file : files)
      {
        log.debug("Parsing {}", file.getAbsolutePath());
        try (Reader reader = new BufferedReader(new FileReader(file)))
        {
          Yaml yaml = new Yaml();
          Iterable<Object> configList = yaml.loadAll(reader);
          for (Object object : configList)
          {
            ProfileConfig configBean = (ProfileConfig) object;
            log.info(configBean.toString());
            profileConfigMap.put(configBean.getName(), configBean);
          }
        }
        catch (IOException ex)
        {
          log.warn("I/O Error reading file {}", file.getAbsolutePath());
          log.warn("I/O Error while loading configuration!", ex);
        }
      }
    }
    else
    {
      log.debug("Files array is null!");
    }

    return profileConfigMap;
  }


  public Map<String, PolicyConfig> loadPolicyConfigurations()
  {
    log.debug("Loading policy configurations");
    Map<String, PolicyConfig> policyConfigMap = new TreeMap<>();

    File[] files = configDirectory.listFiles(new FileFilter()
    {
      @Override
      public boolean accept(File pathname)
      {
        return (pathname.getName().matches(POLICY_CONFIG_REGEX));
      }
    });

    if (files != null)
    {
      log.debug("Found {} files", files.length);

      for (File file : files)
      {
        log.debug("Parsing {}", file.getAbsolutePath());
        try (Reader reader = new BufferedReader(new FileReader(file)))
        {
          Yaml yaml = new Yaml();
          Iterable<Object> configList = yaml.loadAll(reader);
          for (Object object : configList)
          {
            PolicyConfig configBean = (PolicyConfig) object;
            log.info(configBean.toString());
            configBean.setConfigurationLoader(this);
            policyConfigMap.put(configBean.getName(), configBean);
          }
        }
        catch (IOException ex)
        {
          log.warn("I/O Error while loading configuration!", ex);
        }
      }
    }
    else
    {
      log.debug("Files array is null!");
    }

    return policyConfigMap;
  }

  public Map<String, HouseholdConfig<? extends Household>> getHouseholdConfigurations()
  {
    return householdConfigurations;
  }

  public Map<String, ApplianceConfig> getApplianceConfigurations()
  {
    return applianceConfigurations;
  }

  public Map<String, ProfileConfig> getProfileConfigurations()
  {
    return profileConfigurations;
  }

  public Map<String, PolicyConfig> getPolicyConfigurations()
  {
    return policyConfigurations;
  }

  public SimulationConfig getSimulationConfiguration()
  {
    return simulationConfiguration;
  }
}
