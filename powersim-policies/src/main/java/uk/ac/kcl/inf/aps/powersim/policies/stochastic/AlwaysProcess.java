package uk.ac.kcl.inf.aps.powersim.policies.stochastic;

import java.util.Calendar;

/**
 * Stochastic process (which in reality is not stochastic) that returns an immediate event;
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 13/11/12
 *         Time: 11:44
 */
public class AlwaysProcess implements StochasticProcess
{
  public AlwaysProcess()
  {
  }

  @Override
  public Calendar getNextSimulatedEventTime(Calendar startTime, long slotDuration)
  {
    return startTime;
  }
}
