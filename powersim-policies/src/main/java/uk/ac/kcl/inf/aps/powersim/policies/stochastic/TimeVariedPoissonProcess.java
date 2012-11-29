package uk.ac.kcl.inf.aps.powersim.policies.stochastic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements a non-homogenous poisson process, whose rate varies by time according to the provided mapping between hour and rate.
 * The maximum configuration granularity is hourly, although hours can be skipped. This class will compute the actual
 * rate in minute granularity, using the gradient between the provided 2 hourly rates bounding the specific required timeslot.
 *
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 12/11/12
 *         Time: 14:51
 */
public class TimeVariedPoissonProcess implements StochasticProcess
{
  public static final Logger log = LoggerFactory.getLogger(TimeVariedPoissonProcess.class);

  public static final long RATE_TIMEUNIT_SIZE = 3600000; //1 hour (in milliseconds)

  /**
   * Map between the hour number and the rate (per hour) that the appliance is expected to go ON within that hour
   */
  private Map<Integer, Double> mapHourlyRate;

  private Double maxRate;

  public TimeVariedPoissonProcess(Map<Integer, Double> mapHourlyRate)
  {
    this.mapHourlyRate = mapHourlyRate;
    this.maxRate = Collections.max(this.mapHourlyRate.values());
    log.trace("Initialised non-homogenous poisson process with max rate {}", maxRate);
  }


  @Override
  public Calendar getNextSimulatedEventTime(Calendar startTime, long slotDuration)
  {
    //todo: handle the case when the rate is 0 better (currently it hangs)

    Calendar rateTimeCal = Calendar.getInstance();
    double maxRatePerDuration = maxRate * ((double) slotDuration / RATE_TIMEUNIT_SIZE);
    log.trace("Max Rate per duration: {}", maxRatePerDuration);
    boolean found = false;
    long timeSlotOffset = 0;

    //simulate the non-homogenous Poisson Process
    do
    {
      //generate the first random number
      double randUmaxRate = ThreadLocalRandom.current().nextDouble(1);
      log.trace("Random number generated randUmaxRate: {}", randUmaxRate);
      //calculate the offset according to Poisson distribution using the maxRate
      timeSlotOffset -= (long) (Math.log(randUmaxRate) / maxRatePerDuration);
      log.trace("Timeslot offset {}", timeSlotOffset);

      //compute the offset in milliseconds
      long rateTime = startTime.getTimeInMillis() + (timeSlotOffset * slotDuration);
      rateTimeCal.setTimeInMillis(rateTime);

      //generate the second random number for the non-homogenous poisson filtering
      double randUrelRate = ThreadLocalRandom.current().nextDouble(1);

      //get the non-homogenous rate lambda(t) for the time that came up
      double rateForTime = getRate(mapHourlyRate, rateTimeCal, slotDuration);
      log.trace("*** Rate for Time {} per duration {}", rateForTime, slotDuration);

      //compute the ratio of lambda(t) rate with regards to the max rate
      double rateRatio = rateForTime / maxRatePerDuration;
      log.trace("*** Rate ratio {} while random number was {}", rateRatio, randUrelRate);

      //if the random number generated is smaller or equal to the ratio, accept it
      if (randUrelRate <= rateRatio)
      {
        log.trace("Smaller therefore accepted!");
        found = true;
      }
    }
    while (!found);

    return rateTimeCal;
  }

