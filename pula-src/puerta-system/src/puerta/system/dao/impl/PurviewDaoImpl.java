/**
 * Created on 2008-12-19 10:04:02
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
import org.springframework.stereotype.Repository;

import puerta.PuertaWeb;
import puerta.support.mls.Mls;
import puerta.support.utils.StringTool;
import puerta.system.base.CheckInterceptor;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.PurviewCondition;
import puerta.system.dao.AppFieldDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.helper.ParameterHelper;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.po.ActorPurview;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.po.RequestUri;
import puerta.system.transfer.PurviewTransfer;
import puerta.system.vo.IActionResult;
import puerta.system.vo.PurviewWithModule;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class PurviewDaoImpl extends HibernateGenericDao<Purview, String>
		implements PurviewDao {

	@Resource
	private ModuleDao moduleDao;
	@Resource
	private LoggerDao loggerDao;
	@Resource
	private AppFieldDao appFieldDao;
	@Resource
	private ParameterKeeper parameterKeeper;
	@Resource
	private Mls mls;

	// private InsiderDao insiderDao;
	//
	// public InsiderDao getInsiderDao() {
	// return insiderDao;
	// }
	//
	// public void setInsiderDao(InsiderDao insiderDao) {
	// this.insiderDao = insiderDao;
	// }

	public ParameterKeeper getParameterKeeper() {
		return parameterKeeper;
	}

	public void setParameterKeeper(ParameterKeeper parameterKeeper) {
		this.parameterKeeper = parameterKeeper;
	}

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

	public ModuleDao getModuleDao() {
		return moduleDao;
	}

	public void setModuleDao(ModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#loadAll(java.lang.String)
	 */
	public List<Purview> loadAll(String no) {
		String hql = "FROM Purview p WHERE p.appField.no=? and p.removed=? and p.module.removed=? ORDER BY p.treePath";
		return find(hql, new Object[] { no, false, false });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#save(java.lang.String)
	 */
	public void save(String name) {
		Purview p = new Purview();
		p.setName(name);

		getHibernateTemplate().save(p);

		System.out.println("WO=" + this);

		// throw new LawnDaoException("non");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doCheck(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	public String doCheck(RequestUri ma, String id) {
		final boolean const_pass_not_register = parameterKeeper.getBoolean(
				ParameterHelper.PURVIEW_CHECK_PASS_NOTREGED, true);
		final boolean force_check_session = parameterKeeper.getBoolean(
				ParameterHelper.PURVIEW_CHECK_FORCE_SESSION, true);
		;

		// MappedAction ma = purviewDao.getMappedAction(actionName);

		if (ma == null) {
			// skip?
			if (!const_pass_not_register) {
				return IActionResult.NOT_REGISTER;
			}

			try {
				// if (AUTO_REGISTER_ACTION) {
				// ma = new MappedAction();
				// ma.setName(actionName);
				// purviewDao.save(ma);
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			if (force_check_session) {
				if (id == null) {
					return IActionResult.NOT_LOGIN;
				}
			}
		} else {

			if (ma.isOnlineCheck() || ma.isPurviewCheck()) {

				if (id == null) {
					return IActionResult.NOT_LOGIN;
				}
			}

			if (CheckInterceptor.ROLLING_STONE) {
				return PASS;
			}

			if (ma.isPurviewCheck()) {

				// check
				Boolean b = canAccess(ma, id);

				if (b == null) {
					return IActionResult.NOT_REGISTER;
				}

				if (!b.booleanValue()) {
					return IActionResult.NO_PURVIEW;
				}
			}
		}

		return PASS;
	}

	public Boolean canAccess(RequestUri ma, String actorId) {

		String hql = "SELECT COUNT(u) FROM PurviewToMappedAction as s, MappedAction u WHERE (u.id=?) AND u.id=s.mappedAction.id";
		int count = getCountByQuery(hql, ma.getId());

		// 没登记过的不判断权限
		// 没有权限关联
		if (count <= 0)
			return null;

		logger.debug("register action =" + String.valueOf(count));
		// AppField as = appFieldDao.findByNo(
		// su.getAppFieldNo());

		hql = "SELECT COUNT(a) FROM PurviewToMappedAction AS a,ActorPurview AS u WHERE a.purview=u.purview ";
		hql += " AND a.mappedAction.id=? AND u.actorId=?";
		count = getCountByQuery(hql, ma.getId(), actorId);

		logger.debug("find action =" + String.valueOf(count));

		if (count <= 0) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#beforeEdit(java.lang.String)
	 */
	public PurviewWithModule beforeEdit(String id) {

		Purview purview = this.findById(id);
		Module module = purview.getModule();

		int maxIndexNo = this.getMaxIndexNoByPurview(purview) + 10;
		// module = moduleDao.findModuleById(id);

		List<Module> modules = moduleDao.loadAll(module.getAppField().getNo());

		return new PurviewWithModule(purview, module, maxIndexNo, modules);

	}

	/**
	 * @param purview
	 * @return
	 */
	private int getMaxIndexNoByPurview(Purview purview) {
		String hql = "SELECT MAX(u.indexNo) FROM Purview u,Purview p1 WHERE"
				+ " u.removed=? AND p1.id=? AND p1.module.id=u.module.id";

		Integer n = findSingle(hql, false, purview.getId());
		if (n == null) {
			return 0;
		}
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#update(wxl platform.po.Purview)
	 */
	public Purview update(Purview p) {
		Purview po = this.findById(p.getId());

		Map<String, Object> conditions = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();
		conditions.put("no", p.getNo());
		conditions.put("removed", false);

		f.put("id", p.getId());
		boolean exists = exists(conditions, f);
		if (exists) {
			Mls.raise("platform.purview.noExists", p.getNo());
		}

		po.setNo(p.getNo());

		po.setName(p.getName());
		po.setIndexNo(p.getIndexNo());
		po.setLevel(p.getLevel());
		po.setMenuItem(p.isMenuItem());
		// po.setModule(p.getModule());
		po.setParentPurview(p.getParentPurview());
		po.setVisible(p.isVisible());
		po.setDefaultURL(p.getDefaultURL());
		po.setModule(p.getModule());

		setupTreePath(po);
		// po.setType(po.getModule().getType());
		po.setRemoved(false);
		getHibernateTemplate().update(po);

		return po;
	}

	/**
	 * @param po
	 */
	private void setupTreePath(Purview p) {
		String treePath = makeTreePath(p);

		p.setTreePath(treePath);

		String hql = "FROM Purview d WHERE d.parentPurview.id=? AND d.removed=?";
		List<Purview> l = find(hql, new Object[] { p.getId(), false });
		for (Iterator<Purview> iter = l.iterator(); iter.hasNext();) {
			Purview pur = (Purview) iter.next();
			// pur.setMenuItem(p.isMenuItem());
			setupTreePath(pur);
		}

	}

	/**
	 * @param p
	 * @return
	 */
	private String makeTreePath(Purview p) {
		String treePath = StringTool.fillZero(p.getIndexNo(), 3);
		if (p.getParentPurview() != null) {
			Purview parent = null;
			if (!StringUtils.isEmpty(p.getParentPurview().getNo())) {
				parent = findByNo(p.getParentPurview().getNo());

			} else {
				parent = findById(p.getParentPurview().getId());
			}

			treePath = parent.getTreePath() + "-" + treePath;

			p.setLevel(parent.getLevel() + 1);

			p.setModule(parent.getModule());
			p.setLeaf(true);

			if (parent.isLeaf())
				parent.setLeaf(false);

			// parent.set
		} else {
			logger.debug("purview.name=" + p.getName());
			logger.debug("makeTreePath::module.no=" + p.getModule().getNo());
			logger.debug("makeTreePath::module.name=" + p.getModule().getName());
			Module m = moduleDao.findById(p.getModule().getId());
			p.setModule(m);
			treePath = "M" + StringTool.fillZero(m.getIndexNo(), 3) + "-P"
					+ treePath;
		}

		// p.setType(p.getModule().getType());
		p.setAppField(p.getModule().getAppField());

		return treePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#beforeSaveByModuleId(java
	 * .lang.String)
	 */
	public PurviewWithModule beforeSaveByModuleId(String moduleId) {
		Module module = moduleDao.findById(moduleId);
		getHibernateTemplate().initialize(module.getAppField());
		int maxIndexNo = this.getMaxIndexNo(module.getId()) + 10;

		return new PurviewWithModule(null, module, maxIndexNo, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doMoveDown(java.lang.String)
	 */
	public void doMoveDown(String id) {
		Purview p = this.findById(id);
		String hql;
		// int nextIndex;
		List<Purview> objList;
		if (p.getLevel() == 0) {
			hql = "FROM Purview p WHERE p.module.id=? AND p.level=? AND p.indexNo>=? AND p.id<>? AND p.removed=? ORDER BY p.indexNo ";
			objList = find(
					hql,
					new Object[] { p.getModule().getId(), p.getLevel(),
							p.getIndexNo(), p.getId(), false });
		} else {
			hql = "FROM Purview p WHERE p.parentPurview.id=? AND p.indexNo>=? AND p.id<>? AND p.removed=? ORDER BY p.indexNo ";

			objList = find(hql,
					new Object[] { p.getParentPurview().getId(),
							p.getIndexNo(), p.getId(), false });
		}

		if (objList.size() <= 0) {
			// do nothing
		} else {
			Purview p2 = (Purview) objList.get(0);
			if (p2.getIndexNo() == p.getIndexNo()) {
				p2.setIndexNo(p2.getIndexNo() + 1);
			}
			this.swapIndexNo(p, p2);
		}

	}

	/**
	 * @param p
	 * @param p2
	 */
	private void swapIndexNo(Purview p1, Purview p2) {
		int p1Index = 0, p2Index = 0;
		// if(p1!=null){
		p1Index = p1.getIndexNo();
		// }

		// if(p2!=null){
		p2Index = p2.getIndexNo();
		// }
		//
		// if (p1Index == p2Index) {
		// if (down) {
		// p1Index += 2;
		// } else {
		// p2Index = p2Index + 1;
		// }
		// }

		p1.setIndexNo(p2Index);
		p2.setIndexNo(p1Index);

		this.setupTreePath(p1);
		this.setupTreePath(p2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doMoveToModule(java.lang .String,
	 * java.lang.String)
	 */
	public void doMoveToModule(String id, String targetModule) {
		Purview p = this.findById(id);
		Module m = moduleDao.findById(targetModule);

		p.setModule(m);
		if (p.getParentPurview() != null)
			this.checkLeaf(p.getParentPurview());
		p.setParentPurview(null);
		p.setLevel(0);

		setupTreePath(p);

		getHibernateTemplate().update(p);

	}

	/**
	 * @param parentPurview
	 */
	private void checkLeaf(Purview p) {
		String hql = "SELECT COUNT(u) FROM Purview u WHERE u.parentPurview.id=? AND u.removed=?";
		long i = super.getCountByQuery(hql, new Object[] { p.getId(), false });
		p.setLeaf(i == 0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doMoveToPurview(java.lang .String,
	 * java.lang.String)
	 */
	public String doMoveToPurview(String id, String targetPurviewNo) {
		Purview p = this.findById(id);
		Purview parent = (Purview) this.findByNo(targetPurviewNo);
		if (parent == null) {
			Mls.raise("platform.purview.noNotExists", targetPurviewNo);

		}

		if (p.getParentPurview() != null)
			this.checkLeaf(p.getParentPurview());

		p.setParentPurview(parent);
		setupTreePath(p);

		getHibernateTemplate().update(p);

		return parent.getId();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doMoveUp(java.lang.String)
	 */
	public void doMoveUp(String id) {
		Purview p = this.findById(id);
		String hql;
		// int nextIndex;
		List<Purview> objList;
		if (p.getLevel() == 0) {
			hql = "FROM Purview p WHERE p.module.id=? AND p.level=? AND p.indexNo<=? AND p.id<>? AND p.removed=? ORDER BY p.indexNo DESC ";
			objList = find(
					hql,
					new Object[] { p.getModule().getId(), p.getLevel(),
							p.getIndexNo(), p.getId(), false });
		} else {
			hql = "FROM Purview p WHERE p.parentPurview.id=? AND p.indexNo<=? AND p.id<>? AND p.removed=?  ORDER BY p.indexNo DESC";

			objList = find(hql,
					new Object[] { p.getParentPurview().getId(),
							p.getIndexNo(), p.getId(), false });
		}

		if (objList.size() <= 0) {
			// do nothing
		} else {
			Purview p2 = (Purview) objList.get(0);
			if (p2.getIndexNo() == p.getIndexNo()) {
				p2.setIndexNo(p2.getIndexNo() - 1);
			}
			this.swapIndexNo(p, p2);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#remove(java.lang.String[])
	 */
	public void remove(String[] objId) {
		if (objId == null)
			return;
		for (int i = 0; i < objId.length; i++) {
			Purview p = findById(objId[i]);

			removePurview(p);
			if (p.getParentPurview() != null)
				checkLeaf(p.getParentPurview());

			loggerDao.doLog(mls.t("platform.purview.delete"),
					StringTool.getBean(p));
		}

	}

	/**
	 * @param p
	 */
	private void removePurview(Purview p) {
		List<Purview> children = getChildren(p);
		for (Iterator<Purview> iter = children.iterator(); iter.hasNext();) {
			Purview child = (Purview) iter.next();
			removePurview(child);
		}

		// p.setRemoved(true);
		String hql = "UPDATE Purview SET removed=? WHERE id=?";
		super.updateBatch(hql, true, p.getId());

	}

	/**
	 * @param p
	 * @return
	 */
	private List<Purview> getChildren(Purview p) {
		String hql = "FROM Purview p WHERE p.parentPurview.id=?";
		return find(hql, p.getId());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#beforeSaveByPurviewId(java
	 * .lang.String)
	 */
	public PurviewWithModule beforeSaveByPurviewId(String purviewId) {
		Purview purview = this.findById(purviewId);
		Module module = purview.getModule();
		// System.out.println(module.getNo());
		getHibernateTemplate().initialize(module);
		getHibernateTemplate().initialize(module.getAppField());
		// System.out.println("module=" + module.getNo());
		int maxIndexNo = this.getMaxIndexNoByPurview(purview) + 10;

		return new PurviewWithModule(purview, module, maxIndexNo, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#save(wxlplatform .po.Purview)
	 */
	public Purview save(Purview p) {

		Map<String, Object> conditions = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();
		conditions.put("no", p.getNo());
		conditions.put("removed", false);
		// f.put("id", p.getId());
		boolean exists = exists(conditions, f);
		if (exists) {
			Mls.raise("platform.purview.noExists", p.getNo());
		}

		setupTreePath(p);

		p.setRemoved(false);
		p.setLeaf(true);
		int maxIndex = this.getMaxIndexNo(p.getModule().getId());
		p.setIndexNo(maxIndex + 10);

		getHibernateTemplate().save(p);

		loggerDao.doLog(mls.t("platform.purview.add"), p.getName());

		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#getMaxIndexNo(java.lang. String)
	 */
	public int getMaxIndexNo(String id) {
		String hql = "SELECT MAX(u.indexNo) FROM Purview u WHERE"
				+ " u.removed=? AND u.module.id=? ";

		Integer n = findSingle(hql, false, id);

		if (n == null) {
			return 0;
		} else {
			return n.intValue();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#search(wxl
	 * platform.purview.PurviewCondition)
	 */
	public List<Purview> search(PurviewCondition condition) {
		String hql = "FROM Purview p WHERE  p.module.id=? AND p.removed=? ORDER BY p.treePath";
		List<Purview> ps = find(hql, new Object[] { condition.getModuleId(),
				false });
		// for (Purview p : ps) {
		// // getHibernateTemplate().initialize(p.getModule());
		// }
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#loadFunctionPoints(java.lang.String)
	 */
	public List<Purview> loadFunctionPoints(String appFieldNo) {
		String hql = "FROM Purview u WHERE u.appField.no=? AND u.removed=?"
				+ " AND  u.module.removed=? ORDER BY u.treePath";
		List<Purview> list = find(hql,
				new Object[] { appFieldNo, false, false });
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doClear()
	 */
	public void doClear() {

		String hql = "DELETE FROM ActorPurview";
		super.delete(hql);
		hql = "UPDATE Purview SET parentPurview=null";
		super.updateBatch(hql);
		hql = "DELETE FROM Purview p";
		super.delete(hql);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#doImport(java.util.List, boolean)
	 */
	public void doImport(List<Purview> purviews, boolean append, String actorId) {
		for (Iterator<Purview> iter = purviews.iterator(); iter.hasNext();) {
			Purview unp = iter.next();
			logger.debug("==>module.no" + unp.getModule().getNo());
			if (!StringUtils.isEmpty(unp.getModule().getNo())) {
				Module m = moduleDao.findByNo(unp.getModule().getNo());

				if (m == null) {
					// skip
					continue;
				}
				unp.setModule(m);
			}

			if (unp.getParentPurview() != null) {
				if (!StringUtils.isEmpty(unp.getParentPurview().getNo())) {
					Purview pp = this.findByNo(unp.getParentPurview().getNo());
					unp.setParentPurview(pp);
				}
			}

			Purview p = null;
			if (append) {
				p = this.updateByNoOrSave(unp);
			} else {
				p = this.save(unp);
			}

			// if (p.getType() == NutGroundwork.ADMIN) {
			// AdminPurview ap = new AdminPurview();
			// ap.setPurview(p);
			// ap.setAdmin(helperDao.getCurrentAdmin());
			// getHibernateTemplate().save(ap);
			// }

			if (StringUtils.equals(unp.getAppField().getNo(),
					PuertaWeb.AS_INSIDER)) {
				ActorPurview ap = new ActorPurview();
				ap.setPurview(p);
				ap.setActorId(actorId);
				getHibernateTemplate().save(ap);
			}
		}

	}

	/**
	 * @param unp
	 * @return
	 */
	private Purview updateByNoOrSave(Purview next) {
		Purview po = this.findByNo(next.getNo());
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
	 * @see wxlplatform.purview.PurviewDao#fetchPurviewWithModule(java.lang.
	 * String)
	 */
	public Purview fetchPurviewWithModule(String id) {
		Purview p = this.findById(id);
		getHibernateTemplate().initialize(p.getModule());
		getHibernateTemplate().initialize(p.getAppField());
		getHibernateTemplate().initialize(p.getParentPurview());
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<Purview> doExports(String asNo) {
		if (StringUtils.isEmpty(asNo)) {

			String hql = "FROM Purview WHERE removed=? ORDER BY appField.indexNo,treePath";
			List<Purview> ps = find(hql, false);
			for (Purview p : ps) {
				getHibernateTemplate().initialize(p.getModule());
				getHibernateTemplate().initialize(p.getAppField());
			}
			return ps;
		} else {
			return this.loadAll(asNo);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<Purview> objs, boolean update) {
		for (Purview unp : objs) {

			logger.debug("==>module.no" + unp.getModule().getNo());
			if (!StringUtils.isEmpty(unp.getModule().getNo())) {
				Module m = moduleDao.findByNo(unp.getModule().getNo());

				if (m == null) {
					// skip
					continue;
				}
				unp.setModule(m);
			}

			if (unp.getParentPurview() != null) {
				if (!StringUtils.isEmpty(unp.getParentPurview().getNo())) {
					Purview pp = this.findByNo(unp.getParentPurview().getNo());
					unp.setParentPurview(pp);
				}
			}

			@SuppressWarnings("unused")
			Purview p = null;
			if (update) {
				p = this.updateByNoOrSave(unp);
			} else {
				p = this.save(unp);
			}

			// if (p.getType() == NutGroundwork.ADMIN) {
			// AdminPurview ap = new AdminPurview();
			// ap.setPurview(p);
			// ap.setAdmin(helperDao.getCurrentAdmin());
			// getHibernateTemplate().save(ap);
			// }

			// if (StringUtils.equals(unp.getAppField().getNo(),
			// WxlWeb.AS_INSIDER)) {
			// ActorPurview ap = new ActorPurview();
			// ap.setPurview(p);
			// ap.setActorId(actorId);
			// getHibernateTemplate().save(ap);
			// }
		}

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
	public JDOMTransfer<Purview> getTransfer() {
		return new PurviewTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.purview.PurviewDao#fetchAll(java.lang.String)
	 */
	public Purview fetchAll(String id) {
		Purview p = this.findById(id);
		getHibernateTemplate().initialize(p.getModule().getAppField());
		return p;
	}

	@Override
	public List<Purview> getChildren(String id) {
		String hql = "FROM Purview where parentPurview.id=? AND removed=? order by treePath";
		return find(hql, id, false);
	}

	@Override
	public Purview getByDefaultURL(String processUri, String no) {
		String hql = "select u from Purview u where u.defaultURL=? and u.removed=? and u.appField.no=?";
		return findSingle(hql, processUri, false, no);
	}
}
