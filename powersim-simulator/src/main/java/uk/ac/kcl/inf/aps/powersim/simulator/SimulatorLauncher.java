package uk.ac.kcl.inf.aps.powersim.simulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *   Date: 15/10/2012
 *   Time: 18:06
 */
public class SimulatorLauncher
{
  protected static final Logger log = LoggerFactory.getLogger(SimulatorLauncher.class);

  public static void main( String[] args )
  {
    log.info("Starting Power simulator");

    //todo: read configuration name from command line

    // open/read the application context file
    log.debug("Loading Spring application context");
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application-context.xml");

    Simulator simulator = (Simulator) ctx.getBean("simulator");
    simulator.start();
  }
}