  public static double getRate(Map<Integer, Double> mapHourRates, Calendar time, long duration)
          throws IllegalArgumentException
  {
    //todo: unit test these functions
    log.trace("Finding the rate for {} and duration {}ms", time, duration);

    int hour = time.get(Calendar.HOUR_OF_DAY);
    log.trace("Required Hour is {}", hour);

    //find the surrounding hours before (inclusive) and after it (exclusive)
    log.trace("Getting start hour");
    HourRateTuple startHourRate = getStartRate(mapHourRates, hour);

    log.trace("Getting end hour");
    HourRateTuple endHourRate = getEndRate(mapHourRates, hour+1);

    log.trace("Found 2 boundary rates defined start {} and end {}", startHourRate, endHourRate);
    if (startHourRate.getRate() == endHourRate.getRate())
    {
      log.trace("No slope in rates, we can return the rate (possibly because of start hour and end hour being the same)");
      return startHourRate.getRate() * ((double) duration / RATE_TIMEUNIT_SIZE);
    }

    log.trace("startRate is {} and endRate is {}", startHourRate.getRate(), endHourRate.getRate());

    //find the gradient of the line between the 2 sample rates
    //x is the Hour, y is the Rate
    int startX = startHourRate.getHour();
    //if the start hour is before midnight, reduce 24hrs from it (it will be -ve on the axis)
    if (startX > hour)
    {
      startX -= 24;
    }

    int endX = endHourRate.getHour();
    //if the end hour is before the start hour (because it overlaps midnight), add 24hrs to it
    if (startX > endX)
    {
      endX += 24;
    }

    log.trace("startX is {} and endX is {}", startX, endX);

    //find the gradient m
    double m = (endHourRate.getRate() - startHourRate.getRate()) / (endX - startX);
    //find the y intercept b = y - mx
    double b = (endHourRate.getRate() - (m * endX));

    log.trace("m is {} and b is {}", m, b);
    //todo: can we reuse m and b? can we cache the gradients?

    //todo: check if the floating point operations can be changed to integer (change the rate per second?)
    //determine the x for which we want to calculate y
    double minuteIncr = (double) time.get(Calendar.MINUTE) / 60;
    double secondIncr = (double) time.get(Calendar.SECOND) / 3600;
    double inputX = hour + minuteIncr + secondIncr;

    //inputX is currently for the startTime of the timeslot, but we want it for the mid point of the duration
    double midpointXOffset = (duration / 2) / 3600000; //find the midpoint offset in relation to hours
    inputX += midpointXOffset;

    //y = mx + b, where y is the rate
    double yHourlyRate = m * inputX + b;

    log.trace("Calculated hourly rate {} for time {}", yHourlyRate, time);

    log.trace("Calculating rate for duration {} in relation to hour {} ", duration, RATE_TIMEUNIT_SIZE);

    double yRate = yHourlyRate * ((double) duration / RATE_TIMEUNIT_SIZE);
    log.trace("Calculated rate for timeslot {} ", yRate);

    return yRate;
  }

  private static HourRateTuple getStartRate(Map<Integer, Double> mapHourRates, int startHour)
  {
    int startRateHour = startHour;

    Double startRate = mapHourRates.get(startRateHour);
    while (startRate == null)
    {
      if (startRateHour <= 0)
      {
        startRateHour += 24 ; //cycle midnight
      }

      startRate = mapHourRates.get(--startRateHour);
      if ((startRate == null) && (startRateHour == startHour)) //we did a full cycle?!
        throw new IllegalArgumentException("Invalid Hourly map rates passed");
    }

    return new HourRateTuple(startRateHour, startRate);
  }

  private static HourRateTuple getEndRate(Map<Integer, Double> mapHourRates, int endHour)
  {
    int endRateHour = endHour;

    Double endRate = mapHourRates.get(endRateHour);
    while (endRate == null)
    {
      if (endRateHour >= 24)
      {
        endRateHour -= 24; //cycle midnight
      }

      endRate = mapHourRates.get(++endRateHour);
      if ((endRate == null) && (endRateHour == endHour)) //we did a full cycle?!
        throw new IllegalArgumentException("Invalid Hourly map rates passed");

    }

    return new HourRateTuple(endRateHour, endRate);
  }

  private static class HourRateTuple
  {
    int hour;
    double rate;

    HourRateTuple(int hour, double rate)
    {
      this.hour = hour;
      this.rate = rate;
    }

    public int getHour()
    {
      return hour;
    }

    public void setHour(int hour)
    {
      this.hour = hour;
    }

    public double getRate()
    {
      return rate;
    }

    public void setRate(double rate)
    {
      this.rate = rate;
    }

    @Override
    public String toString()
    {
      final StringBuilder sb = new StringBuilder();
      sb.append("HourRateTuple");
      sb.append("{hour=").append(hour);
      sb.append(", rate=").append(rate);
      sb.append('}');
      return sb.toString();
    }
  }
}
