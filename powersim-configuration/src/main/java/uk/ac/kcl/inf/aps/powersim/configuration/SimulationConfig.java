package uk.ac.kcl.inf.aps.powersim.configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 22/11/12
 *         Time: 14:32
 */
public final class SimulationConfig
{
  String name;
  Date simulatedStartTime;
  int durationMins;
  int granularityMins;

  List<String> policies = new ArrayList<>();

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Date getSimulatedStartTime()
  {
    return simulatedStartTime;
  }

  public void setSimulatedStartTime(Date simulatedStartTime)
  {
    this.simulatedStartTime = simulatedStartTime;
  }

  public int getDurationMins()
  {
    return durationMins;
  }

  public void setDurationMins(int durationMins)
  {
    this.durationMins = durationMins;
  }

  public int getGranularityMins()
  {
    return granularityMins;
  }

  public void setGranularityMins(int granularityMins)
  {
    this.granularityMins = granularityMins;
  }

  public List<String> getPolicies()
  {
    return policies;
  }

  public void setPolicies(List<String> policies)
  {
    this.policies = policies;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    sb.append("SimulationConfig");
    sb.append("{name='").append(name).append('\'');
    sb.append(", simulatedStartTime=").append(simulatedStartTime);
    sb.append(", durationMins=").append(durationMins);
    sb.append(", granularityMins=").append(granularityMins);
    sb.append(", policies=").append(policies);
    sb.append('}');
    return sb.toString();
  }
}
