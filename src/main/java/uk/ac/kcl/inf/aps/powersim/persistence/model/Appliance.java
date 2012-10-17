package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:50
 */
@Entity
public class Appliance implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private String type;

  @ManyToOne(optional = false)
  private Household household;

  private Boolean generator = false;

  public Appliance(String type, Household household, Boolean generator)
  {
    this.type = type;
    this.household = household;
    this.generator = generator;
  }

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

  public Household getHousehold()
  {
    return household;
  }

  public void setHousehold(Household household)
  {
    this.household = household;
  }

  public Boolean getGenerator()
  {
    return generator;
  }

  public void setGenerator(Boolean generator)
  {
    this.generator = generator;
  }
}
