package uk.ac.kcl.inf.aps.powersim.policies.ondemand;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private long peakLoadWatts;
  private long peakTimeHour;
  private double loadVariance;
  private double peakErrorVariance;
  private int lowerBoundHour;
  private int higherBoundHour;

  /**
   * if set to true, it means that this appliance is generating rather than consuming load
   */
  private boolean generator;

  public DayBoundedGLAppliance(String uid, String type, String subtype, long peakLoadWatts, int peakTimeHour, double loadVariance, double peakErrorVariance, int lowerBoundHour, int higherBoundHour, boolean generator)
  {
    //todo: handle generation (negative load) more clearly
    super(uid, type, subtype);
    this.peakLoadWatts = peakLoadWatts;

    //scale the time parameters to milliseconds
    this.peakTimeHour = peakTimeHour;
    this.loadVariance = loadVariance;

    this.peakErrorVariance = peakErrorVariance;
    this.lowerBoundHour = lowerBoundHour;
    this.higherBoundHour = higherBoundHour;

    this.generator = generator;
  }

  @Override
  public long consumeTimeSlot(SimulationContext simulationContext)
  {
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

    log.trace("Peak load watts {} and variance {}", peakLoadWatts, loadVariance);

    double eulerExponent = 0 - Math.pow(timeHour - peakTimeHour, 2) / (2 * loadVariance * loadVariance);
    log.trace("Euler exp: {}", eulerExponent);

    double expRes = Math.exp(eulerExponent);
    log.trace("Euler power result: {}", expRes);

    double expectedLoadWatts = (double) peakLoadWatts * expRes;
    log.trace("Expected load {}watts from peak load {}watts", expectedLoadWatts, peakLoadWatts);

//    long actualLoadWatts = (long) expectedLoadWatts;

    //todo: put some stochasticity by random variance error
    //calculate the actual random load based on the mean and error variance
    double errorRand = ThreadLocalRandom.current().nextDouble(1);
    double expVariance = peakErrorVariance * expectedLoadWatts / peakLoadWatts;

    log.trace("Expected error variance {} random {}", expVariance, errorRand);
    NormalDistribution normalDistribution = new NormalDistribution(expectedLoadWatts, expVariance);

    long actualLoadWatts = (long) normalDistribution.inverseCumulativeProbability(errorRand);
    log.trace("Actual load: {}watts", actualLoadWatts);

    return generator ? 0 - actualLoadWatts : actualLoadWatts;
  }

  public static DayBoundedGLAppliance getInstance(String uuid, String name, String subtype, DayBoundedGLApplianceRating applianceRating)
  {
    DayBoundedGLAppliance appliance = new DayBoundedGLAppliance(uuid, name, subtype,
            applianceRating.getPeakLoadWatts(),
            applianceRating.getPeakTimeHour(),
            applianceRating.getLoadVariance(),
            applianceRating.getErrorVariance(),
            applianceRating.getLowerBoundHour(),
            applianceRating.getHigherBoundHour(),
            applianceRating.isGenerator());

    return appliance;
  }

}
