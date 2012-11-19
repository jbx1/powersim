package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 15/11/12
 *         Time: 18:05
 */
public enum DayBoundedGLApplianceRating
{
  PV (1000, 12, 2, 100, 6, 19, true);

  private long peakLoadWatts;
  private int peakTimeHour;
  private double loadVariance;
  private double errorVariance;
  private int lowerBoundHour;
  private int higherBoundHour;
  private boolean generator;

  private DayBoundedGLApplianceRating(long peakLoadWatts, int peakTimeHour, double loadVariance, double errorVariance, int lowerBoundHour, int higherBoundHour, boolean generator)
  {
    this.peakLoadWatts = peakLoadWatts;
    this.peakTimeHour = peakTimeHour;
    this.loadVariance = loadVariance;
    this.errorVariance = errorVariance;
    this.lowerBoundHour = lowerBoundHour;
    this.higherBoundHour = higherBoundHour;
    this.generator = generator;
  }

  public long getPeakLoadWatts()
  {
    return peakLoadWatts;
  }

  public int getPeakTimeHour()
  {
    return peakTimeHour;
  }

  public double getLoadVariance()
  {
    return loadVariance;
  }

  public double getErrorVariance()
  {
    return errorVariance;
  }

  public int getLowerBoundHour()
  {
    return lowerBoundHour;
  }

  public int getHigherBoundHour()
  {
    return higherBoundHour;
  }

  public boolean isGenerator()
  {
    return generator;
  }
}
