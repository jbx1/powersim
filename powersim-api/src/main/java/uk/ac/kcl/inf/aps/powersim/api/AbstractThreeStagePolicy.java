package uk.ac.kcl.inf.aps.powersim.api;

/**
 *
 * Date: 04/07/13
 * Time: 16:25
 * @author josef.bajada
 */
public abstract class AbstractThreeStagePolicy implements Policy
{
  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    //notify the households to prepare for the new timeslot
    preparationStage(context);

    //schedule any appliances to do their activities in this timeslot
    schedulingStage(context);

    //collect consumption events from appliances and households
    consumptionStage(context);
  }

  protected abstract void preparationStage(SimulationContext context);

  protected abstract void schedulingStage(SimulationContext context);

  protected abstract void consumptionStage(SimulationContext context);

}
