/**
 * Created on 2009-8-7
 * WXL 2009
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import puerta.support.mls.Mls;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.PurviewToRequestUriDao;
import puerta.system.dao.RequestUriDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Purview;
import puerta.system.po.PurviewToRequestUri;
import puerta.system.po.RequestUri;
import puerta.system.transfer.PurviewToRequestUriTransfer;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class PurviewToRequestUriDaoImpl extends
		HibernateGenericDao<PurviewToRequestUri, String> implements
		PurviewToRequestUriDao {

	@Resource
	private RequestUriDao sysUrlDao;
	@Resource
	private PurviewDao purviewDao;
	@Resource
	private LoggerDao loggerDao;
	@Resource
	private Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM PurviewToRequestUri";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<PurviewToRequestUri> doExports(String asNo) {
		List<PurviewToRequestUri> ls = null;

		if (StringUtils.isEmpty(asNo)) {
			String hql = "SELECT u FROM PurviewToRequestUri u"
					+ " ORDER BY u.purview.treePath,u.requestUri.uri";

			ls = find(hql);
		} else {
			String hql = "select u from PurviewToRequestUri u where u.purview.appField.no=? order by u.purview.treePath,u.requestUri.uri";
			ls = find(hql, asNo);
		}

		for (PurviewToRequestUri act : ls) {
			getHibernateTemplate().initialize(act.getRequestUri());
			getHibernateTemplate().initialize(act.getPurview());
		}

		return ls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<PurviewToRequestUri> objs, boolean update) {

		for (Iterator<PurviewToRequestUri> iter = objs.iterator(); iter
				.hasNext();) {
			PurviewToRequestUri pma = iter.next();
			RequestUri ma = sysUrlDao.get(pma.getRequestUri());
			if (ma == null) {
				// logger.debug("ma is null :" + ma.getActionName());
				continue;
			}
			Purview p = purviewDao.findByNo(pma.getPurview().getNo());
			if (p == null) {
				continue;
			}
			if (exists(ma.getId(), p.getId())) {
				continue;
			}

			pma.setRequestUri(ma);
			pma.setPurview(p);

			getHibernateTemplate().save(pma);

			logger.debug("id=========" + pma.getId());
		}

		// batch update
		String hql = "update RequestUri u set assignedCount=(select count(c.id) from PurviewToRequestUri c where c.requestUri.id=u.id) ";
		updateBatch(hql);

		loggerDao.doLog(mls.t("platform.mappedaction.import"),
				mls.t("platform.mappedaction.count") + objs.size());

	}

	/**
	 * @param maId
	 * @param pid
	 * @return
	 */
	private boolean exists(String maId, String pid) {
		String hql = "SELECT id FROM PurviewToRequestUri WHERE purview.id=? AND requestUri.id=?";
		return (findSingle(hql, pid, maId) != null);
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
	public JDOMTransfer<PurviewToRequestUri> getTransfer() {
		return new PurviewToRequestUriTransfer();
	}

	@Override
	public List<RequestUri> getAssigned(String id) {
		String hql = "select u.requestUri FROM PurviewToRequestUri u WHERE u.purview.id=? order by u.requestUri.uri ";
		return find(hql, id);
	}

	@Override
	public void save(String purviewId, String[] uriId, boolean append) {

		if (uriId == null)
			return;
		Purview pu = new Purview();
		pu.setId(purviewId);
		for (String id : uriId) {
			if (append) {
				if (existsRelation(purviewId, id))
					continue;
			}
			PurviewToRequestUri p = new PurviewToRequestUri();
			p.setPurview(pu);
			p.setRequestUri(RequestUri.create(id));
			getHibernateTemplate().save(p);
		}

		for (String id : uriId) {
			updateAssignedCount(id);
		}

	}

	private boolean existsRelation(String purviewId, String id) {
		String hql = "select id from PurviewToRequestUri where purview.id=? AND requestUri.id=?";
		return findSingle(hql, purviewId, id) != null;

	}

	private void updateAssignedCount(String id) {
		String hql = "select count(id) from PurviewToRequestUri where requestUri.id=?";
		int c = getCountByQuery(hql, id);
		hql = "update RequestUri set assignedCount=?,onlineCheck=?,purviewCheck=? WHERE id=?";
		updateBatch(hql, c, true, true, id);
	}

	@Override
	public Map<String, Integer> getAssigned() {
		String hql = "select purview.id,count(id) from PurviewToRequestUri group by purview.id";
		Map<String, Integer> map = WxlSugar.newHashMap();
		List<Object[]> objsList = find(hql);
		for (Object[] objs : objsList) {
			String id = (String) objs[0];
			Long n = (Long) objs[1];
			map.put(id, n.intValue());
		}
		return map;
	}

	@Override
	public void clear(String purviewId) {
		String hql = "delete from PurviewToRequestUri where purview.id=?";
		delete(hql, purviewId);
	}

	@Override
	public boolean canAccess(RequestUri ma, String id) {
		String hql = "SELECT s.id FROM PurviewToRequestUri as s, ActorPurview ap WHERE (s.requestUri.id=?) AND s.purview.id=ap.purview.id AND ap.actorId=?";
		String aid = findSingle(hql, ma.getId(), id);
		if (aid == null) {
			return false;
		}
		return true;
	}

	@Override
	public void doImportLocal(List<PurviewToRequestUri> ptrs) {
		String sql = "select id from PurviewToRequestUri where purview.id=? and requestUri.id=?";
		for (PurviewToRequestUri p : ptrs) {
			String id = purviewDao.getIdByNo(p.getPurview().getNo());
			if (id == null) {
				continue;
			}

			// check exists ,if exists skip
			if (exists(sql, id, p.getRequestUri().getId())) {
				continue;
			}

			p.setPurview(Purview.create(id));
			getHibernateTemplate().save(p);
		}

	}

}
