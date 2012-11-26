package uk.ac.kcl.inf.aps.powersim.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 14:21
 */
public class SimulationConfigurationLoader
{
  public static final Logger log = LoggerFactory.getLogger(SimulationConfigurationLoader.class);

  public static final String ROOT_CONFIG_DIR = "./config";

  public static final String APPLIANCE_CONFIG_REGEX = "appliance(-.+)?\\.cfg";
  public static final String HOUSEHOLD_CONFIG_REGEX = "household(-.+)?\\.cfg";
  public static final String PROFILE_CONFIG_REGEX = "profile(-.+)?\\.cfg";
  public static final String POLICY_CONFIG_REGEX = "policy(-.+)?\\.cfg";
  public static final String SIMULATION_CONFIG = "simulation.cfg";


  public final File configDirectory;

  public SimulationConfigurationLoader(String configName)
          throws FileNotFoundException
  {
    configDirectory = new File(ROOT_CONFIG_DIR, configName);
    if (!configDirectory.exists() || !configDirectory.isDirectory())
    {
      log.error("The configuration direction {} does not exist or is not a directory!", configDirectory.getAbsolutePath());
      throw new FileNotFoundException(configDirectory.getAbsolutePath() + " does not exist or is not a directory.");
    }
  }

  public List<HouseholdConfig> loadHouseholdConfigurations()
  {
    log.debug("Loading appliance configurations");
    List<HouseholdConfig> householdConfigList = new ArrayList<>();

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
            householdConfigList.add(configBean);
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

    return householdConfigList;

  }

  public List<ApplianceConfig> loadApplianceConfigurations()
  {
    log.debug("Loading appliance configurations");
    List<ApplianceConfig> applianceConfigList = new ArrayList<>();

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
            applianceConfigList.add(configBean);
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

    return applianceConfigList;
  }


  public List<ProfileConfig> loadProfileConfigurations()
  {
    log.debug("Loading profile configurations");
    List<ProfileConfig> profileConfigList = new ArrayList<>();

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
            profileConfigList.add(configBean);
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

    return profileConfigList;
  }

  public SimulationConfig loadSimulationConfiguration()
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

  public List<PolicyConfig> loadPolicyConfigurations()
  {
    log.debug("Loading policy configurations");
    List<PolicyConfig> policyConfigList = new ArrayList<>();

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
            policyConfigList.add(configBean);
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

    return policyConfigList;
  }

}
