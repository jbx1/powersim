package uk.ac.kcl.inf.aps.powersim.api;

import java.util.Date;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 14:48
 */
public class Timeslot
{
  private Date startTime;
  private Date endTime;

  public Timeslot(Date startTime, Date endTime)
  {
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public Date getEndTime()
  {
    return endTime;
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
