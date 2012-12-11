package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 14:56
 */
public final class SimulationContext
{
  private Simulation simulation;

  private final Timeslot timeslot;

  public SimulationContext(Simulation simulation, Timeslot timeslot)
  {
    this.simulation = simulation;
    this.timeslot = timeslot;
  }

  public Timeslot getTimeslot()
  {
    return timeslot;
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("SimulationContext");
    sb.append("{simulation=").append(simulation.getName());
    sb.append(", timeslot=").append(timeslot);
    sb.append('}');
    return sb.toString();
  }
}
