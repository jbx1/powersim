package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 16:18
 */
public class NormalDistributionProcessConfig extends StochasticProcessConfig
{
  private double mean;
  private double stdDev;

  public NormalDistributionProcessConfig()
  {
  }

  public double getMean()
  {
    return mean;
  }

  public void setMean(double mean)
  {
    this.mean = mean;
  }

  public double getStdDev()
  {
    return stdDev;
  }

  public void setStdDev(double stdDev)
  {
    this.stdDev = stdDev;
  }
}
