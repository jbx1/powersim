package uk.ac.kcl.inf.aps.powersim.persistence.reporting;

import java.util.Date;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 16/11/12
 *         Time: 15:39
 */
public class TimeslotConsumptionData
{
  private long timeslotId;
  private Date timeSlotStartTime;
  private Date timeSlotEndTime;
  private Long load;

  public TimeslotConsumptionData(long timeslotId, Date timeSlotStartTime, Date timeSlotEndTime, Long load)
  {
    this.timeslotId = timeslotId;
    this.timeSlotStartTime = timeSlotStartTime;
    this.timeSlotEndTime = timeSlotEndTime;
    this.load = load;
  }

  public long getTimeslotId()
  {
    return timeslotId;
  }

  public Date getTimeSlotStartTime()
  {
    return timeSlotStartTime;
  }

  public Date getTimeSlotEndTime()
  {
    return timeSlotEndTime;
  }

  public Long getLoad()
  {
    return load;
  }
}
