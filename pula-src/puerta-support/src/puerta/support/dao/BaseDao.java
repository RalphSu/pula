/**
 * Created on 2008-3-21 上午11:37:27
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public interface BaseDao<T, ID extends Serializable> {
	public List<T> loadAll();

	public List<T> loadAllWithoutRemoved();

	public List<T> loadEnabled();

	public <E> List<E> find(String hql, Object... values);

	public T findById(ID id);

	public T findByNo(String no);

	public List<T> findByProperty(String propertyName, Object value);

	public ID _save(T transientInstance);

	public void updateDirectly(T transientInstance);

	void deleteById(ID id);

	void delete(T persistentInstance);

	void deleteById(ID[] objId);

	void doEnable(ID[] objId, boolean enable);

	void doVisible(ID[] objId, boolean visible);

	<E> E findTarget(String target, String where, Object... objs);

	boolean existsNo(String no);

	boolean existsNo(String no, ID filterId);


}
