/**
 * Created on 2008-12-18 02:45:34
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
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
import puerta.support.utils.StringTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.ModuleCondition;
import puerta.system.dao.AppFieldDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.ModuleDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.Module;
import puerta.system.transfer.ModuleTransfer;
import puerta.system.vo.ModuleWithAppField;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class ModuleDaoImpl extends HibernateGenericDao<Module, String>
		implements ModuleDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private AppFieldDao appFieldDao;
	@Resource
	private Mls mls;

	public AppFieldDao getAppFieldDao() {
		return appFieldDao;
	}

	public void setAppFieldDao(AppFieldDao appFieldDao) {
		this.appFieldDao = appFieldDao;
	}

	public LoggerDao getLoggerDao() {
		return loggerDao;
	}

	public void setLoggerDao(LoggerDao loggerDao) {
		this.loggerDao = loggerDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#loadModules(java.lang.String)
	 */
	public List<Module> loadModules(String id) {
		String hql = "SELECT DISTINCT mm FROM ActorPurview"
				+ " AS am,Module mm WHERE "
				+ "am.actorId=? AND am.purview.module.id=mm.id and am.purview.menuItem = ? "
				+ " and mm.removed=? ORDER BY mm.indexNo";

		List<Module> list = find(hql, id, Boolean.TRUE, false);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterEnable(java.lang.Object,
	 * boolean)
	 */
	public void afterEnable(Module obj, boolean enable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterRemove(java.lang.Object)
	 */
	public void afterRemove(Module obj) {
		Module m = (Module) obj;
		loggerDao.doLog(mls.t("platform.module.remove"), StringTool.getBean(m));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.basement.bonding.dao.DomainInspector#afterVisible(java.lang.Object,
	 * boolean)
	 */
	public void afterVisible(Module obj, boolean visible) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#loadEnabled(java.lang.String)
	 */
	public List<Module> loadAll(String no) {
		String hql = "FROM Module u WHERE u.appField.no=? AND u.removed=? ORDER BY u.indexNo";
		return find(hql, no, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#save(wxlplatform .po.Module)
	 */
	public Module save(Module m) {
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());

		if (exists(v)) {
			Mls.raise("platform.module.noExists", m.getNo());
		}

		m.setAppField(appFieldDao.findByNo(m.getAppField().getNo()));

		getHibernateTemplate().save(m);
		loggerDao.doLog(mls.t("platform.module.add"), StringTool.getBean(m));
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#search(wxlplatform
	 * .module.ModuleCondition, int)
	 */
	public PaginationSupport<Module> search(ModuleCondition condition,
			int pageNo) {
		DetachedCriteria dc = makeDetachedCriteria(condition);

		// dc.add(criterion)

		dc.createAlias("appField", "as");

		dc.add(Restrictions.eq("as.no", condition.getAppFieldNo()));

		return super.findPageByCriteria(dc, new PageInfo(pageNo),
				Order.asc("indexNo"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#update(wxlplatform .po.Module)
	 */
	public Module update(Module m) {
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", m.getId());

		if (exists(v, filter)) {
			Mls.raise("platform.module.noExists", m.getNo());
		}

		Module po = findById(m.getId());

		po.setNo(m.getNo());
		po.setName(m.getName());

		boolean diffIndex = po.getIndexNo() != m.getIndexNo();
		int oldIndex = po.getIndexNo();
		po.setIndexNo(m.getIndexNo());

		po.setAppField(appFieldDao.findByNo(m.getAppField().getNo()));

		getHibernateTemplate().update(po);

		// 更新权限的索引
		// 批量修改
		if (diffIndex) {
			String oldTreePath = "M" + StringTool.fillZero(oldIndex, 3);
			String newTreePath = "M" + StringTool.fillZero(m.getIndexNo(), 3);
			logger.debug("oldTree=" + oldTreePath);
			logger.debug("newTree=" + newTreePath);
			String hql = "UPDATE Purview SET treePath = Replace ( treePath , ?,?) WHERE module.id=? ";
			updateBatch(hql, oldTreePath, newTreePath, po.getId());
		}
		loggerDao.doLog(mls.t("platform.module.edit"), StringTool.getBean(m));
		return m;
	}

	public ModuleWithAppField beforeEdit(String id) {
		Module m = null;
		if (StringUtils.isEmpty(id)) {

		} else {
			m = findById(id);

			// System.out.println(m.getNo());
			// System.out.println(m.getAppField().getId());
			// System.out.println(m.getAppField().getNo());

			// m.getAppField().getNo()
			getHibernateTemplate().initialize(m.getAppField());
			// getHibernateTemplate().initialize(m.getAppField());
			// moduleDao.initialize(m.getAppField());
			// System.out.println(m.getAppField().getNo());
		}
		List<AppField> as = appFieldDao.loadByIndexNo();
		return new ModuleWithAppField(m, as);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM Module p";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.module.ModuleDao#doImport(java.util.List, boolean)
	 */
	public void doImport(List<Module> modules, boolean b) {
		for (Iterator<Module> iter = modules.iterator(); iter.hasNext();) {
			if (b) {
				updateByNoOrSave(iter.next());
			} else {
				this.save(iter.next());
			}
		}

	}

	/**
	 * @param next
	 */
	private void updateByNoOrSave(Module next) {
		Module po = this.findByNo(next.getNo());
		if (po == null) {
			save(next);
		} else {
			next.setId(po.getId());
			update(next);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<Module> doExports(String afNo) {
		if (StringUtils.isEmpty(afNo)) {
			List<Module> ret = this.loadAll();
			for (Module m : ret) {
				getHibernateTemplate().initialize(m.getAppField());
			}
			return ret;
		} else {
			return this.loadAll(afNo);
		}
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
	public JDOMTransfer<Module> getTransfer() {
		return new ModuleTransfer();
	}

	@Override
	public int getMaxIndexNo(String appFieldNo) {
		String hql = "select MAX(indexNo) FROM Module WHERE removed=? AND appField.no=?";
		Integer n = findSingle(hql, false, appFieldNo);
		if (n == null) {
			return 10;
		}
		return n + 10;
	}
}
