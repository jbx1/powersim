package uk.ac.kcl.inf.aps.powersim.analyser.utilities;

import uk.ac.kcl.inf.aps.powersim.persistence.model.TimeslotData;
import uk.ac.kcl.inf.aps.powersim.persistence.reporting.TimeslotConsumptionData;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 16/11/12
 *         Time: 16:26
 */
public class ConsumptionAggregate
{
  Date timeslotTime;
  long consumed;
  long generated;

  public ConsumptionAggregate(Date timeslotTime, long consumed, long generated)
  {
    this.timeslotTime = timeslotTime;
    this.consumed = consumed;
    this.generated = generated;
  }

  public long getConsumed()
  {
    return consumed;
  }

  public void setConsumed(long consumed)
  {
    this.consumed = consumed;
  }

  public long getGenerated()
  {
    return generated;
  }

  public void setGenerated(long generated)
  {
    this.generated = generated;
  }


  public Date getTimeslotTime()
  {
    return timeslotTime;
  }

  public ConsumptionAggregate(Date timeslotTime)
  {
    this.timeslotTime = timeslotTime;
  }

  public static SortedMap<Long, ConsumptionAggregate> aggregateConsumption(List<TimeslotData> timeslotList, List<TimeslotConsumptionData> timeslotConsumptionDataList)
  {
    SortedMap<Long, ConsumptionAggregate> consumptionAggregateMap = new TreeMap<>();

    for (TimeslotData timeslotData : timeslotList)
    {
      consumptionAggregateMap.put(timeslotData.getId(), new ConsumptionAggregate(timeslotData.getStartTime(), 0, 0));
    }

    for (TimeslotConsumptionData timeslotConsumptionData: timeslotConsumptionDataList)
    {
      long id = timeslotConsumptionData.getTimeslotId();
      long consumed = timeslotConsumptionData.getLoad() < 0 ? 0 : timeslotConsumptionData.getLoad();
      long generated = timeslotConsumptionData.getLoad() > 0 ? 0 : 0-timeslotConsumptionData.getLoad();

      ConsumptionAggregate consumptionAggregate = consumptionAggregateMap.get(id);
      if (consumptionAggregate == null) //this should never happen, but just in case
      {
        consumptionAggregate = new ConsumptionAggregate(timeslotConsumptionData.getTimeSlotStartTime(), consumed, generated);
        consumptionAggregateMap.put(id, consumptionAggregate);
      }
      else
      {
        consumptionAggregate.setConsumed(consumptionAggregate.getConsumed() + consumed);
        consumptionAggregate.setGenerated(consumptionAggregate.getGenerated() + generated);
      }
    }

    return consumptionAggregateMap;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ConsumptionAggregate");
    sb.append("{timeslotTime=").append(timeslotTime);
    sb.append(", consumed=").append(consumed);
    sb.append(", generated=").append(generated);
    sb.append('}');
    return sb.toString();
  }
}
