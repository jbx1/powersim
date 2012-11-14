package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import java.util.Map;

/**
 * Hardcoded ratings for various appliances.
 * It is assumed that the turn on rate is governed by a non-homogenous poisson process,
 * while the turn off is governed by the normal distribution.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 13/11/12
 *         Time: 12:02
 */
public enum ApplianceUsageRating
{
  LAPTOP (50, 0, 120, 20, ApplianceSwitchOnFreq.LAPTOP),
  FRIDGE (800, 50, 2, 0.5, ApplianceSwitchOnFreq.FRIDGE),
  WASHING_MACHINE (600, 0, 40, 2, ApplianceSwitchOnFreq.WASHING_MACHINE),
  AQUARIUM (500, 5, 5, 0.5, ApplianceSwitchOnFreq.AQUARIUM),
  KETTLE (1200, 0, 4, 0.3, ApplianceSwitchOnFreq.KETTLE),
  TV (110, 0, 240, 20, ApplianceSwitchOnFreq.TV),
  TOASTER (1000, 0, 5, 0.1, ApplianceSwitchOnFreq.TOASTER),
  OVEN (1200, 0, 45, 10, ApplianceSwitchOnFreq.OVEN),
  DISHWASHER (1500, 0, 120, 25, ApplianceSwitchOnFreq.DISHWASHER),
  LIGHT (10, 0, 60, 18, ApplianceSwitchOnFreq.LIGHT);

  private final long onWattage;
  private final long offWattage;
  private final long meanDurationMins;
  private final double stdDev;
  private Map<Integer, Double> mapSwitchOnFreq;

  private ApplianceUsageRating(long onWattage, long offWattage, int meanDurationMins, double stdDev, Map<Integer, Double> mapSwitchOnFreq)
  {
    this.onWattage = onWattage;
    this.offWattage = offWattage;
    this.meanDurationMins = meanDurationMins;
    this.stdDev = stdDev;
    this.mapSwitchOnFreq = mapSwitchOnFreq;
  }

  public long getOnWattage()
  {
    return onWattage;
  }

  public long getOffWattage()
  {
    return offWattage;
  }

  public long getMeanDurationMins()
  {
    return meanDurationMins;
  }

  public double getStdDev()
  {
    return stdDev;
  }

  public Map<Integer, Double> getMapSwitchOnFreq()
  {
    return mapSwitchOnFreq;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ApplianceUsageRating");
    sb.append("{").append(this.name());
    sb.append(", onWattage=").append(onWattage);
    sb.append(", offWattage=").append(offWattage);
    sb.append(", meanDurationMins=").append(meanDurationMins);
    sb.append(", stdDev=").append(stdDev);
    sb.append('}');
    return sb.toString();
  }
}
