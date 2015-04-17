/**
 * Created on 2007-6-4 10:27:10
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.ActorShortcutDao;
import puerta.system.po.ActorShortcut;
import puerta.system.po.Shortcut;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ActorShortcutDaoImpl extends
		HibernateGenericDao<ActorShortcut, String> implements ActorShortcutDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.actorshortcut.ActorShortcutMgr#loadByActor
	 * (java.lang.String)
	 */

	public List<ActorShortcut> loadByActor(String id) {
		String hql = "SELECT u FROM ActorShortcut u JOIN FETCH u.shortcut WHERE u.actorId=? ORDER BY u.indexNo";

		return find(hql, id);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.actorshortcut.ActorShortcutMgr#save(java
	 * .lang.String[], java.lang.String)
	 */
	public void save(String[] shortcutId, String id) {
		// clearAll at first
		doClearByActor(id);

		if (shortcutId == null)
			return;

		for (int i = 0; i < shortcutId.length; i++) {
			String scId = shortcutId[i];
			ActorShortcut asc = new ActorShortcut();
			asc.setShortcut(new Shortcut());
			asc.getShortcut().setId(scId);
			asc.setIndexNo(i);
			asc.setActorId(id);

			getHibernateTemplate().save(asc);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.actorshortcut.ActorShortcutMgr#doClearByActor
	 * (java.lang.String)
	 */
	public void doClearByActor(String id) {
		String hql = "DELETE FROM ActorShortcut WHERE actorId=?";
		super.delete(hql, id);
	}

}
