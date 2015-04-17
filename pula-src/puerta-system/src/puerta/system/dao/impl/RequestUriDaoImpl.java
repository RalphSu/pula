package puerta.system.dao.impl;

import java.util.Iterator;
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
import puerta.support.utils.RowReader;
import puerta.support.utils.StringTool;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.RequestUriCondition;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.RequestUriDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Purview;
import puerta.system.po.PurviewToRequestUri;
import puerta.system.po.RequestUri;
import puerta.system.transfer.RequestUriTransfer;

@Repository
public class RequestUriDaoImpl extends HibernateGenericDao<RequestUri, String>
		implements RequestUriDao {

	@Resource
	private PurviewDao purviewDao;
	@Resource
	private LoggerDao loggerDao;
	@Resource
	private Mls mls;

	public RequestUri save(RequestUri requestUri) {
		getHibernateTemplate().save(requestUri);
		return requestUri;
	}

	public RequestUri update(RequestUri requestUri) {
		RequestUri po = findById(requestUri.getId());

		po.setOnlineCheck(requestUri.isOnlineCheck());
		po.setPurviewCheck(requestUri.isPurviewCheck());
		po.setUri(requestUri.getUri());
		return po;
	}

	public List<RequestUri> loadActionsByPurview(String purviewId) {
		String hql = "SELECT u.requestUri FROM PurviewToRequestUri u"
				+ " WHERE u.purview.id=? ORDER BY u.requestUri.actionName,u.requestUri.method";

		return find(hql, new Object[] { purviewId });
	}

	public List<RequestUri> loadAll() {
		String hql = "FROM RequestUri u ORDER BY u.uri ";
		return find(hql);
	}

	public void savePurviewToRequestUri(String purviewId, String[] id) {
		String hql = "DELETE FROM PurviewToRequestUri p WHERE p.purview.id=?";
		super.delete(hql, new Object[] { purviewId });

		if (id == null)
			return;
		Purview p = purviewDao.findById(purviewId);
		for (int i = 0; i < id.length; i++) {
			RequestUri ma = this.findById(id[i]);
			PurviewToRequestUri pm = new PurviewToRequestUri();
			pm.setPurview(p);
			pm.setRequestUri(ma);
			getHibernateTemplate().save(pm);

			// update
			updateMappedAcount(ma);
		}

	}

	private void updateMappedAcount(RequestUri ma) {
		String hql = "SELECT COUNT(u) FROM PurviewToRequestUri u WHERE u.requestUri.id=?";
		int c = super.getCountByQuery(hql, new Object[] { ma.getId() });

		ma.setAssignedCount(c);
		ma.setPurviewCheck(true);

		getHibernateTemplate().update(ma);

	}

	public void doClearAll() {

		String hql = "DELETE FROM RequestUri ";
		super.delete(hql);

	}

	public void doImport(List<RequestUri> actions) {
		String hql = "FROM RequestUri WHERE uri=?";
		for (Iterator<RequestUri> iter = actions.iterator(); iter.hasNext();) {
			RequestUri element = iter.next();
			RequestUri ma = findSingle(hql, element.getUri());
			if (ma != null) {
				ma.setPurviewCheck(element.isPurviewCheck());
				ma.setOnlineCheck(element.isOnlineCheck());
			} else {
				getHibernateTemplate().save(element);
			}

		}

	}

	public PaginationSupport<RequestUri> search(RequestUriCondition condition,
			int pageNo) {

		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("uri"));

	}

	/**
	 * @param condition
	 * @return
	 */
	private DetachedCriteria makeDetachedCriteria(RequestUriCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(RequestUri.class);

		if (!StringUtils.isEmpty(condition.getUri())) {
			dc.add(Restrictions.like("uri", condition.getUri(),
					MatchMode.ANYWHERE));
		}

		if (condition.isForceNoOnline()) {
			dc.add(Restrictions.eq("onlineCheck", false));
		}
		if (condition.isForceNoPurview()) {
			dc.add(Restrictions.eq("purviewCheck", false));
		}
		if (condition.getAssignedCount() >= 0) {
			dc.add(Restrictions.eq("assignedCount",
					condition.getAssignedCount()));
		}
		return dc;
	}

	public void afterEnable(RequestUri obj, boolean ena) {
		// TODO Auto-generated method stub

	}

	public void afterRemove(RequestUri obj) {
		RequestUri m = (RequestUri) obj;

		loggerDao.doLog(mls.t("platform.requesturi.remove"),
				StringTool.getBean(m));

	}

	public void afterVisible(RequestUri obj, boolean vis) {
		// TODO Auto-generated method stub

	}

	public PurviewDao getPurviewDao() {
		return purviewDao;
	}

	public void setPurviewDao(PurviewDao purviewDao) {
		this.purviewDao = purviewDao;
	}

	public List<Purview> loadPurviewsByAction(String actionId) {
		String hql = "SELECT u.purview FROM PurviewToRequestUri u"
				+ " WHERE u.requestUri.id=? ORDER BY u.purview.treePath";

		return find(hql, new Object[] { actionId });
	}

	public List<RequestUri> loadAllPurviewCheck(String contextPath) {
		String hql = "FROM RequestUri u WHERE u.purviewCheck=? AND namespace like ? "
				+ "ORDER BY u.actionName,u.method ";
		return find(hql, new Object[] { true, contextPath + "%" });
	}

	public void doPurviewCheck(String[] objId, boolean purviewCheck) {
		if (objId == null)
			return;

		for (int i = 0; i < objId.length; i++) {
			RequestUri ma = findById(objId[i]);

			ma.setPurviewCheck(purviewCheck);

			getHibernateTemplate().update(ma);
		}

	}

	public void doOnlineCheck(String[] objId, boolean onlineCheck) {
		if (objId == null)
			return;

		for (int i = 0; i < objId.length; i++) {
			RequestUri ma = findById(objId[i]);

			ma.setOnlineCheck(onlineCheck);

			getHibernateTemplate().update(ma);
		}

	}

	public List<PurviewToRequestUri> loadAllMapped() {
		String hql = "SELECT u FROM PurviewToRequestUri u"
				+ " ORDER BY u.requestUri.actionName,u.requestUri.method";

		return find(hql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.groundwork.platform.requesturi.RequestUriDao#doImportAssign
	 * (java.util.List)
	 */
	public void doImportAssign(List<PurviewToRequestUri> purviewToRequestUris) {
		for (Iterator<PurviewToRequestUri> iter = purviewToRequestUris
				.iterator(); iter.hasNext();) {
			PurviewToRequestUri pma = iter.next();
			RequestUri ma = this.get(pma.getRequestUri());
			if (ma == null) {
				// logger.debug("ma is null :" + ma.getActionName());
				continue;
			}
			Purview p = purviewDao.findByNo(pma.getPurview().getNo());

			pma.setRequestUri(ma);
			pma.setPurview(p);

			getHibernateTemplate().save(pma);
		}

		loggerDao.doLog(
				mls.t("platform.requesturi.import"),
				mls.t("platform.requesturi.count")
						+ purviewToRequestUris.size());

	}

	/**
	 * @param requestUri
	 * @return
	 */
	public RequestUri get(RequestUri requestUri) {
		String hql = "FROM RequestUri u WHERE u.uri=?";
		return findSingle(hql, requestUri.getUri());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.requesturi.RequestUriDao#doClearByPurview
	 * (int)
	 */
	public void doClearByPurviewType(int type) {
		String hql = "SELECT u FROM PurviewToRequestUri u"
				+ " WHERE u.purview.type=? ";
		List<PurviewToRequestUri> l = find(hql, type);
		logger.debug("assign purviews.size=" + l.size());
		for (Iterator<PurviewToRequestUri> iter = l.iterator(); iter.hasNext();) {
			getHibernateTemplate().delete(iter.next());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nilnut.groundwork.platform.requesturi.RequestUriDao#find(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	// public RequestUri find(String actionName, String ns, String method) {
	//
	// String hql = "FROM RequestUri u WHERE u.actionName=?";
	// List<RequestUri> l = find(hql, new Object[] { actionName });
	// RequestUri all_match = null, namespace_Match = null, method_Match = null;
	// for (Iterator<RequestUri> iter = l.iterator(); iter.hasNext();) {
	// RequestUri ma = (RequestUri) iter.next();
	// if (ma.isAllMethod() && ma.isAllNamespace()) {
	// all_match = ma;
	// } else if (ma.getNamespace().equals(ns)
	// && method.equals(ma.getMethod())) {
	// method_Match = ma;
	// } else if (ma.isAllMethod() && ma.getNamespace().equals(ns)) {
	// namespace_Match = ma;
	// }
	// }
	//
	// if (method_Match != null) {
	// return method_Match;
	// }
	// if (namespace_Match != null) {
	// return namespace_Match;
	// }
	//
	// return all_match;
	//
	// // String hql = "FROM RequestUri u WHERE u.actionName=? and
	// // u.namespace=? AND u.method=?";
	// // List l = getHibernateTemplate().find(hql,
	// // new Object[] { actionName, ns, method });
	// // if (l.size() <= 0) {
	// // // find next
	// // hql = "FROM RequestUri u WHERE u.actionName=? and u.namespace=? AND
	// // u.method=?";
	// // l = getHibernateTemplate().find(hql,
	// // new Object[] { actionName, ns, RequestUri.ALL_METHOD });
	// //
	// // if (l.size() <= 0) {
	// // return null;
	// // } else {
	// // return (RequestUri) l.get(0);
	// // }
	// // } else {
	// // return (RequestUri) l.get(0);
	// // }
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nilnut.groundwork.platform.requesturi.RequestUriDao#doAppend(
	 * java.util.List)
	 */
	public void doAppend(List<RequestUri> actions) {
		this.doImport(actions);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.requesturi.RequestUriDao#doAppendAssign
	 * (java.util.List)
	 */
	public void doAppendAssign(List<PurviewToRequestUri> purviewToRequestUris) {
		this.doImportAssign(purviewToRequestUris);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {
		this.doClearAll();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doExports()
	 */
	public List<RequestUri> doExports(String afNo) {
		return this.loadAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<RequestUri> objs, boolean update) {
		this.doImport(objs);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#getName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<RequestUri> getTransfer() {
		return new RequestUriTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.requesturi.RequestUriDao#get(java.lang.String,
	 * java.lang.String)
	 */
	public RequestUri get(String namespace, String actionName) {
		String hql = "FROM RequestUri u WHERE u.namespace=? AND u.actionName=?";
		return findSingle(hql, namespace, actionName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.requesturi.RequestUriDao#loadAllMappedCount(java
	 * .lang.String)
	 */
	public Map<String, Integer> loadAllMappedCount(String contextPath) {
		String hql = "select p.id,count(u) FROM PurviewToRequestUri pta,Purview p, RequestUri u WHERE u.id=pta.requestUri.id AND"
				+ " u.purviewCheck=? AND namespace like ? AND p.id=pta.purview.id "
				+ " GROUP BY p.id";
		List<Object[]> objsList = find(hql, new Object[] { true,

		contextPath + "%" });
		Map<String, Integer> ps = WxlSugar.newHashMap();
		RowReader rr = new RowReader(objsList);
		while (rr.hasNextRow()) {
			String pid = rr.next();
			Long count = rr.next();
			ps.put(pid, count.intValue());
			rr.nextRow();
		}
		return ps;
	}

	@Override
	public List<RequestUri> getAll(String prefix) {
		logger.debug("prefix=" + prefix);
		String hql = "From RequestUri where uri like ? order by uri";
		return find(hql, prefix + "%");
	}

	@Override
	public RequestUri get(String uri) {
		String hql = "from RequestUri where uri=?";
		return findSingle(hql, uri);
	}

	@Override
	public List<RequestUri> getPurviewCheck(String prefix) {
		logger.debug("prefix=" + prefix);
		String hql = "From RequestUri where uri like ? AND purviewCheck=? order by uri";
		return find(hql, prefix + "%", true);
	}

	@Override
	public void doImportLocal(List<RequestUri> actions) {
		String hql = "FROM RequestUri WHERE uri=?";
		for (Iterator<RequestUri> iter = actions.iterator(); iter.hasNext();) {
			RequestUri element = iter.next();
			RequestUri ma = findSingle(hql, element.getUri());
			if (ma != null) {
				// 2013 0919 本地最大
				ma.setPurviewCheck(element.isPurviewCheck());
				ma.setOnlineCheck(element.isOnlineCheck());
				element.setId(ma.getId());
			} else {
				getHibernateTemplate().save(element);
			}

		}

	}

}
