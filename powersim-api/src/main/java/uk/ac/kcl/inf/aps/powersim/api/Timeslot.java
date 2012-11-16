package uk.ac.kcl.inf.aps.powersim.api;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 14:48
 */
public final class Timeslot
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
}
