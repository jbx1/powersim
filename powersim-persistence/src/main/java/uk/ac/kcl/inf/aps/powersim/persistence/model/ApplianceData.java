package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:50
 */
@Entity
@Table(name="appliances")
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
}
