package uk.ac.kcl.inf.aps.powersim.api;

/**
 * Date: 04/07/13
 * Time: 19:12
 *
 * @author Josef Bajada &lt; josef.bajada@kcl.ac.uk @gt;
 */
public interface ActivityManager
{
  public void requestActivity(ActivityRequest activityRequest);

  public void notifyActivityTermination(ActivityRequest activityRequest);
}
