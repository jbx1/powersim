package uk.ac.kcl.inf.aps.powersim.policies;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.NonHomogenousPoissonProcess;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.StochasticProcess;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 12/11/12
 *         Time: 16:09
 */

public class NonHomogenousPoissonProcessTest
{
  protected static final Logger log = LoggerFactory.getLogger(NonHomogenousPoissonProcessTest.class);
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");

  StochasticProcess stochasticProcess;

  @Before
  public void setUp() throws Exception
  {
    log.info("Setting up");
    Map<Integer, Double> map = new TreeMap<>();
    map.put(1, 1d);
    map.put(5, 1d);
    map.put(10, 3d);
    map.put(14, 3d);
    map.put(19, 5d);
    map.put(22, 4d);

    log.info("Initialising stochastic process (Nonhomogenouspoisson");
    stochasticProcess = new NonHomogenousPoissonProcess(map);
  }

  @Test
  public void test()
  {
    log.info("Initialising test");

    Calendar startTime = Calendar.getInstance();
    startTime.set(Calendar.HOUR_OF_DAY, 12);
    startTime.set(Calendar.MINUTE, 55);

    Calendar nextTime = stochasticProcess.getNextSimulatedEventTime(startTime, 60000);


    log.info("Got next time {}", nextTime);


    log.info("Next execution time from {} is {}", startTime, nextTime);

    log.info("Start time: {} 0", sdf.format(startTime.getTime()));
    log.info("Next execution time: {} 0", sdf.format(nextTime.getTime()));
  }
}
