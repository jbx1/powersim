package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 13/11/12
 *         Time: 11:09
 */
public class ApplianceSwitchOnFreq
{
  public static final Map<Integer, Double> KETTLE = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0.1d);
      put(5, 0.1d);
      put(7, 0.2d);
      put(9, 0.4d);
      put(12, 0.1d);
      put(16, 0.2d);
      put(17, 0.3d);
      put(20, 0.2d);
      put(23, 0.1d);
    }
  };

  public static final Map<Integer, Double> TOASTER = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0d);
      put(4, 0d);
      put(7, 0.2d);
      put(9, 0.1d);
      put(12, 0d);
      put(16, 0d);
      put(17, 0.3d);
      put(20, 0.3d);
      put(23, 0d);
    }
  };

  public static final Map<Integer, Double> WASHING_MACHINE = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0d);
      put(4, 0d);
      put(7, 0.1d);
      put(9, 0d);
      put(12, 0.1d);
      put(16, 0.1d);
      put(17, 0.1d);
      put(20, 0.1d);
      put(23, 0d);
    }
  };

  public static final Map<Integer, Double> DISHWASHER = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0.05d);
      put(2, 0d);
      put(16, 0d);
      put(17, 0.05d);
      put(20, 0.2d);
      put(22, 0.3d);
    }
  };

  public static final Map<Integer, Double> FRIDGE = new TreeMap<Integer, Double>()
  {
    {
      put(0, 2d);
      put(2, 1d);
      put(6, 1d);
      put(7, 1.5d);
      put(9, 1.5d);
      put(12, 1d);
      put(16, 1d);
      put(17, 3d);
      put(20, 4d);
      put(23, 3d);
    }
  };

  public static final Map<Integer, Double> AQUARIUM = new TreeMap<Integer, Double>()
  {
    {
      put(0, 2d);
      put(2, 3d);
      put(6, 2d);
      put(7, 2d);
      put(9, 1.5d);
      put(12, 1.5d);
      put(16, 1.5d);
      put(17, 2d);
      put(20, 2d);
      put(23, 2d);
    }
  };

  public static final Map<Integer, Double> OVEN = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0d);
      put(4, 0d);
      put(7, 0d);
      put(9, 0d);
      put(12, 0d);
      put(16, 0d);
      put(17, 0.2d);
      put(20, 0.2d);
      put(23, 0d);
    }
  };

  public static final Map<Integer, Double> TV = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0.05d);
      put(2, 0.01d);
      put(7, 1d);
      put(9, 0.1d);
      put(12, 0.1d);
      put(16, 0.3d);
      put(17, 0.3d);
      put(20, 0.4d);
      put(23, 0.1d);
    }
  };

  public static final Map<Integer, Double> LAPTOP = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0.1d);
      put(2, 0.05d);
      put(7, 0.1d);
      put(9, 0.2d);
      put(12, 0.2d);
      put(16, 0.3d);
      put(17, 0.3d);
      put(20, 0.3d);
      put(23, 0.2d);
    }
  };


  public static final Map<Integer, Double> LIGHT = new TreeMap<Integer, Double>()
  {
    {
      put(0, 0.1d);
      put(4, 0.3d);
      put(7, 0.4d);
      put(9, 0.4d);
      put(12, 0.1d);
      put(16, 0.3d);
      put(17, 1d);
      put(20, 1d);
      put(23, 1d);
    }
  };





}


