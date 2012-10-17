package uk.ac.kcl.inf.aps.powersim.persistence.model;

import org.springframework.stereotype.Repository;
import uk.ac.kcl.inf.aps.powersim.persistence.GenericDaoImpl;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:20
 */
@Repository("householdDao")
public class HouseholdDaoImpl extends GenericDaoImpl<Household> implements HouseholdDao
{
}
