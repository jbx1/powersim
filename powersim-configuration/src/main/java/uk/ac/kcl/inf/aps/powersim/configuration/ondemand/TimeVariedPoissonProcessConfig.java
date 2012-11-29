package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.TimeVariedPoissonProcess;

import java.util.Map;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 16:16
 */
public class TimeVariedPoissonProcessConfig implements StochasticProcessConfig<TimeVariedPoissonProcess>
{
  protected static final Logger log = LoggerFactory.getLogger(TimeVariedPoissonProcessConfig.class);

  private Map<Integer, Double> rates;

  public TimeVariedPoissonProcessConfig()
  {
  }

  public Map<Integer, Double> getRates()
  {
    return rates;
  }

  public void setRates(Map<Integer, Double> rates)
  {
    this.rates = rates;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("TimeVariedPoissonProcessConfig");
    sb.append("{rates=").append(rates);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public TimeVariedPoissonProcess getStochasticProcessInstance()
  {
    log.debug("Creating new TimeVariedPoissonProcess stochastic process with rates", rates);
    return new TimeVariedPoissonProcess(rates);
  }
}
