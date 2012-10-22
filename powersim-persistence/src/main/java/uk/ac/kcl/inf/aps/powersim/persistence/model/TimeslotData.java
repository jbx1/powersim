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
@Table(name = "timeslots")
public class TimeslotData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Date startTime;

  @Column(nullable = false)
  private Date endTime;

  @ManyToOne(optional = false)
  @JoinColumn(name="simulation_id")
  private SimulationData simulationData;

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public Date getEndTime()
  {
    return endTime;
  }

  public SimulationData getSimulationData()
  {
    return simulationData;
  }

  public void setStartTime(Date startTime)
  {
    this.startTime = startTime;
  }

  public void setEndTime(Date endTime)
  {
    this.endTime = endTime;
  }

  public void setSimulationData(SimulationData simulationData)
  {
    this.simulationData = simulationData;
  }
}
