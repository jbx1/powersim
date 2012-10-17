package uk.ac.kcl.inf.aps.powersim.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.kcl.inf.aps.powersim.persistence.model.Simulation;
import uk.ac.kcl.inf.aps.powersim.persistence.model.SimulationDao;
import uk.ac.kcl.inf.aps.powersim.policies.Policy;

import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:29
 */
public class SimulatorImpl implements Runnable, Simulator
{
  protected static final Logger log = LoggerFactory.getLogger(SimulatorImpl.class);

  private Long id;
  private List<Policy> policies;

  private Thread thread;

  @Autowired
  private SimulationDao simulationDao;

  //todo: configuration (time intervals, duration, etc.) use YAML?

  public SimulatorImpl()
  {
  }

  public void start()
  {
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run()
  {
    log.info("Starting Simulation!");
    try
    {
      Simulation simulation = new Simulation();
      simulation.setName("Test Simulation 1! Hello world!");
      Date now = new Date(System.currentTimeMillis());

      simulation.setActualStartTime(now);
      simulation.setSimulatedStartTime(now);
      simulationDao.create(simulation);

      Thread.currentThread().sleep(5000);

      now = new Date(System.currentTimeMillis());

      log.info("Simulation Ready!");
      simulation.setActualEndTime(now);
      simulation.setSimulatedEndTime(now);

      simulationDao.update(simulation);

    }
    catch (InterruptedException e)
    {
      log.warn("Error", e);
    }




    //todo: create the new simulation instance
    //todo: inform all policies to set up their artefacts (households, appliances etc.)

    //todo: this algorithm
    //for each loop
    //  create a new timeslot
    //  notify policies with new timeslot

  }
}
