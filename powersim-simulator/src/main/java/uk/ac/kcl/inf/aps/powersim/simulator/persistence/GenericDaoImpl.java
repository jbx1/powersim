package uk.ac.kcl.inf.aps.powersim.simulator.persistence;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Josef Bajada &lt;josef.bajada@kcl.ac.uk&gt;
 *         Date: 17/10/12
 *         Time: 16:02
 */
public abstract class GenericDaoImpl<T> implements GenericDao<T>
{
  @PersistenceContext
  protected EntityManager em;

  private Class<T> type;

  public GenericDaoImpl()
  {
    Type t = getClass().getGenericSuperclass();
    ParameterizedType pt = (ParameterizedType) t;
    type = (Class) pt.getActualTypeArguments()[0];
  }

  @Override
  @Transactional
  public T create(final T t) {
    this.em.persist(t);
    return t;
  }

  @Override
  @Transactional
  public void delete(final Object id) {
    this.em.remove(this.em.getReference(type, id));
  }

  @Override
  @Transactional
  public T find(final Object id) {
    return (T) this.em.find(type, id);
  }

  @Override
  @Transactional
  public T update(final T t) {
    return this.em.merge(t);
  }
}