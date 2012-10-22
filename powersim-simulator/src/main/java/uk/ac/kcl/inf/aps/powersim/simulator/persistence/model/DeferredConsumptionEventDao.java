package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/10/12
 *         Time: 14:45
 */
public interface DeferredConsumptionEventDao
{
  public void flushDeferred();

  public void createBulkDeferredConcumptionData(List<ConsumptionData> consumptionDataList);

  public void createBulkDeferredApplianceData(List<ApplianceData> applianceDataList);

  public void createDeferredApplianceData(ApplianceData applianceData);
}
