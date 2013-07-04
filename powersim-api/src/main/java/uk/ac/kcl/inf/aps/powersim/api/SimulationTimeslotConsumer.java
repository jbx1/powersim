package uk.ac.kcl.inf.aps.powersim.api;

/**
 * To be implemented by classes which are expected to simulate load (watts) at each timeslot.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 12/11/12
 *         Time: 15:03
 */
public interface SimulationTimeslotConsumer
{

  /**
   * Notifies the listener that a timeslot is about to start. Any logic regarding request for power should be done at this stage.
   *
   * @param simulationContext - the simulation context, including the timeslot and other relevant information
   */
  public void prepareForTimeslot(SimulationContext simulationContext);

  /**
   * Method instructing the appliance / household to report the wattage consumed in that timeslot.
   *
   * @param simulationContext - the simulation context, including the timeslot and other relevant information
   * @return the wattage consumed during that timeslot
   */
  public long consumeTimeSlot(SimulationContext simulationContext);

}
