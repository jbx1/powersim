package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;
import uk.ac.kcl.inf.aps.powersim.persistence.model.TimeslotData;
import uk.ac.kcl.inf.aps.powersim.persistence.model.TimeslotDataDao;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:09
 */
@Repository("timeslotDataDao")
public class TimeslotDataDaoImpl extends GenericDaoImpl<TimeslotData> implements TimeslotDataDao
{

}
