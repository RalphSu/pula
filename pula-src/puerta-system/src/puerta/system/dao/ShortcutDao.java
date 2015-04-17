/**
 * Created on 2007-6-3 09:36:34
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.ShortcutCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.Shortcut;

/**
 * 
 * @author tiyi
 * 
 */
public interface ShortcutDao extends BaseDao<Shortcut, String>,
		IWxlPluginSetting<Shortcut> {

	/**
	 * @param shortcut
	 * @return
	 */
	Shortcut save(Shortcut shortcut);

	Shortcut update(Shortcut shortcut);

	/**
	 * @param purviewId
	 * @return
	 */
	List<Shortcut> loadByPurview(String purviewId);

	/**
	 * @param condition
	 * @param pageInfo
	 * @return
	 */
	PaginationSupport<Shortcut> search(ShortcutCondition condition, int pageNo);

	/**
	 * @param purviewId
	 * @return
	 */
	int getMaxIndexNo(String purviewId);

	/**
	 * @return
	 */
	List<Shortcut> loadAll();

	/**
	 * 
	 */
	void doClear();

	/**
	 * @param shortcuts
	 */
	void doImport(List<Shortcut> shortcuts);

	/**
	 * @param shortcuts
	 */
	void doAppend(List<Shortcut> shortcuts);

	/**
	 * @param scope
	 * @param actorId
	 * @return
	 */
	Map<String, Shortcut> loadByActor(String actorId);

}
