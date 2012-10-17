package uk.ac.kcl.inf.aps.powersim.simulation;

import uk.ac.kcl.inf.aps.powersim.persistence.Timeslot;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 14:56
 */
public class SimulationContext
{
  //todo: add different contextual conditions such as Tariff information and Weather

  private final Timeslot timeslot;

  public SimulationContext(Timeslot timeslot)
  {
    this.timeslot = timeslot;
  }

  public Timeslot getTimeslot()
  {
    return timeslot;
  }
}
