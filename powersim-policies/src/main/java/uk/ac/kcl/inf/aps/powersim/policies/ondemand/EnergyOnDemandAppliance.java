package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import uk.ac.kcl.inf.aps.powersim.api.Appliance;
import uk.ac.kcl.inf.aps.powersim.api.SimulationTimeslotConsumer;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 15/11/12
 *         Time: 17:04
 */
public abstract class EnergyOnDemandAppliance extends Appliance implements SimulationTimeslotConsumer
{
  public EnergyOnDemandAppliance(String uid, String type)
  {
    super(uid, type);
  }
}
