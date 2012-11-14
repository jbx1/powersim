package uk.ac.kcl.inf.aps.powersim.policies.stochastic;

import java.util.Calendar;

/**
 * A stochastic process (which in reality is not a stochastic process) that never fires an event.
 * Used in cases where an event is never expected to happen.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 13/11/12
 *         Time: 11:46
 */
public class NeverProcess implements StochasticProcess
{
  @Override
  public Calendar getNextSimulatedEventTime(Calendar startTime, long slotDuration)
  {
    return null;
  }
}
