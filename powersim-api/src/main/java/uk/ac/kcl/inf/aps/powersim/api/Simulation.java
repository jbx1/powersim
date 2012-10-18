package uk.ac.kcl.inf.aps.powersim.api;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 15:13
 */
public interface Simulation
{
  String getName();

  void handleConsumptionEvents(List<ConsumptionEvent> consumptionEvents);
}
