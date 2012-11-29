package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.NormalDistProcess;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 16:18
 */
public class NormalDistributionProcessConfig implements StochasticProcessConfig<NormalDistProcess>
{
  protected static final Logger log = LoggerFactory.getLogger(NormalDistributionProcessConfig.class);

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

  @Override
  public NormalDistProcess getStochasticProcessInstance()
  {
    log.debug("Creating new NormalDistribution stochastic process with mean {} and stdDev {}", mean, stdDev);
    return new NormalDistProcess(mean, stdDev);
  }
}
