package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.HashMap;
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

import puerta.PuertaWeb;
import puerta.support.BasementException;
import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.DomainInspector;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.LoggablePo;
import puerta.support.dao.QueryJedi;
import puerta.support.mls.Mls;
import puerta.support.utils.MD5;
import puerta.support.utils.PewUtils;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.intfs.IWxlPluginActor;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.service.SettingService;
import puerta.system.vo.ActorWithPurviewAll;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.BhzqConstants;
import pula.sys.conditions.SysUserCondition;
import pula.sys.daos.SysUserDao;
import pula.sys.domains.SysUser;
import pula.sys.transfers.SysUserTransfer;

@Repository
public class SysUserDaoImpl extends HibernateGenericDao<SysUser, String>
		implements SysUserDao, DomainInspector<SysUser>,
		IWxlPluginSetting<SysUser>, IWxlPluginActor<SysUser> {

	@Resource
	private ActorPurviewDao actorPurviewDao;
	@Resource
	private PurviewDao purviewDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private SettingService settingService;
	@Resource
	private Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.potential.mib.online.backend.SysUserDao#login(java.lang.String,
	 * java.lang.String)
	 */
	public SysUser doLogin(String loginId, String password, boolean importClient) {
		String hql = "SELECT a FROM SysUser as a WHERE a.loginId=? AND a.password=? AND a.removed=?";

		String encode_password = MD5.GetMD5String(loginId + password);
		List<SysUser> list = find(hql, new Object[] { loginId, encode_password,
				Boolean.FALSE });

		Iterator<SysUser> iter = list.iterator();
		if (iter.hasNext()) {
			SysUser user = iter.next();
			if (!user.isEnabled()) {
				logger.debug("SysUser(" + loginId + ") disabled");
				Mls.raise("platform.user.userDisabled");
				return null;
			}

			String event = null;

			if (importClient) {
				event = "客户端登录";
			} else {
				event = "登录";
			}

			loggerDao.doLog(event, "", user);

			return user;
		} else {
			logger.debug("SysUser(" + loginId + ") not found");
			Mls.raise("platform.user.cannotFoundSysUser");
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.potential.mib.online.backend.SysUserDao#logout(java.lang.String)
	 */
	public void doLogout(String id) {

		loggerDao.doLog(mls.t("platform.user.exitTitle"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.potential.mib.online.backend.SysUserDao#doSaveSysUser(com.potential
	 * .mib .online.backend.po.SysUser)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.potential.mib.online.backend.SysUserDao#doUpdateSysUser(com.potential
	 * .mib .online.backend.po.SysUser)
	 */
	public Mix<SysUser, Boolean> update(SysUser user,
			final boolean changePassword) {
		// if (this.existsLoginId(user)) {
		// Pe.raise("指定编号已经存在:" + user.getLoginId());
		// return null;
		// }

		// if (this.existsCamNo(user.getCamNo(), user.getId())) {
		// Pe.raise("CAM编号已经使用，请更换");
		// }
		boolean b = false;
		SysUser po = findById(user.getId());

		// if (user.getManager() != null) {
		// SysUser man = this.findByLoginId(user.getManager().getLoginId());
		// if (man == null) {
		// Pe.raise("找不到指定的编号的主管:" + user.getManager().getLoginId());
		// }
		// po.setManager(man);
		// } else {
		// po.setManager(null);
		// }
		po.setName(user.getName());

		boolean salesman = false;
		if (BhzqConstants.ROLE_SALES.equals(po.getRole().getNo())) {
			salesman = true;
		}

		if (!salesman) {

			if (po.getBelongsToGroup() != null
					&& StringUtils.equals(po.getBelongsToGroup().getId(), user
							.getBelongsToGroup().getId())) {

			} else {
				po.setBelongsToGroup(user.getBelongsToGroup());
				b = true;
			}

			po.setRole(user.getRole());

			po.setBranch(user.getBranch());
		} else {

		}

		if (changePassword) {
			String encode_password = MD5.GetMD5String(po.getLoginId()
					+ user.getPassword());
			po.setPassword(encode_password);

		}

		_update(po);
		return Mix.create(po, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.potential.mib.online.backend.SysUserDao#loadSysUsers()
	 */
	public List<SysUser> loadSysUsers() {

		return loadAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.potential.mib.online.backend.SysUserDao#countSysUsers(java.io.
	 * Serializable)
	 */
	public int countSysUsers(SysUserCondition condition) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		int count = getCountByCriteria(dc);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.potential.mib.online.backend.SysUserDao#loadSysUsers(java.io.Serializable
	 * , com.potential.platform.PageInfo)
	 */
	public PaginationSupport<SysUser> search(SysUserCondition conditon,
			int pageNo) {

		DetachedCriteria criteria = makeDetachedCriteria(conditon);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("loginId"));
	}

	/**
	 * @param conditon
	 * @return
	 */
	private DetachedCriteria makeDetachedCriteria(SysUserCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(SysUser.class, "uu");

		if (!StringUtils.isEmpty(condition.getLoginId())) {
			dc.add(Restrictions.like("loginId", condition.getLoginId(),
					MatchMode.ANYWHERE));
		}

		if (!StringUtils.isEmpty(condition.getName())) {
			dc.add(Restrictions.like("name", condition.getName(),
					MatchMode.ANYWHERE));
		}
		HibernateTool.eqIfNotEmpty(dc, "role.id", condition.getRoleId());
		HibernateTool.eqIfNotZero(dc, "branch.id", condition.getBranchId());
		HibernateTool.eqIfNotEmpty(dc, "belongsToGroup.id",
				condition.getGroupId());

		// if (!StringUtils.isEmpty(condition.getRoleId())) {
		// DetachedCriteria sub_dc = DetachedCriteria.forClass(
		// SysUserRole.class, "sur");
		// sub_dc.setProjection(Projections.id());
		// sub_dc.add(Restrictions.eqProperty("sur.sysUser.id", "uu.id"));
		// sub_dc.add(Restrictions.eq("sur.role.id", condition.getRoleId()));
		// dc.add(Subqueries.exists(sub_dc));
		//
		// }

		dc.add(Restrictions.eq("removed", false));

		return dc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.potential.mib.online.backend.SysUserDao#doChangePassword(java.lang
	 * .String , java.lang.String, java.lang.String)
	 */
	public void doChangePassword(String id, String oldPassword,
			String newPassword) {
		SysUser user = findById(id);
		// check old password ;
		String loginId = user.getLoginId();
		String password = oldPassword;
		String encode_password = MD5.GetMD5String(loginId + password);
		// String old_encode_password = MD5.GetMD5String(loginId + password);
		if (encode_password.equals(user.getPassword())) {
			encode_password = MD5.GetMD5String(loginId + newPassword);
			user.setPassword(encode_password);
		} else {

			Mls.raise("platform.user.badPassword");
			return;
		}

		loggerDao.doLog(mls.t("platform.user.updateSelfPassword"), "");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.potential.mib.online.backend.SysUserDao#doChangePassword(java.lang
	 * .String , java.lang.String)
	 */
	// public void doChangePassword(String id, String newPassword) {
	// SysUser user = findById(id);
	// // check old password ;
	// String loginId = user.getLoginId();
	// String encode_password = MD5.GetMD5String(loginId + newPassword);
	// user.setPassword(encode_password);
	//
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.potential.mib.online.backend.SysUserDao#exists(java.lang.String)
	 */
	public boolean exists(String loginId) {

		int count = getCountByQuery(
				"FROM SysUser WHERE loginId=? and u.removed=?", loginId, false);

		return count > 0;
	}

	public SysUser save(SysUser user) {

		if (existsLoginId(user)) {
			//
			Mls.raise("platform.user.idExists", user.getLoginId());
		}

		// if (existsCamNo(user.getCamNo(), user.getId())) {
		// Pe.raise("CAM编号已经使用，请更换");
		// }

		// if (user.getManager() != null) {
		// SysUser man = this.findByLoginId(user.getManager().getLoginId());
		// if (man == null) {
		// Pe.raise("找不到指定的编号的主管:" + user.getManager().getLoginId());
		// }
		// user.setManager(man);
		// } else {
		// // po.setManager(null);
		// }

		String loginId = user.getLoginId();
		String password = user.getPassword();
		String encode_password = MD5.GetMD5String(loginId + password);
		user.setPassword(encode_password);
		user.setEnabled(true);
		user.setCreatedTime(Calendar.getInstance());
		// user.setDomain(d);
		// user.setVendee(d.getVendee());
		// SystemLogHelper.saveSystemLog("????????",
		// StringTool.getSysUser(user));

		// List<SysUserRole> roles = user.getRoles();
		// List<SysUserRole> roles_build = buildRoles(user, roles);
		// user.setRoles(null);

		_save(user);

		// for (SysUserRole sr : roles_build) {
		// getHibernateTemplate().save(sr);
		// }

		// loggerDao.doLog(mls.t("platform.user.saveUser"), (LoggablePo) user);

		return user;
	}

	// private boolean existsCamNo(String camNo, String id) {
	// if (StringUtils.isEmpty(camNo)) {
	// return false;
	// }
	//
	// QueryJedi qp = new QueryJedi(
	// "select id from SysUser where camNo=? and removed=?", camNo,
	// false);
	// qp.ifNotEmpty("and id<>?", id);
	//
	// return exists(qp.hql(), qp.parameters());
	// }

	private boolean existsLoginId(SysUser user) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();

		map.put("loginId", user.getLoginId());
		map.put("removed", false);
		// map.put("domain.id", d.getId());
		if (!StringUtils.isEmpty(user.getId())) {
			f.put("id", user.getId());
		}
		return this.exists(map, f);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.user.SysUserDao#doLogin(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public SysUser doLogin(String loginId, String password) {
		String hql = "SELECT a FROM SysUser as a LEFT JOIN FETCH a.branch WHERE a.loginId=? AND a.password=? "
				+ " AND a.removed=? ";

		String encode_password = MD5.GetMD5String(loginId + password);
		List<SysUser> list = find(hql, new Object[] { loginId, encode_password,
				Boolean.FALSE });

		Iterator<SysUser> iter = list.iterator();
		if (iter.hasNext()) {
			SysUser user = (SysUser) iter.next();
			if (!user.isEnabled()) {
				logger.debug("SysUser(" + loginId + ") disabled");
				// throw new
				// LawnDaoException("mls.t("platform.user.userDisabled")");
				Mls.raise("platform.user.userDisabled");
			}

			String event = null;

			event = "登录";

			loggerDao.doLog(event, "", user);

			return user;
		} else {
			logger.debug("SysUser(" + loginId + ") not found");
			Mls.raise("platform.user.cannotFoundUser");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.system.user.SysUserDao#checkSysUser(java.lang.String
	 * , java.lang.String)
	 */
	public boolean checkSysUser(String loginId, String password) {
		String hql = "SELECT a FROM SysUser as a WHERE a.loginId=? AND a.password=? AND a.removed=?";

		String encode_password = MD5.GetMD5String(loginId + password);
		List<SysUser> list = find(hql, new Object[] { loginId, encode_password,
				Boolean.FALSE });

		Iterator<SysUser> iter = list.iterator();
		if (iter.hasNext()) {
			SysUser user = (SysUser) iter.next();
			if (!user.isEnabled()) {
				return false;
			}

		} else {
			logger.debug("SysUser(" + loginId + ") not found");
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.system.user.SysUserDao#loadByKeyword(java.lang.
	 * String)
	 */
	@Override
	public List<SysUser> loadByKeywords(String no, long bid) {
		PaginationSupport<SysUser> ps = super
				.findPageByQuery(
						"FROM SysUser u WHERE ( u.loginId LIKE ? or u.name LIKE ? ) AND u.removed=? AND u.enabled=? and branch.id=? ",
						new Object[] {40, 0, "%" + no + "%", "%" + no + "%", false, true, bid});

		return ps.getItems();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilvar.pew.system.user.SysUserDao#findByLoginId(java.lang.String)
	 */
	public SysUser findByLoginId(String loginId) {
		List<SysUser> users = this.findByProperty(new String[] { "loginId",
				"removed" }, new Object[] { loginId, false });
		if (users.size() == 0) {
			return null;
		} else {
			return users.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doClear()
	 */
	@Override
	public void doClear() {
		List<String> ids = find("SELECT id FROM SysUser");
		// clear purviews
		String hql = "DELETE FROM ActorPurview WHERE actorId=? ";
		for (String id : ids)
			delete(hql, id);
		delete("DELETE FROM SysUser");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doExports()
	 */
	@Override
	public List<SysUser> doExports(String afNo) {
		return find("FROM SysUser WHERE removed=?", false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	@Override
	public void doImport(List<SysUser> objs, boolean update) {
		// store
		for (SysUser ad : objs) {
			ad.setPassword(ad.getLoginId());
			// SysRole role = sysRoleDao.findByNo(ad.getRole().getNo());
			if (update) {
				SysUser sysUser = this.findByLoginId(ad.getId());

				if (sysUser == null) {
					update = false;

				} else {
					ad.setId(sysUser.getId());
					// ad.setRole(role);
					update(ad, false);
				}
			}

			if (!update) {
				// ad.setRole(role);
				ad.setPassword(ad.getLoginId());
				save(ad);
			}

			actorPurviewDao.doAssignAll(ad.getId(), PuertaWeb.AS_WEBUSER);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#getPluginName()
	 */
	@Override
	public String getPluginName() {
		return getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.web.platform.setting.IWxlPluginSetting#getTransfer()
	 */
	@Override
	public JDOMTransfer<SysUser> getTransfer() {
		return new SysUserTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.umsystem.system.user.SysUserDao#registerSetting()
	 */
	@Override
	public void registerSetting() {
		settingService.register((IWxlPluginSetting<SysUser>) this);
		settingService.register((IWxlPluginActor<SysUser>) this);
	}

	@Override
	public String getActorId(String no) {
		String hql = "select id from SysUser WHERE removed=? AND loginId=?";
		return findSingle(hql, false, no);
	}

	@Override
	public String getAppFieldNo() {
		return PuertaWeb.AS_WEBUSER;
	}

	@Override
	public List<SysUser> getActorsByRole(String rno) {
		return null;
	}

	@Override
	public void afterRemove(SysUser obj) {
		loggerDao.doLog("删除", (LoggablePo) obj);

	}

	@Override
	public void afterEnable(SysUser obj, boolean en) {
		loggerDao.doLog(PewUtils.toEnabledString(mls, en), (LoggablePo) obj);
	}

	@Override
	public void afterVisible(SysUser obj, boolean visible) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePassword(String id, String oldPassword, String newPassword) {

		SysUser admin = findById(id);
		// check old password ;
		String loginId = admin.getLoginId();
		String password = oldPassword;
		String encode_password = MD5.GetMD5String(loginId + password);
		// String old_encode_password = MD5.GetMD5String(loginId + password);
		if (encode_password.equals(admin.getPassword())) {
			encode_password = MD5.GetMD5String(loginId + newPassword);
			admin.setPassword(encode_password);
		} else {
			throw new BasementException(mls.t("platform.admin.passwordError"));
		}

		// SystemLogHelper.saveSystemLog(mls.t("platform.admin.editPassword"),
		// StringTool.getUser(user));

		loggerDao.doLog(mls.t("platform.admin.editPassword"),
				(LoggablePo) admin);

	}

	@Override
	public ActorWithPurviewAll<SysUser> beforeSetupPurview(String id) {
		List<Purview> purviews = purviewDao.loadAll(PuertaWeb.AS_WEBUSER);
		List<Purview> checkedPurviews = actorPurviewDao.loadByActor(id,
				PuertaWeb.AS_ADMIN);
		List<Module> module = moduleDao.loadAll(PuertaWeb.AS_ADMIN);
		SysUser sysUser = this.findById(id);

		return new ActorWithPurviewAll<SysUser>(sysUser, purviews,
				checkedPurviews, module);
	}

	@Override
	public MapList loadByKeywords(String no, String roleNo, long bid) {

		String hql = "select id as id ,loginId as no ,name as name FROM SysUser WHERE (loginId LIKE ? or name LIKE ? ) AND removed=? AND role.no=?";

		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, false, roleNo);

		qj.eqIfNotZero("branch.id", bid);

		qj.append("ORDER BY no");
		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public List<SysUser> loadByRole(String... role) {
		QueryJedi qp = new puerta.support.dao.QueryJedi();

		qp.append(
				"select u from SysUser u where u.removed=? and u.enabled=? and exists( select c.id from SysUserRole c,SysRole sr"
						+ " where u.id=c.sysUser.id and c.role.id=sr.id AND sr.id=c.role.id and ( 1 <> 1",
				false, true);
		for (String r : role) {
			qp.append(" or sr.no=? ", r);

		}
		if (role.length == 0) {
			qp.append(" or  1=1 ");
		}
		qp.append(")) order by u.loginId");

		return find(qp.hql(), qp.parameters());

	}

	@Override
	public List<String> listByGroup(String id) {
		String hql = "select id from SysUser where removed=? and belongsToGroup.id=?";
		return find(hql, false, id);
	}

	@Override
	public SysUser loadWithRole(String id) {
		String hql = "select u from SysUser u LEFT JOIN FETCH u.role where u.removed=? and u.id=?";
		return findSingle(hql, false, id);
	}

	@Override
	public void updateSalesmen(String name, String no, long branchId, long id) {
		// no 改完了密码就变了
		// String encode_password = MD5.GetMD5String(loginId + password);

		String sql = "select loginId,name,branch.id from SysUser where removed=? and salesman.id=?";

		Object[] objs = findSingle(sql, false, id);
		if (objs == null)
			return;
		String loginId = (String) objs[0];
		String user_name = (String) objs[1];
		Long user_branchId = (Long) objs[2];

		if (StringUtils.equals(loginId, no)) {
			if (StringUtils.equals(user_name, name)
					&& user_branchId == branchId) {
				return; // 都一样还改什么啊
			}
			// 不修改密码了
			sql = "update SysUser set name=?,branch.id=? where salesman.id=? and removed=?";
			updateBatch(sql, name, branchId, id, false);
			return;
		}

		// 修改密码的版本(修改了loginId，必须重置)
		sql = "update SysUser set password=?,name=?,loginId=?,branch.id=? where salesman.id=? and removed=?";

		// password gernate

		String encode_password = MD5.GetMD5String(no + no);

		updateBatch(sql, encode_password, name, no, branchId, id, false);

	}

	@Override
	public void removeBySalesmen(Long[] id) {

		String sql = "update SysUser set removed=? where ";
		QueryJedi qj = new QueryJedi(sql, true);
		qj.eqOr("salesman.id", WxlSugar.asObjects(id));

		updateBatch(qj.hql(), qj.parameters());
	}

	@Override
	public void enableBySalesmen(Long[] id, boolean enable) {

		String sql = "update SysUser set enabled=? where ";
		QueryJedi qj = new QueryJedi(sql, enable);
		qj.eqOr("salesman.id", WxlSugar.asObjects(id));

		updateBatch(qj.hql(), qj.parameters());

	}

	@Override
	public MapBean meta4Login(String username, String password) {
		String sql = "select u.branch.id as branchId,u.password as password,u.id as id from SysUser u where u.loginId=? and u.removed=? and u.enabled=?";
		MapBean mb = mapBean(sql, username, false, true);
		if (mb == null)
			return null;

		String db_password = mb.string("password");
		String encode_password = MD5.GetMD5String(username + password);
		if (!StringUtils.equals(db_password, encode_password)) {
			return null;
		}
		mb.remove("password");
		return mb;
	}
}
