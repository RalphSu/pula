/**
 * Created on 2006-12-7 上午01:19:22
 *
 * DiagCN.COM 2004-2006
 * $Id: BusiInspecter.java,v 1.2 2006/12/08 08:38:41 tiyi Exp $
 */
package puerta.support.dao;

/**
 * 
 * @author tiyi
 * 
 */
public interface DomainInspector<T> {
	public abstract void afterRemove(T obj);

	/**
	 * @param obj
	 */
	public abstract void afterEnable(T obj, boolean enable);

	/**
	 * @param obj
	 */
	public abstract void afterVisible(T obj, boolean visible);
}
