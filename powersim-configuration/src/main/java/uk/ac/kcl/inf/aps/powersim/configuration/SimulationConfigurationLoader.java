package uk.ac.kcl.inf.aps.powersim.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

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

//todo: finish this
  /*
  public Simulation configureSimulation()
          throws SimulationConfigurationException
  {
    Map<ApplianceTuple, ApplianceConfig> applianceConfigMap = loadApplianceConfigurations();
    Map<String, ProfileConfig> profileConfigMap = loadProfileConfigurations();
    Map<String, HouseholdConfig> householdConfigMap = loadHouseholdConfigurations();
    Map<String, PolicyConfig> policyConfigMap = loadPolicyConfigurations();
    SimulationConfig simulationConfig = loadSimulationConfiguration();


    List<Policy> policies = new ArrayList<>();

    for (String policyName : simulationConfig.getPolicies())
    {
      PolicyConfig policyConfig = policyConfigMap.get(policyName);

      //todo: set the household factories to the policy
      //todo: for each household factory, set the appliance factory

    }



  } */



  public Map<String, HouseholdConfig> loadHouseholdConfigurations()
  {
    log.debug("Loading appliance configurations");
    Map<String, HouseholdConfig> householdConfigMap = new TreeMap<>();

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

  public Map<ApplianceTuple, ApplianceConfig> loadApplianceConfigurations()
  {
    log.debug("Loading appliance configurations");
    Map<ApplianceTuple, ApplianceConfig> applianceConfigMap = new TreeMap<>();

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
            applianceConfigMap.put(new ApplianceTuple(configBean.getType(), configBean.getSubType()), configBean);
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


  public Map<String, ProfileConfig> loadProfileConfigurations()
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

  private static class ApplianceTuple implements Comparable<ApplianceTuple>
  {
    private final String type;
    private final String subtype;

    private ApplianceTuple(String type, String subtype)
    {
      this.type = type;
      this.subtype = subtype;
    }

    public String getType()
    {
      return type;
    }

    public String getSubtype()
    {
      return subtype;
    }

    @Override
    public boolean equals(Object o)
    {
      if (this == o)
      {
        return true;
      }
      if (o == null || getClass() != o.getClass())
      {
        return false;
      }

      ApplianceTuple that = (ApplianceTuple) o;

      if (subtype != null ? !subtype.equals(that.subtype) : that.subtype != null)
      {
        return false;
      }
      if (type != null ? !type.equals(that.type) : that.type != null)
      {
        return false;
      }

      return true;
    }

    @Override
    public int compareTo(ApplianceTuple o)
    {
      if (!o.getType().equals(this.getType()))
      {
        return this.getType().compareTo(o.getType());
      }
      else
      {
        return this.getSubtype().compareTo(o.getSubtype());
      }
    }
  }


}
