package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 17:01
 */
public interface SimulationTimeslotListener
{
  /**
   * Notifies the policy that a new time tick of the simulation has commenced.
   * This method will return immediately and the time tick will be handled asynchronously.
   * @param context - The SimulatorImpl Context, including date, time and any other conditions to which the policy must react.
   */
  public void handleTimeSlot(SimulationContext context);

}
