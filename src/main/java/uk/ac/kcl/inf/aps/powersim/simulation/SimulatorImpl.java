package uk.ac.kcl.inf.aps.powersim.simulation;

import uk.ac.kcl.inf.aps.powersim.policies.Policy;

import java.util.List;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:29
 */
public class SimulatorImpl implements Runnable, Simulator
{
  Long id;
  List<Policy> policies;

  public SimulatorImpl()
  {
  }

  @Override
  public void run()
  {
    //todo: create the new simulation instance
    //todo: inform all policies to set up their artefacts (households, appliances etc.)

    //todo: this algorithm
    //for each loop
    //  create a new timeslot
    //  notify policies with new timeslot
  }


}
