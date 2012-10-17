package uk.ac.kcl.inf.aps.powersim.policies;

import uk.ac.kcl.inf.aps.powersim.simulation.SimulationContext;

/**
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:55
 */
public interface Policy
{
  /**
   * Notifies the policy that a new time tick of the simulation has commenced.
   * This method will return immediately and the time tick will be handled asynchronously.
   * @param context - The SimulatorImpl Context, including date, time and any other conditions to which the policy must react.
   */
  public void handleTimeTick(SimulationContext context);

  /**
   * Determines whether the policy has completed all the tasks needed for the current time tick.
   * @return true if the policy is ready, false otherwise
   */
  public boolean isReady();
}
