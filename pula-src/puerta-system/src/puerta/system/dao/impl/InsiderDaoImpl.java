package puerta.system.dao.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.PuertaWeb;
import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.mls.Mls;
import puerta.support.utils.MD5;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.InsiderCondition;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.dao.InsiderDao;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.ModuleDao;
import puerta.system.dao.PurviewDao;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Insider;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.service.SessionService;
import puerta.system.transfer.InsiderTransfer;
import puerta.system.vo.ActorWithPurview;
import puerta.system.vo.ActorWithPurviewAll;

@Repository
public class InsiderDaoImpl extends HibernateGenericDao<Insider, String>
		implements InsiderDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private SessionService sessionService;
	@Resource
	private ActorPurviewDao actorPurviewDao;
	private PurviewDao purviewDao;
	@Resource
	private ModuleDao moduleDao;
	@Resource
	private Mls mls;

	public InsiderDaoImpl() {
		// System.err.println("hohoho");
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(InsiderDaoImpl.class);

	public Insider _doLogin(String loginId, String password) {

		String hql = "SELECT a FROM Insider as a WHERE a.loginId=? AND a.password=? AND a.enabled=?";

		String encode_password = MD5.GetMD5String(loginId + password);
		List<Insider> list = find(hql, new Object[] { loginId, encode_password,
				Boolean.TRUE });

		Iterator<Insider> iter = list.iterator();
		if (iter.hasNext()) {
			Insider insider = iter.next();

			loggerDao.doLog(mls.t("platform.admin.login"), insider);

			return insider;
		} else {
			logger.debug("Insider(" + loginId + ") not found");
			// String extendInfo = "ID=" + loginId + ";pass=" + password;
			// loggerDao.doLog(mls.t("platform.admin.loginFail"), extendInfo,
			// (Insider) null);
			Mls.raise("platform.admin.loginError");
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.walnut.system.InsiderDao#doChangePassword(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void doChangePassword(String id, String oldPassword,
			String newPassword) {
		Insider insider = findById(id);
		// check old password ;
		String loginId = insider.getLoginId();
		String password = oldPassword;
		String encode_password = MD5.GetMD5String(loginId + password);
		// String old_encode_password = MD5.GetMD5String(loginId + password);
		if (encode_password.equals(insider.getPassword())) {
			encode_password = MD5.GetMD5String(loginId + newPassword);
			insider.setPassword(encode_password);
		} else {
			Mls.raise("platform.admin.passwordError");
		}

		loggerDao.doLog(mls.t("platform.insider.editPassword"), insider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.walnut.system.InsiderDao#doLogout(java.lang.String)
	 */
	public void doLogout(String id) {
		// Insider u = (Insider) this.findById(Insider.class, id);
		// UserLog sl = new UserLog(u);
		// sl.setEvent(mls.t("platform.insider.logout"));
		// systemLogDao.doSaveSystemLog(sl);

		loggerDao.doLog(mls.t("platform.admin.logout"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.insider.InsiderDao#save(com.nut.groundwork
	 * .platform.po.Insider)
	 */
	public Insider save(Insider insider) {
		if (exists(insider.getLoginId())) {

			Mls.raise("platform.insider.accountExists", insider.getLoginId());
		}

		String loginId = insider.getLoginId();
		String password = insider.getPassword();
		String encode_password = MD5.GetMD5String(loginId + password);
		insider.setPassword(encode_password);
		insider.setEnabled(true);
		loggerDao.doLog(mls.t("platform.insider.add"), insider.getLoginId()
				+ "(" + insider.getName() + ")");
		getHibernateTemplate().save(insider);

		return insider;
	}

	/**
	 * @param loginId
	 * @return
	 */
	private boolean exists(String loginId) {
		if (findByLoginId(loginId) != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.insider.InsiderDao#findByLoginId(java.lang
	 * .String)
	 */
	public Insider findByLoginId(String loginId) {
		List<Insider> lst = this.findByProperty("loginId", loginId);
		if (lst.size() == 0) {
			return null;
		}
		return lst.get(0);

	}

	public PaginationSupport<Insider> search(InsiderCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("loginId"));
	}

	public DetachedCriteria makeDetachedCriteria(InsiderCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		if (!StringUtils.isEmpty(condition.getLoginId())) {
			dc.add(Restrictions.like("loginId", condition.getLoginId(),
					MatchMode.ANYWHERE));
		}

		return dc;

	}

	public Insider update(Insider insider, boolean changePassword) {
		Insider po = findById(insider.getId());
		po.setName(insider.getName());
		po.setComments(insider.getComments());

		if (existsLoginId(insider.getLoginId(), insider.getId())) {
			Mls.raise("platform.insider.accountExists", insider.getLoginId());
		}

		po.setLoginId(insider.getLoginId());

		if (po.getLoginId().toLowerCase().equals("insider")) {
			Mls.raise("platform.insider.cannotEdit");
		}

		if (changePassword) {
			doChangePassword(insider.getId(), insider.getPassword());
		}

		getHibernateTemplate().update(po);

		loggerDao.doLog(
				mls.t("platform.insider.edit"),
				insider.getLoginId() + "(" + insider.getName() + "):"
						+ mls.t("platform.insider.editPassword")
						+ changePassword);

		return po;
	}

	private void doChangePassword(String id, String password) {
		Insider insider = this.findById(id);
		// check old password ;
		String loginId = insider.getLoginId();
		String encode_password = MD5.GetMD5String(loginId + password);
		insider.setPassword(encode_password);

	}

	private boolean existsLoginId(String loginId, String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> f = new HashMap<String, Object>();

		map.put("loginId", loginId);
		f.put("id", id);
		return this.exists(map, f);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.pew.platform.insider.InsiderDao#doDefaultInsider()
	 */
	public Insider doDefaultInsider() {
		String id = (String)sessionService.getActorId();
		Insider ins;
		if (id != null) {
			ins = new Insider();
			ins.setId(id);
			return ins;
		}

		ins = findByLoginId("tiyi");
		if (ins != null)
			return ins;

		ins = new Insider();
		ins.setLoginId("tiyi");
		ins.setPassword("tiyi");
		ins.setEnabled(true);
		ins.setComments("Created By Default");
		ins.setName(mls.t("platform.insider.tiyi"));
		ins.setRemoved(false);
		return save(ins);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.insider.InsiderDao#doLogin(java.lang.String,
	 * java.lang.String)
	 */
	public ActorWithPurview<Insider> doLogin(String loginId, String password) {
		Insider ad = this._doLogin(loginId, password);
		String menu = actorPurviewDao.loadMenu(ad.getId(),
				PuertaWeb.AS_INSIDER, true);
		return new ActorWithPurview<Insider>(ad, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.insider.InsiderDao#beforeSetupPurview(java.lang.String)
	 */
	public ActorWithPurviewAll<Insider> beforeSetupPurview(String id) {
		List<Purview> purviews = purviewDao.loadAll(PuertaWeb.AS_INSIDER);
		List<Purview> checkedPurviews = actorPurviewDao.loadByActor(id,
				PuertaWeb.AS_INSIDER);
		List<Module> module = moduleDao.loadAll(PuertaWeb.AS_INSIDER);
		Insider is = this.findById(id);

		return new ActorWithPurviewAll<Insider>(is, purviews, checkedPurviews,
				module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.insider.InsiderDao#checkLogin(java.lang.String,
	 * java.lang.String)
	 */
	public boolean checkLogin(String no, String password) {
		try {
			this._doLogin(no, password);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doClear()
	 */
	public void doClear() {

		List<String> ids = find("SELECT id FROM Insider");
		// clear purviews
		String hql = "DELETE FROM ActorPurview WHERE actorId=? ";
		for (String id : ids)
			delete(hql, id);
		delete("DELETE FROM InsiderLog");
		delete("DELETE FROM Insider");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<Insider> doExports(String afNo) {
		return find("FROM Insider WHERE removed=?", false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<Insider> objs, boolean update) {

		for (Insider ins : objs) {
			boolean u = update;
			if (u) {
				Insider po = this.findByLoginId(ins.getLoginId());
				if (po == null) {
					u = false;

				} else {
					ins.setId(po.getId());
					update(ins, false);
				}
			}

			if (!u) {
				ins.setPassword(ins.getLoginId());
				ins = save(ins);
			}

			actorPurviewDao.doAssignAll(ins.getId(), PuertaWeb.AS_INSIDER);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getPluginName()
	 */
	public String getPluginName() {
		return getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<Insider> getTransfer() {
		return new InsiderTransfer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginActor#getActorId(java.lang.String)
	 */
	public String getActorId(String no) {
		String hql = "select id from Insider WHERE removed=? AND loginId=?";
		return findSingle(hql, false, no);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginActor#getApplicableScopeNo()
	 */
	public String getAppFieldNo() {
		return PuertaWeb.AS_INSIDER;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginActor#getActorsByRole(int)
	 */
	public List<Insider> getActorsByRole(String rno) {

		return null;
	}
}
