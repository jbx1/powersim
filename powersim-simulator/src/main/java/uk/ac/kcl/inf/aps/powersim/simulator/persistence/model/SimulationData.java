package uk.ac.kcl.inf.aps.powersim.simulator.persistence.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 11:50
 */

@Entity
@Table(name = "simulations")
public class SimulationData implements Serializable
{
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  /**
   * The actual timestamp when the simulation started
   */
  private Date actualStartTime;

  /**
   * The actual timestamp when the simulation ended
   */
  private Date actualEndTime;

  /**
   * The start time of the simulated period
   */
  private Date simulatedStartTime;

  /**
   * The end time of the simulated period
   */
  private Date simulatedEndTime;


  public Long getId()
  {
    return id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Date getActualStartTime()
  {
    return actualStartTime;
  }

  public void setActualStartTime(Date actualStartTime)
  {
    this.actualStartTime = actualStartTime;
  }

  public Date getActualEndTime()
  {
    return actualEndTime;
  }

  public void setActualEndTime(Date actualEndTime)
  {
    this.actualEndTime = actualEndTime;
  }

  public Date getSimulatedStartTime()
  {
    return simulatedStartTime;
  }

  public void setSimulatedStartTime(Date simulatedStartTime)
  {
    this.simulatedStartTime = simulatedStartTime;
  }

  public Date getSimulatedEndTime()
  {
    return simulatedEndTime;
  }

  public void setSimulatedEndTime(Date simulatedEndTime)
  {
    this.simulatedEndTime = simulatedEndTime;
  }
}
