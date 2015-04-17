/**
 * Created on 2007-6-4 10:18:59
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.dao;

import java.util.List;

import puerta.support.dao.BaseDao;
import puerta.system.po.ActorShortcut;

/**
 * 
 * @author tiyi
 * 
 */
public interface ActorShortcutDao extends BaseDao<ActorShortcut, String> {

	/**
	 * @param id
	 * @returns
	 */
	List<ActorShortcut> loadByActor(String id);

	/**
	 * @param shortcutId
	 * @param id
	 */
	void save(String[] shortcutId, String id);

	void doClearByActor(String id);

}
