package uk.ac.kcl.inf.aps.powersim.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.Household;
import uk.ac.kcl.inf.aps.powersim.api.Policy;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple test consumer policy.
 * Creates 1000 households each with a number of appliances and randomly switches them on and off.
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 19/10/12
 *         Time: 14:01
 */
public class SimpleConsumerPolicy implements Policy
{
  protected static final Logger log = LoggerFactory.getLogger(Policy.class);

  private List<SimpleHousehold> households;

  private int householdCount = 0;

  public SimpleConsumerPolicy(int householdCount)
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
      households.add(new SimpleHousehold(UUID.randomUUID().toString(), "default", this));
    }

    return households;
  }

  @Override
  public void handleTimeSlot(SimulationContext context)
  {
    //todo: handle multithreading
    for (SimpleHousehold household : households)
    {
   //   log.debug("Handling household {} ", household.getUid());
      household.handleTimeslot(context);
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
    return "simple-consumer-policy";
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("SimpleConsumerPolicy");
    sb.append("{descriptor=").append(getDescriptor());
    sb.append(", householdCount=").append(householdCount);
    sb.append('}');
    return sb.toString();
  }
}
