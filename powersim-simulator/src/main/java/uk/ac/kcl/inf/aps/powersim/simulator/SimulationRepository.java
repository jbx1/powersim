package uk.ac.kcl.inf.aps.powersim.simulator;


import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.Policy;
import uk.ac.kcl.inf.aps.powersim.api.Timeslot;
import uk.ac.kcl.inf.aps.powersim.persistence.model.*;

import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/10/12
 *         Time: 14:45
 */
public interface SimulationRepository
{
  public SimulationData registerSimulation(String name, Date actualStart, Date simulatedStart);

  public void registerHouseholds(SimulationData simulationData, Policy policy, List<? extends Household> households);

  public TimeslotData registerTimeslot(SimulationData simulationData, Timeslot timeslot);

  public ApplianceData registerAppliance(SimulationData simulationData, Appliance appliance)
          throws HouseholdNotRegisteredException;

  public HouseholdData getRegisteredHousehold(Household household)
          throws HouseholdNotRegisteredException;

  public void flushDeferred();

  public void createBulkDeferredConcumptionData(List<ConsumptionData> consumptionDataList);

  public void saveAggregateLoadData(AggregateLoadData aggregateLoadData);

  public void updateSimulationData(SimulationData simulationData);

  public void createDeferredApplianceData(ApplianceData applianceData);

  public void turnOnConsumptionIndexes();

  public void turnOffConsumptionIndexes();

  public void shutdown();

  public boolean isPersistingConsumptionData();

  public void setPersistingConsumptionData(boolean persistingConsumptionData);
}
