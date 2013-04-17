package uk.ac.kcl.inf.aps.powersim.policies.stochastic;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 12/11/12
 *         Time: 19:48
 */
public class NormalDistProcess implements StochasticProcess
{
  protected static final Logger log = LoggerFactory.getLogger(NormalDistProcess.class);

  private NormalDistribution normalDistribution;

  /**
   * @param meanMins - expected (mean) value (in minutes) where the appliance is expected to switch
   * @param stdDev - the standard deviation value
   */
  public NormalDistProcess(double meanMins, double stdDev)
  {
    this.normalDistribution = new NormalDistribution(meanMins, stdDev);
  }

  @Override
  public Calendar getNextSimulatedEventTime(Calendar startTime, long slotDuration)
  {
    double randP = ThreadLocalRandom.current().nextDouble(1);
    log.trace("Normal Dist Random: {}", randP);

    double timeOffsetMins = normalDistribution.inverseCumulativeProbability(randP);
    log.trace("Inv cumulative time: {}mins", timeOffsetMins);

    long nextTime = startTime.getTimeInMillis() + ((long) timeOffsetMins * 60000);// convert it to milliseconds

    Calendar calNextTime = Calendar.getInstance();
    calNextTime.setTimeInMillis(nextTime);

    return calNextTime;
  }
}
