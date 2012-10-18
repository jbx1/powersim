package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

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
  private int generated = 0;

  @Column(nullable = false)
  private int consumed = 0;

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

  public int getGenerated()
  {
    return generated;
  }

  public void setGenerated(int generated)
  {
    this.generated = generated;
  }

  public int getConsumed()
  {
    return consumed;
  }

  public void setConsumed(int consumed)
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
