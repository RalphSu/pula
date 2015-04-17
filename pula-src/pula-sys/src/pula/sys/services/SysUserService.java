package pula.sys.services;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import puerta.support.service.SessionBox;
import puerta.system.dao.SysRoleDao;
import puerta.system.po.SysRole;
import pula.sys.BhzqConstants;
import pula.sys.daos.SysUserDao;
import pula.sys.daos.SysUserGroupDao;
import pula.sys.domains.Salesman;
import pula.sys.domains.SysUser;
import pula.sys.domains.SysUserGroup;

@Service
public class SysUserService {

	@Resource
	SysUserDao sysUserDao;

	@Resource
	SessionUserService sessionUserService;
	@Resource
	SysUserGroupDao sysUserGroupDao;
	@Resource
	SysRoleDao sysRoleDao;

	// @Resource
	// private ViewTrackerDao viewTrackerDao;

	@Transactional
	public SessionBox login(String udata) {
		SysUser a = sysUserDao.findById(udata);
		if (a == null)
			return null;

		return sessionUserService.buildSession(false, null, a);

		// return su;
	}

	/**
	 * 跟踪Tracker
	 * 
	 * @param req
	 * @param user_id
	 */
	@Transactional
	public void tracker(HttpServletRequest req, String user_id) {

		// String id = (req.getParameter("_tracker"));
		//
		// if (StringUtils.isEmpty(id)) {
		// return;
		// }
		//
		// String path = StringUtils.defaultString(req.getRequestURI());
		// if (path.endsWith("/my/_tracker")) {
		// return;
		// }
		//
		// ViewTracker vt = new ViewTracker();
		// vt.setBrowser(req.getHeader("user-agent"));
		// vt.setIp(req.getRemoteAddr());
		// vt.setNo(id);
		// vt.setPath(path);
		// vt.setViewer(SysUser.create(user_id));
		// viewTrackerDao.save(vt);

	}

	public void createSalesmen(Salesman cc) {

		// 创建一个销售人员

		// 找到销售人员组
		String group_id = sysUserGroupDao
				.getIdByNo(BhzqConstants.SYS_USER_GROUP_SALESMEN);

		String role_id = sysRoleDao.getIdByNo(BhzqConstants.ROLE_SALES);

		SysUser su = new SysUser();
		su.setBelongsToGroup(SysUserGroup.create(group_id));
		su.setBranch(cc.getBranch());
		su.setEnabled(true);
		su.setLoginId(cc.getNo());
		su.setName(cc.getName());
		su.setPassword(cc.getNo());
		su.setRole(SysRole.create(role_id));
		su.setSalesman(cc);

		sysUserDao.save(su);

	}

	public void updateSalesmen(Salesman cc) {

		// 修改用户信息，主要是改个名，账号要改也行，就是密码完蛋了

		sysUserDao.updateSalesmen(cc.getName(), cc.getNo(), cc.getBranch()
				.getId(), cc.getId());

	}

	public void removeSalesmen(Long[] id) {

		// 删除销售人员

		// 找到一个删一个呗
		sysUserDao.removeBySalesmen(id);

	}

	public void enableSalesmen(Long[] id, boolean enable) {

		sysUserDao.enableBySalesmen(id, enable);

	}

}
