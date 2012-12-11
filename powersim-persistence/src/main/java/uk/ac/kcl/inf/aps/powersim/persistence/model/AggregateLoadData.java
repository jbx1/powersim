package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 18:34
 */
@Entity
@Table(name = "aggregate_load")
@org.hibernate.annotations.Table(appliesTo = "aggregate_load", indexes =
        {
                @Index(name = "aggregate_load_timeslot_id_idx", columnNames = "timeslot_id")
        })
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)

@NamedQueries({
        @NamedQuery(name="AggregateLoadData.getAggregateLoadForSimulation",
        query="select new uk.ac.kcl.inf.aps.powersim.persistence.reporting.SimulationTimeslotAggregateData(t.startTime, t.endTime, a.consumed, a.generated) " +
                "from AggregateLoadData a JOIN a.timeslotData t " +
                "where t.simulationData.id = :simulationId " +
                "order by t.startTime"),

        @NamedQuery(name="AggregateLoadData.deleteBySimulationId",
                query = "delete from AggregateLoadData a where a.timeslotData.simulationData.id = :simulationId")
}
)
public class AggregateLoadData
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private long generated = 0;

  @Column(nullable = false)
  private long consumed = 0;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="timeslot_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private TimeslotData timeslotData;

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public long getGenerated()
  {
    return generated;
  }

  public void setGenerated(long generated)
  {
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

  public TimeslotData getTimeslotData()
  {
    return timeslotData;
  }

  public void setTimeslotData(TimeslotData timeslotData)
  {
    this.timeslotData = timeslotData;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("AggregateLoadData");
    sb.append("{id=").append(id);
    sb.append(", generated=").append(generated);
    sb.append(", consumed=").append(consumed);
    sb.append(", timeslotData=").append(timeslotData);
    sb.append('}');
    return sb.toString();
  }
}
