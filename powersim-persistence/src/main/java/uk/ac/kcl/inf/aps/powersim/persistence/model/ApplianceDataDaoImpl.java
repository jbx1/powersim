package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:16
 */
@Repository("applianceDataDao")
public class ApplianceDataDaoImpl extends GenericDaoImpl<ApplianceData> implements ApplianceDataDao
{
  @Transactional
  @Override
  public void createBulk(Iterable<ApplianceData> applianceDataList)
  {
    for (ApplianceData applianceData : applianceDataList)
    {
      em.persist(applianceData);
    }

    em.flush();
  }
}
