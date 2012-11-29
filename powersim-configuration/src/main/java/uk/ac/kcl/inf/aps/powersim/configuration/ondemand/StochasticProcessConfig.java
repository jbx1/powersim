package uk.ac.kcl.inf.aps.powersim.configuration.ondemand;

import uk.ac.kcl.inf.aps.powersim.policies.stochastic.StochasticProcess;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 26/11/12
 *         Time: 16:15
 */
public interface StochasticProcessConfig<T extends StochasticProcess>
{
  public T getStochasticProcessInstance();
}
