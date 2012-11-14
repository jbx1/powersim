package uk.ac.kcl.inf.aps.powersim.policies.stochastic;

import java.util.Calendar;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 12/11/12
 *         Time: 14:50
 */
public interface StochasticProcess
{
  Calendar getNextSimulatedEventTime(Calendar startTime, long slotDuration);
}
