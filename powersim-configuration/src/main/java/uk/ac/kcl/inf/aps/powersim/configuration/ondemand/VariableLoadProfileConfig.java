package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.configuration.ProfileConfig;
import uk.ac.kcl.inf.aps.powersim.policies.ondemand.DayBoundedGLAppliance;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 23/11/12
 *         Time: 16:49
 */
public class VariableLoadProfileConfig extends ProfileConfig<DayBoundedGLAppliance>
{
  private int startHour;
  private int peakHour;
  private int stopHour;
  private double variance;
  private double error;

  public int getStartHour()
  {
    return startHour;
  }

  public void setStartHour(int startHour)
  {
    this.startHour = startHour;
  }

  public int getPeakHour()
  {
    return peakHour;
  }

  public void setPeakHour(int peakHour)
  {
    this.peakHour = peakHour;
  }

  public int getStopHour()
  {
    return stopHour;
  }

  public void setStopHour(int stopHour)
  {
    this.stopHour = stopHour;
  }

  public double getVariance()
  {
    return variance;
  }

  public void setVariance(double variance)
  {
    this.variance = variance;
  }

  public double getError()
  {
    return error;
  }

  public void setError(double error)
  {
    this.error = error;
  }

  @Override
  public void profileAppliance(DayBoundedGLAppliance appliance)
  {
    appliance.setLowerBoundHour(startHour);
    appliance.setHigherBoundHour(stopHour);
    appliance.setPeakTimeHour(peakHour);
    appliance.setPeakErrorVariance(error);
    appliance.setLoadVariance(variance);
  }
}
