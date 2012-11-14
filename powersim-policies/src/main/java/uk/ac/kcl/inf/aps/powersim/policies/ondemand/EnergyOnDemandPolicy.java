package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.Policy;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 09/11/12
 *         Time: 16:46
 */
public class EnergyOnDemandPolicy implements Policy
{
  protected static final Logger log = LoggerFactory.getLogger(EnergyOnDemandPolicy.class);

  private List<EnergyOnDemandHousehold> households;

  private int householdCount = 0;

  public EnergyOnDemandPolicy(int householdCount)
  {
    this.householdCount = householdCount;
  }

  public int getHouseholdCount()
  {
    return householdCount;
  }

  @Override
  public List<? extends Household> setup()
  {
    //create 100 households
    households = new ArrayList<>(householdCount);
    for (int i = 0; i < householdCount; i++)
    {
      households.add(new EnergyOnDemandHousehold(UUID.randomUUID().toString(), "default", this));
    }

    return households;
  }

  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    //todo: handle multithreading
    for (EnergyOnDemandHousehold household : households)
    {
      //   log.debug("Handling household {} ", household.getUid());
      household.consumeTimeSlot(context);
    }
  }

  @Override
  public boolean ready(int timeout)
  {
    //todo: handle multithreading
    return true;
  }

  @Override
  public String getDescriptor()
  {
    return "energy-on-demand-policy";
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("EnergyOnDemandPolicy");
    sb.append("{descriptor=").append(getDescriptor());
    sb.append(", householdCount=").append(householdCount);
    sb.append('}');
    return sb.toString();
  }

}
