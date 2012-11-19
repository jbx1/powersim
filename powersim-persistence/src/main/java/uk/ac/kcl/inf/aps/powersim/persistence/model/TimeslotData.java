package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;

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
@org.hibernate.annotations.Table(appliesTo = "timeslots", indexes =
        {
                @Index(name = "timeslots_simulation_id_idx", columnNames = "simulation_id")
        })
@NamedQueries({
        @NamedQuery(name = "TimeslotData.findAll",
                query = "select t from TimeslotData t where t.simulationData.id = :simulationId order by startTime"),

        @NamedQuery(name="TimeslotData.deleteBySimulationId",
                query = "delete from TimeslotData t where t.simulationData.id = :simulationId"),
})
public class TimeslotData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Date startTime;

  @Column(nullable = false)
  private Date endTime;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="simulation_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
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

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("TimeslotData");
    sb.append("{id=").append(id);
    sb.append(", startTime=").append(startTime);
    sb.append(", endTime=").append(endTime);
    sb.append(", simulationData=").append(simulationData);
    sb.append('}');
    return sb.toString();
  }
}
