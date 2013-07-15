package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.kcl.inf.aps.powersim.api.ActivityRequest;
import uk.ac.kcl.inf.aps.powersim.api.SimulationContext;
import uk.ac.kcl.inf.aps.powersim.policies.stochastic.NormalDistProcess;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 15/11/12
 *         Time: 17:06
 */
public class DayBoundedGLAppliance extends EnergyOnDemandAppliance
{
  protected static final Logger log = LoggerFactory.getLogger(NormalDistProcess.class);

  private final long peakLoadWatts;
  private long peakTimeHour;
  private double loadVariance;
  private double peakErrorVariance;
  private int lowerBoundHour;
  private int higherBoundHour;

  public DayBoundedGLAppliance(String uid, String type, long peakLoadWatts)
  {
    super(uid, type);
    this.peakLoadWatts = peakLoadWatts;
  }

  @Override
  public void prepareForTimeslot(SimulationContext simulationContext)
  {

  }

  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
    log.trace("Appliance {} consuming timeslot {}", this.getUid(), simulationContext.getTimeslot());
    //lets work with the absolute value (ignoring the negative) and then invert it again in the end
    double absPeakLoad = Math.abs(peakLoadWatts);

    //check the bounding hours
    int startHour = simulationContext.getTimeslot().getStartTime().get(Calendar.HOUR_OF_DAY);
    int endHour = simulationContext.getTimeslot().getEndTime().get(Calendar.HOUR_OF_DAY);

    log.trace("Start hour {} and end hour {}", startHour, endHour);
    log.trace("Lower bound {} and higher bound {}", lowerBoundHour, higherBoundHour);
    if ((startHour < lowerBoundHour) || (endHour > higherBoundHour))
    {
      //todo: take care of overlapping midnight hours
      log.trace("Out of operation hourly bounds, no load.");
      return 0;
    }

    //calculate mean at current time using Hubbert Peak (Gaussian) curve
    double timeHour = simulationContext.getTimeslot().getMidTimeInHours();
    log.trace("Mid-time in hours for which to calculate: {}", timeHour);

    log.trace("Peak load watts {} and variance {}", absPeakLoad, loadVariance);

    double eulerExponent = 0 - Math.pow(timeHour - peakTimeHour, 2) / (2 * loadVariance * loadVariance);
    log.trace("Euler exp: {}", eulerExponent);

    double expRes = Math.exp(eulerExponent);
    log.trace("Euler power result: {}", expRes);

    double expectedLoadWatts = absPeakLoad * expRes;
    log.trace("Expected load {}watts from peak load {}watts", expectedLoadWatts, absPeakLoad);

    //calculate the actual random load based on the mean and error variance
    double errorRand = ThreadLocalRandom.current().nextDouble(1);
    double expVariance = peakErrorVariance * expectedLoadWatts / absPeakLoad;

    log.trace("Expected error variance {} random {}", expVariance, errorRand);
    NormalDistribution normalDistribution = new NormalDistribution(expectedLoadWatts, expVariance);
    long actualLoadWatts = (long) normalDistribution.inverseCumulativeProbability(errorRand);
    log.trace("Actual load: {}watts", actualLoadWatts);

    long consumedLoad = (peakLoadWatts < 0) ? 0 - actualLoadWatts : actualLoadWatts;
    log.trace("Appliance {} consumed {}", this.getUid(), consumedLoad);
    return consumedLoad;
  }

  public long getPeakTimeHour()
  {
    return peakTimeHour;
  }

  public void setPeakTimeHour(long peakTimeHour)
  {
    this.peakTimeHour = peakTimeHour;
  }

  public double getLoadVariance()
  {
    return loadVariance;
  }

  public void setLoadVariance(double loadVariance)
  {
    this.loadVariance = loadVariance;
  }

  public double getPeakErrorVariance()
  {
    return peakErrorVariance;
  }

  public void setPeakErrorVariance(double peakErrorVariance)
  {
    this.peakErrorVariance = peakErrorVariance;
  }

  public int getLowerBoundHour()
  {
    return lowerBoundHour;
  }

  public void setLowerBoundHour(int lowerBoundHour)
  {
    this.lowerBoundHour = lowerBoundHour;
  }

  public int getHigherBoundHour()
  {
    return higherBoundHour;
  }

  public void setHigherBoundHour(int higherBoundHour)
  {
    this.higherBoundHour = higherBoundHour;
  }

  @Override
  public void activate(SimulationContext simulationContext, ActivityRequest request)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void deactivate(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void suspend(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void resume(SimulationContext simulationContext)
  {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
