/**
 * Created on 2008-12-22 09:23:25
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

/**
 * 
 * @author tiyi
 * 
 */
public class EntityWithIndex<T> {

	T entity;
	int indexNo;

	public EntityWithIndex(T obj, int m) {
		this.entity = obj;
		this.indexNo = m;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int index) {
		this.indexNo = index;
	}
}
