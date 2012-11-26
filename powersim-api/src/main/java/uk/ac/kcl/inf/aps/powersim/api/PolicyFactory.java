package uk.ac.kcl.inf.aps.powersim.api;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 15:22
 */
public interface PolicyFactory<P extends Policy>
{
  public abstract P getPolicyInstance();

  public String getName();
}
