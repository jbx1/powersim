package uk.ac.kcl.inf.aps.powersim.simulator.persistence;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:02
 */
public interface GenericDao<T>
{
  T create(T t);

  void delete(Object id);

  T find(Object id);

  T update(T t);
}