package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 14:56
 */
public final class SimulationContext implements Comparable<SimulationContext>
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
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    SimulationContext that = (SimulationContext) o;

    if (!simulation.equals(that.simulation))
    {
      return false;
    }
    if (!timeslot.equals(that.timeslot))
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = simulation.hashCode();
    result = 31 * result + timeslot.hashCode();
    return result;
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

  /**
   * Comparing two Simulation Contexts is equivalent to comparing their internal Timeslots
   * @param o - the Simulation context
   * @return less than 0 if this simulation context comes before the passed simulation context, 0 if equal, less than 0 if it comes after
   */
  @Override
  public int compareTo(SimulationContext o)
  {
    return this.getTimeslot().compareTo(o.getTimeslot());
  }
}
