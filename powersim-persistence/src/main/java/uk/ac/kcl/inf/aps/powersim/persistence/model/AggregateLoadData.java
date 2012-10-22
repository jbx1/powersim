package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 18/10/12
 *         Time: 18:34
 */
@Entity
@Table(name = "aggregate_load")
public class AggregateLoadData
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private long generated = 0;

  @Column(nullable = false)
  private long consumed = 0;

  @ManyToOne(optional = false)
  @JoinColumn(name="timeslot_id")
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
}
