/**
 * Created on 2008-12-18 02:45:34
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
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
import puerta.system.condition.SysRoleCondition;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.AppFieldDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.dao.SysRoleDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.ActorRole;
import puerta.system.po.AppField;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.po.SysRole;
import puerta.system.transfer.SysRoleTransfer;
import puerta.system.vo.ActorWithPurviewAll;
import puerta.system.vo.EntityWithTypes;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class SysRoleDaoImpl extends HibernateGenericDao<SysRole, String>
		implements SysRoleDao {
	@Resource
	private LoggerDao loggerDao;
	@Resource
	private AppFieldDao appFieldDao;
	@Resource
	private ActorPurviewDao actorPurviewDao;
	@Resource
	private PurviewDao purviewDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#loadSysRoles(java.lang.String)
	 */
	public List<SysRole> loadSysRoles(String id) {
		String hql = "SELECT DISTINCT mm FROM ActorPurview"
				+ " AS am,SysRole mm WHERE "
				+ "am.actorId=? AND am.purview.actorRole.id=mm.id and am.purview.menuItem = ? "
				+ " and mm.removed=? ORDER BY mm.indexNo";

		List<SysRole> list = find(hql, id, Boolean.TRUE, false);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterEnable(java.lang.Object,
	 * boolean)
	 */
	public void afterEnable(SysRole obj, boolean enable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterRemove(java.lang.Object)
	 */
	public void afterRemove(SysRole obj) {
		SysRole m = (SysRole) obj;
		loggerDao.doLog(mls.t("platform.actorRole.remove"),
				StringTool.getBean(m));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterVisible(java.lang.Object,
	 * boolean)
	 */
	public void afterVisible(SysRole obj, boolean visible) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#loadEnabled(java.lang.String)
	 */
	public List<SysRole> loadAll(String no) {
		String hql = "FROM SysRole u WHERE u.appField.no=? AND u.removed=? ORDER BY u.no";
		return find(hql, no, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#save(wxlplatform .po.SysRole)
	 */
	public SysRole save(SysRole m) {
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());

		if (exists(v)) {
			Mls.raise("platform.actorRole.noExists", m.getNo());
		}

		m.setAppField(appFieldDao.findByNo(m.getAppField().getNo()));

		getHibernateTemplate().save(m);
		loggerDao.doLog(mls.t("platform.actorRole.add"), StringTool.getBean(m));
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#search(wxlplatform
	 * .actorRole.SysRoleCondition, int)
	 */
	public PaginationSupport<SysRole> search(SysRoleCondition condition,
			int pageNo) {
		DetachedCriteria dc = makeDetachedCriteria(condition);

		// dc.add(criterion)

		dc.createAlias("appField", "as");

		dc.add(Restrictions.eq("as.no", condition.getAppFieldNo()));

		return super.findPageByCriteria(dc, new PageInfo(pageNo),
				Order.asc("no"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#update(wxlplatform .po.SysRole)
	 */
	public SysRole update(SysRole m) {
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", m.getId());

		if (exists(v, filter)) {
			Mls.raise("platform.actorRole.noExists", m.getNo());
		}

		SysRole po = findById(m.getId());

		po.setNo(m.getNo());
		po.setName(m.getName());

		// boolean diffIndex = po.getIndexNo() != m.getIndexNo();
		// int oldIndex = po.getIndexNo();
		// po.setIndexNo(m.getIndexNo());

		po.setAppField(appFieldDao.findByNo(m.getAppField().getNo()));

		getHibernateTemplate().update(po);

		// 更新权限的索引
		// 批量修改
		// if (diffIndex) {
		// String oldTreePath = "M" + StringTool.fillZero(oldIndex, 3);
		// String newTreePath = "M" + StringTool.fillZero(m.getIndexNo(), 3);
		// logger.debug("oldTree=" + oldTreePath);
		// logger.debug("newTree=" + newTreePath);
		// String hql =
		// "UPDATE Purview SET treePath = Replace ( treePath , ?,?) WHERE actorRole.id=? ";
		// updateBatch(hql, oldTreePath, newTreePath, po.getId());
		// }
		loggerDao
				.doLog(mls.t("platform.actorRole.edit"), StringTool.getBean(m));
		return m;
	}

	public EntityWithTypes<SysRole, AppField> beforeEdit(String id) {
		SysRole m = null;
		if (StringUtils.isEmpty(id)) {

		} else {
			m = findById(id);

			// System.out.println(m.getNo());
			// System.out.println(m.getAppField().getId());
			// System.out.println(m.getAppField().getNo());

			// m.getAppField().getNo()
			getHibernateTemplate().initialize(m.getAppField());
			// getHibernateTemplate().initialize(m.getAppField());
			// actorRoleDao.initialize(m.getAppField());
			// System.out.println(m.getAppField().getNo());
		}
		List<AppField> as = appFieldDao.loadByIndexNo();
		return new EntityWithTypes<SysRole, AppField>(m, as);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM SysRole p";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorRole.SysRoleDao#doImport(java.util.List, boolean)
	 */
	public void doImport(List<SysRole> actorRoles, boolean b) {
		Map<String, List<Purview>> purviews = WxlSugar.newHashMap();
		for (SysRole ar : actorRoles) {
			List<Purview> ps = ar.getPurviews();
			if (b) {
				ar = updateByNoOrSave(ar);
			} else {
				ar = this.save(ar);
				// save purviews
			}
			purviews.put(ar.getId(), ps);

		}
		storePurviews(purviews, b);
	}

	/**
	 * @param purviews
	 * @param notForce
	 */
	private void storePurviews(Map<String, List<Purview>> purviews,
			boolean notForce) {
		for (String id : purviews.keySet()) {
			List<Purview> ps = purviews.get(id);
			if (!notForce) {
				// clear first
				String hql = "DELETE FROM ActorPurview WHERE actorId=?";
				delete(hql, id);

			} else {

			}

			for (Purview p : ps) {
				if (notForce) {
					String hql = "SELECT id FROM ActorPurview WHERE purview.no=? AND actorId=?";
					String rid = findSingle(hql, p.getNo(), id);
					if (rid != null) {
						continue;
					}
				}

				// 直接保存了
				// 但是还是要找到Purview在哪里
				actorPurviewDao.doAssign(id, p.getNo());
			}

		}
	}

	/**
	 * @param next
	 * @return
	 */
	private SysRole updateByNoOrSave(SysRole next) {
		SysRole po = this.findByNo(next.getNo());
		if (po == null) {
			return save(next);
		} else {
			next.setId(po.getId());
			return update(next);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<SysRole> doExports(String afNo) {
		List<SysRole> ret = null;

		if (StringUtils.isEmpty(afNo)) {
			ret = this.loadAll();
		} else {
			ret = this.loadAll(afNo);
		}
		for (SysRole m : ret) {
			getHibernateTemplate().initialize(m.getAppField());
			fillPurviews(m);
		}
		return ret;
	}

	/**
	 * @param m
	 */
	private void fillPurviews(SysRole m) {
		m.setPurviews(actorPurviewDao.loadByActor(m.getId(), m.getAppField()
				.getNo()));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<SysRole> getTransfer() {
		return new SysRoleTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorrole.SysRoleDao#beforeSetupPurview(java.lang.
	 * String)
	 */
	public ActorWithPurviewAll<SysRole> beforeSetupPurview(String id) {

		SysRole ar = this.findById(id);
		List<Purview> purviews = purviewDao.loadAll(ar.getAppField().getNo());
		List<Purview> checkedPurviews = actorPurviewDao.loadByActor(id, ar
				.getAppField().getNo());
		List<Module> module = moduleDao.loadAll(ar.getAppField().getNo());

		return new ActorWithPurviewAll<SysRole>(ar, purviews, checkedPurviews,
				module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorrole.SysRoleDao#loadPurviews(java.lang.String)
	 */
	public List<Purview> loadPurviews(String roleNo) {
		String hql = "SELECT a.purview FROM ActorPurview a,SysRole ar WHERE a.actorId=ar.id AND ar.no=?"
				+ " ORDER BY a.purview.treePath ";
		return find(hql, roleNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorrole.SysRoleDao#hasRole(java.lang.String,
	 * java.lang.String)
	 */
	public boolean hasRole(String userId, String role) {
		String hql = "select u.id FROM ActorToRole u WHERE u.actorRole.no=? AND u.actorId=?";
		return (findSingle(hql, role, userId) != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.actorrole.SysRoleDao#loadRoles(java.lang.String)
	 */
	public Map<String, String> loadRoles(String id) {
		String hql = "SELECT atr.actorRole.no,atr.actorRole.name FROM ActorToRole atr WHERE atr.actorId=?";
		Map<String, String> m = WxlSugar.newLinkedHashMap();
		List<Object[]> list = find(hql, id);
		RowReader rr = RowReader.create(list);
		while (rr.hasNextRow()) {
			m.put((String) rr.next(), (String) rr.next());
			rr.nextRow();
		}

		return m;
	}

	public void doAssign(String actorId, String[] roleId) {
		if (roleId == null)
			return;
		String hql = "select actorRole.id FROM ActorToRole WHERE actorId=?";
		List<String> ids = find(hql, actorId);
		Map<String, String> map = new HashMap<String, String>();
		for (String s : ids) {
			map.put(s, s);
		}

		for (String rid : roleId) {
			if (map.containsKey(rid)) {
				map.remove(rid);
				continue;
			}
			// make it
			ActorRole atr = ActorRole.create(rid, actorId);
			getHibernateTemplate().save(atr);

		}

		// clear
		hql = "delete from ActorToRole where actorId=? AND actorRole.id=?";
		for (String s : map.keySet()) {
			delete(hql, actorId, s);
		}

	}

	@Override
	public String getName(String id) {
		String hql = "select name from SysRole where id=?";
		return findSingle(hql, id);
	}

}
