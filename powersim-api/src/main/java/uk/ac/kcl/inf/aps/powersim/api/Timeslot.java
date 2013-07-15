package uk.ac.kcl.inf.aps.powersim.api;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 14:48
 */
public final class Timeslot implements Comparable<Timeslot>
{
  private Calendar startTime;
  private Calendar endTime;

  public Timeslot(Date startTime, Date endTime)
  {
    this.startTime = Calendar.getInstance();
    this.startTime.setTime(startTime);

    this.endTime = Calendar.getInstance();
    this.endTime.setTime(endTime);
  }

  public Timeslot(long starTimeMillis, long endTimeMillis)
  {
    this.startTime = Calendar.getInstance();
    this.startTime.setTimeInMillis(starTimeMillis);

    this.endTime = Calendar.getInstance();
    this.endTime.setTimeInMillis(endTimeMillis);
  }

  public Timeslot(Calendar startTime, Calendar endTime)
  {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Calendar getStartTime()
  {
    return startTime;
  }

  public Calendar getEndTime()
  {
    return endTime;
  }

  public long getDuration()
  {
    return endTime.getTimeInMillis() - startTime.getTimeInMillis();
  }

  public long getMidTimeInMillis()
  {
    return (endTime.getTimeInMillis() + startTime.getTimeInMillis()) / 2;
  }

  public double getMidTimeInHours()

  {
    long midTime = getMidTimeInMillis();

    Calendar midTimeCal = Calendar.getInstance();
    midTimeCal.setTimeInMillis(midTime);

    int hour = midTimeCal.get(Calendar.HOUR_OF_DAY);

    //determine the time offset for which we want to calculate load
    double minuteIncr = (double) midTimeCal.get(Calendar.MINUTE) / 60;
    double secondIncr = (double) midTimeCal.get(Calendar.SECOND) / 3600;

    return hour + minuteIncr + secondIncr;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    Timeslot timeslot = (Timeslot) o;

    if (!endTime.equals(timeslot.endTime))
    {
      return false;
    }
    if (!startTime.equals(timeslot.startTime))
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    int result = startTime.hashCode();
    result = 31 * result + endTime.hashCode();
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("Timeslot");
    sb.append("{startTime=").append(startTime);
    sb.append(", endTime=").append(endTime);
    sb.append('}');
    return sb.toString();
  }

  /**
   * Compares two timeslots. A timeslot is 'less' than another if its start time comes before the start time of the other timeslot.
   * If both timeslots have the same start time, the end time is compared. (Unlikely to have a timeslot with same start time but different end times
   * @param o - the timeslot to be compared with
   * @return less than 0 if the timeslot comes before, 0 if the timeslot is identical, greater than 0 if the timeslot comes after
   */
  @Override
  public int compareTo(Timeslot o)
  {
    int start = startTime.compareTo(o.getStartTime());
    return start == 0 ? start : endTime.compareTo(o.getEndTime());
  }
}
