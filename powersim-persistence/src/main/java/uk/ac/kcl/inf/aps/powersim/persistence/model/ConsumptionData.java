package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:47
 */
@Entity
@Table(name="consumption")
@org.hibernate.annotations.Table(appliesTo = "consumption", indexes =
        {
                @Index(name = "consumption_appliance_id_idx", columnNames = "appliance_id"),
                @Index(name = "consumption_timeslot_id_idx", columnNames = "timeslot_id")
        })
@NamedQueries(
        @NamedQuery(name="ConsumptionData.deleteBySimulationId",
                query = "delete from ConsumptionData c where c.timeslotData.simulationData.id = :simulationId")
)
public class ConsumptionData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name="appliance_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private ApplianceData appliance;

  @ManyToOne(optional = false)
  @JoinColumn(name="timeslot_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private TimeslotData timeslotData;

  @Column(nullable = false)
  private Long loadWatts;

  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public ApplianceData getAppliance()
  {
    return appliance;
  }

  public void setAppliance(ApplianceData appliance)
  {
    this.appliance = appliance;
  }

  public TimeslotData getTimeslotData()
  {
    return timeslotData;
  }

  public void setTimeslotData(TimeslotData timeslotData)
  {
    this.timeslotData = timeslotData;
  }

  public Long getLoadWatts()
  {
    return loadWatts;
  }

  public void setLoadWatts(Long loadWatts)
  {
    this.loadWatts = loadWatts;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ConsumptionData");
    sb.append("{id=").append(id);
    sb.append(", appliance=").append(appliance);
    sb.append(", timeslotData=").append(timeslotData);
    sb.append(", loadWatts=").append(loadWatts);
    sb.append('}');
    return sb.toString();
  }
}
