/**
 * Created on 2008-12-23 11:46:01
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

import java.util.List;

/**
 * 
 * @author tiyi
 * 
 */
public class EntityWithTypeAndIndex<T, E> {
	T entity;
	List<E> types;
	int indexNo;

	/**
	 * @param bt
	 * @param loadByIndexNo
	 * @param maxIndexNo
	 */
	public EntityWithTypeAndIndex(T bt, List<E> loadByIndexNo, int maxIndexNo) {
		this.entity = bt;
		this.types = loadByIndexNo;
		this.indexNo = maxIndexNo;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public List<E> getTypes() {
		return types;
	}

	public void setTypes(List<E> types) {
		this.types = types;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}
}
