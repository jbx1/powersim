package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:50
 */
@Entity
@Table(name="appliances")
@org.hibernate.annotations.Table(appliesTo = "appliances", indexes =
        {
                @Index(name = "appliances_simulation_id_idx", columnNames = "simulation_id")
        })
@NamedQueries({
        @NamedQuery(name = "ApplianceData.countForSimulation",
                query = "select count(a) from ApplianceData a where a.simulationData.id = :simulationId"),

        @NamedQuery(name = "ApplianceData.getForHousehold",
                query = "select a from ApplianceData a, ConsumptionData c where c.applianceData.id = a.id and c.householdData.id = :householdId"),

        @NamedQuery(name="ApplianceData.deleteBySimulationId",
                query = "delete from ApplianceData a where a.simulationData.id = :simulationId")

})
public class ApplianceData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String referenceId;

  private String type;

  @ManyToOne(optional = false)
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

  public String getType()
  {
    return type;
  }

  public void setType(String type)
  {
    this.type = type;
  }

  public String getReferenceId()
  {
    return referenceId;
  }

  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }

  public SimulationData getSimulationData()
  {
    return simulationData;
  }

  public void setSimulationData(SimulationData simulationData)
  {
    this.simulationData = simulationData;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ApplianceData");
    sb.append("{id=").append(id);
    sb.append(", referenceId='").append(referenceId).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", simulationData=").append(simulationData);
    sb.append('}');
    return sb.toString();
  }
}
