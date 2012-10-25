package uk.ac.kcl.inf.aps.powersim.persistence.reporting;

import java.util.Date;

/**
 * A class used to join tables together and get data for reporting purposes.
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 25/10/12
 *         Time: 12:57
 */
public class SimulationTimeslotAggregateData
{
  private Date timeSlotStartTime;
  private Date timeSlotEndTime;
  private Long consumed;
  private Long generated;

  public SimulationTimeslotAggregateData(Date timeSlotStartTime, Date timeSlotEndTime, Long consumed, Long generated)
  {
    this.timeSlotStartTime = timeSlotStartTime;
    this.timeSlotEndTime = timeSlotEndTime;
    this.consumed = consumed;
    this.generated = generated;
  }


  public Date getTimeSlotStartTime()
  {
    return timeSlotStartTime;
  }

  public Date getTimeSlotEndTime()
  {
    return timeSlotEndTime;
  }

  public Long getConsumed()
  {
    return consumed;
  }

  public Long getGenerated()
  {
    return generated;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("SimulationTimeslotAggregateData{");
    sb.append("timeSlotStartTime=").append(timeSlotStartTime);
    sb.append(", timeSlotEndTime=").append(timeSlotEndTime);
    sb.append(", consumed=").append(consumed);
    sb.append(", generated=").append(generated);
    sb.append('}');
    return sb.toString();
  }
}
