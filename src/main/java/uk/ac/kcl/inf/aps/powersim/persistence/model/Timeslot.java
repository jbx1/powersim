package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:34
 */
@Entity
public class Timeslot implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Date startDate;

  @Column(nullable = false)
  private Date endDate;

  @ManyToOne(optional = false)
  private Simulation simulation;

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public Simulation getSimulation()
  {
    return simulation;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public void setSimulation(Simulation simulation)
  {
    this.simulation = simulation;
  }
}
