package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:47
 */
@Entity
public class ConsumptionEvent implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Appliance appliance;

  @ManyToOne(optional = false)
  private Timeslot timeslot;

  @Column(nullable = false)
  private Long loadWatts;

  public ConsumptionEvent(Appliance appliance, Timeslot timeslot, Long loadWatts)
  {
    this.appliance = appliance;
    this.timeslot = timeslot;
    this.loadWatts = loadWatts;
  }

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Appliance getAppliance()
  {
    return appliance;
  }

  public void setAppliance(Appliance appliance)
  {
    this.appliance = appliance;
  }

  public Timeslot getTimeslot()
  {
    return timeslot;
  }

  public void setTimeslot(Timeslot timeslot)
  {
    this.timeslot = timeslot;
  }

  public Long getLoadWatts()
  {
    return loadWatts;
  }

  public void setLoadWatts(Long loadWatts)
  {
    this.loadWatts = loadWatts;
  }
}
