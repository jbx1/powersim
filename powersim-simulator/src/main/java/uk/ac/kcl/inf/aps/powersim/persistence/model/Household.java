package uk.ac.kcl.inf.aps.powersim.persistence.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 12:44
 */
@Entity
public class Household implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

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

  public Simulation getSimulation()
  {
    return simulation;
  }

  public void setSimulation(Simulation simulation)
  {
    this.simulation = simulation;
  }
}
