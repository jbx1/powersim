package uk.ac.kcl.inf.aps.powersim;

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
    System.out.println("Starting Power Simulator" );

    // open/read the application context file
    ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    log.info("Loaded application context");

    // instantiate our spring dao object from the application context
//      FileEventDao fileEventDao = (FileEventDao)ctx.getBean("fileEventDao");

  }
}