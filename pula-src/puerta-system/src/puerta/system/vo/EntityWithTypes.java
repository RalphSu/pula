/**
 * Created on 2008-12-23 11:54:35
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
public class EntityWithTypes<T, E> {

	private T entity;
	private List<E> types;

	/**
	 * @param bt
	 * @param loadByIndexNo
	 */
	public EntityWithTypes(T bt, List<E> loadByIndexNo) {
		this.entity = bt;
		this.types = loadByIndexNo;
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

}
