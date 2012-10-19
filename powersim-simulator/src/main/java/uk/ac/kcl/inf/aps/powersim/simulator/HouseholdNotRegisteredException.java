package uk.ac.kcl.inf.aps.powersim.simulator;

import uk.ac.kcl.inf.aps.powersim.api.Household;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 19/10/12
 *         Time: 11:30
 */
public class HouseholdNotRegisteredException extends Exception
{
  private Household unregisteredHousehold;

  public HouseholdNotRegisteredException(Household unregisteredHousehold)
  {
    super(unregisteredHousehold + "not registered!");
    this.unregisteredHousehold = unregisteredHousehold;
  }

  public Household getUnregisteredHousehold()
  {
    return unregisteredHousehold;
  }
}
