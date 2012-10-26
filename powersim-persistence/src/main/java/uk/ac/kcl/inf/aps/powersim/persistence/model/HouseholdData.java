package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:44
 */
@Entity
@Table(name = "households")
@NamedQueries(
        @NamedQuery(name="HouseholdData.countForSimulation",
                query="select count(h) from HouseholdData h where h.simulationData.id=:simulationId")
)
public class HouseholdData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String referenceId;

  private String category;

  private String policyDescriptor;

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

  public SimulationData getSimulationData()
  {
    return simulationData;
  }

  public void setSimulationData(SimulationData simulationData)
  {
    this.simulationData = simulationData;
  }

  public String getReferenceId()
  {
    return referenceId;
  }

  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }

  public String getCategory()
  {
    return category;
  }

  public void setCategory(String category)
  {
    this.category = category;
  }

  public String getPolicyDescriptor()
  {
    return policyDescriptor;
  }

  public void setPolicyDescriptor(String policyDescriptor)
  {
    this.policyDescriptor = policyDescriptor;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("HouseholdData");
    sb.append("{id=").append(id);
    sb.append(", referenceId='").append(referenceId).append('\'');
    sb.append(", category='").append(category).append('\'');
    sb.append(", policyDescriptor='").append(policyDescriptor).append('\'');
    sb.append(", simulationData=").append(simulationData);
    sb.append('}');
    return sb.toString();
  }
}
