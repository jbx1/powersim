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
                @Index(name = "appliances_household_id_idx", columnNames = "household_id")
        })
@NamedQueries({
        @NamedQuery(name = "ApplianceData.countForSimulation",
                query = "select count(a) from ApplianceData a where a.householdData.simulationData.id=:simulationId"),
        @NamedQuery(name="ApplianceData.deleteBySimulationId",
                query = "delete from ApplianceData a where a.householdData.simulationData.id = :simulationId")

})
public class ApplianceData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String referenceId;

  private String type;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name="household_id")
  @OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
  private HouseholdData householdData;

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

  public HouseholdData getHouseholdData()
  {
    return householdData;
  }

  public void setHouseholdData(HouseholdData householdData)
  {
    this.householdData = householdData;
  }

  public String getReferenceId()
  {
    return referenceId;
  }

  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("ApplianceData");
    sb.append("{id=").append(id);
    sb.append(", referenceId='").append(referenceId).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append(", householdData=").append(householdData);
    sb.append('}');
    return sb.toString();
  }
}
