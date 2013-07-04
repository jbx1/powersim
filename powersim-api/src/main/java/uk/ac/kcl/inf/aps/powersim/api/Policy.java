package uk.ac.kcl.inf.aps.powersim.api;

import java.util.List;

/**
 * Interface to be implemented by any policy simulating the behaviour of a number of households.
 *
 * @author Josef Bajada <josef.bajada@kcl.ac.uk>
 *         Date: 15/10/12
 *         Time: 17:55
 */
public interface Policy extends SimulationTimeslotListener, ActivityAuthoriser
{
  /**
   * Informs the policy that the simulation is about to start.
   * The policy is expected to reply with a list of Households, to be registered with the simulation.
   * Any load coming from any appliances will come from these households.
   *
   * Households are assumed to be fixed throughout a simulation, while appliances can be added throughout the simulation.
   * Note: In the current version an appliance cannot move between households.
   */
  public List<? extends Household> setup();

  /**
   * Determines whether the policy has completed all the tasks needed for the current time tick.
   * @return true if the policy is ready, false otherwise
   */

  /**
   * Determines whether the policy has competed all the tasks needed for the current time tick, and if not waits until timeout.
   * @param timeout - maximum time to wait in milliseconds before giving up waiting for the policy to be ready.
   * @return true if the policy is ready, false if timeout was exceeded
   */
  public boolean ready(int timeout);

  /**
   * A Policy should have an (ideally) unique policy descriptor.
   * This will be associated with each household it manages for reporting purposes.
   *
   * @return A string identifying the policy
   */
  public String getDescriptor();


}
