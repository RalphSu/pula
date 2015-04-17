/**
 * Created on 2007-6-3 11:25:58
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.mls.Mls;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.ShortcutCondition;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.ShortcutDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Purview;
import puerta.system.po.Shortcut;
import puerta.system.transfer.ShortcutTransfer;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ShortcutDaoImpl extends HibernateGenericDao<Shortcut, String>
		implements ShortcutDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private PurviewDao purviewDao;
	@Resource
	private Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.nilnut.groundwork.platform.shortcut.ShortcutDao#save(com.nilnut.
	 * groundwork.platform.po.Shortcut)
	 */
	public Shortcut save(Shortcut shortcut) {
		Map<String, Object> conditions = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();
		conditions.put("no", shortcut.getNo());
		// f.put("id", p.getId());
		boolean exists = exists(conditions, f);
		if (exists) {

			Mls.raise("platform.shortcut.noExists", shortcut.getNo());
		}

		getHibernateTemplate().save(shortcut);
		return shortcut;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#loadByPurview(java
	 * .lang.String)
	 */

	public List<Shortcut> loadByPurview(String purviewId) {
		String hql = "FROM Shortcut u WHERE u.removed=? and u.purview.id=? ORDER BY u.indexNo";
		return find(hql, false, purviewId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#search(com.nilnut
	 * .groundwork.platform.shortcut.ShortcutCondition,
	 * com.nilnut.groundwork.commonPageInfo)
	 */
	public PaginationSupport<Shortcut> search(ShortcutCondition condition,
			int pageNo) {

		DetachedCriteria dc = makeDetachedCriteria(condition);

		return super.findPageByCriteria(dc, new PageInfo(pageNo),
				Order.desc("eventTime "));

	}

	public DetachedCriteria makeDetachedCriteria(ShortcutCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		if (!StringUtils.isEmpty(condition.getPurviewName())) {
			dc.add(Restrictions.like("purview.name",
					condition.getPurviewName(), MatchMode.ANYWHERE));
		}

		if (!StringUtils.isEmpty(condition.getPurviewNo())) {
			dc.add(Restrictions.like("purview.no", condition.getPurviewNo(),
					MatchMode.ANYWHERE));
		}

		return dc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#getMaxIndexNo(java
	 * .lang.String)
	 */
	public int getMaxIndexNo(String purviewId) {
		String hql = "SELECT MAX(u.indexNo) FROM Shortcut u WHERE"
				+ " u.removed=? AND u.purview.id=? ";

		Integer n = (Integer) getHibernateTemplate().iterate(hql,
				new Object[] { false, purviewId }).next();

		if (n == null) {
			return 0;
		} else {
			return n.intValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#doAppend(java.util
	 * .List)
	 */
	public void doAppend(List<Shortcut> shortcuts) {
		// BusiLogger.disableLog();
		for (Iterator<Shortcut> iter = shortcuts.iterator(); iter.hasNext();) {
			Shortcut r = iter.next();
			Shortcut po = findByNo(r.getNo());
			Purview p = purviewDao.findByNo(r.getPurview().getNo());

			r.setPurview(p);
			if (po != null) {
				r.setId(po.getId());
				update(r);
			} else {
				save(r);
			}
		}

		// BusiLogger.enableLog();
		loggerDao.doLog(mls.t("platform.shortcut.importLog"),
				mls.t("platform.shortcut.total") + shortcuts.size());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nilnut.groundwork.platform.shortcut.ShortcutDao#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM ActorShortcut";
		super.delete(hql);

		hql = "DELETE FROM Shortcut ";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#doImport(java.util
	 * .List)
	 */
	public void doImport(List<Shortcut> shortcuts) {

		for (Iterator<Shortcut> iter = shortcuts.iterator(); iter.hasNext();) {
			Shortcut r = iter.next();
			Purview p = purviewDao.findByNo(r.getPurview().getNo());
			r.setPurview(p);
			save(r);

		}

		loggerDao.doLog(mls.t("platform.shortcut.importLog"),
				mls.t("platform.shortcut.total") + shortcuts.size());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nilnut.groundwork.platform.shortcut.ShortcutDao#loadAll()
	 */
	public List<Shortcut> loadAll() {
		String hql = "FROM Shortcut p left outer join fetch p.purview WHERE p.removed=? ORDER BY p.indexNo,p.no";
		return find(hql, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#update(com.nilnut
	 * .groundwork.platform.po.Shortcut)
	 */
	public Shortcut update(Shortcut shortcut) {
		Map<String, Object> conditions = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();
		conditions.put("no", shortcut.getNo());
		f.put("id", shortcut.getId());
		boolean exists = exists(conditions, f);
		if (exists) {
			Mls.raise("platform.shortcut.noExists", shortcut.getNo());
		}

		Shortcut po = findById(shortcut.getId());

		po.setIndexNo(shortcut.getIndexNo());
		po.setName(shortcut.getName());
		po.setNo(shortcut.getNo());
		po.setPurview(shortcut.getPurview());
		po.setUrl(shortcut.getUrl());
		// getHibernateTemplate().save(shortcut);
		return shortcut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.shortcut.ShortcutDao#loadByActor(com.nilnut
	 * .groundwork.platform.po.ApplicableScope, java.lang.String)
	 */
	public Map<String, Shortcut> loadByActor(String id) {
		String hql = "SELECT u FROM Shortcut u , ActorPurview p WHERE "
				+ "p.purview.id=u.purview.id AND p.actorId=? ORDER BY p.purview.treePath,u.indexNo";

		// logger.debug(hql);
		List<Shortcut> l = find(hql, id);
		Map<String, Shortcut> map = new LinkedHashMap<String, Shortcut>();
		for (Iterator<Shortcut> iter = l.iterator(); iter.hasNext();) {
			Shortcut sc = (Shortcut) iter.next();
			map.put(sc.getId(), sc);
		}

		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<Shortcut> doExports(String afNo) {
		return loadAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<Shortcut> objs, boolean update) {
		this.doImport(objs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getPluginName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<Shortcut> getTransfer() {
		return new ShortcutTransfer();
	}
}
