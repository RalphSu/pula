/**
 * Created on 2010-1-24
 * WXL 2009
 * $Id$
 */
package pula.services;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import puerta.support.service.SessionBox;
import puerta.support.vo.NoNameVo;
import puerta.system.service.SessionService;
import puerta.system.vo.MapBean;
import pula.BhzqConstants;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class SessionUserService {

	@Resource
	private SessionService sessionService;

	/**
	 * @return
	 */
	public String getActorId() {
		return sessionService.getActorId();
	}

	public <T> T getProps(String no) {
		return sessionService.getProp(no);
	}

	// public SysUser getUser() {
	// return SysUser.create(sessionService.getActorId());
	// }

	// public boolean isHeadquarter() {
	// Boolean b = this.sessionService
	// .getProp(BhzqConstants.SESSION_HEADQUARTER);
	// if (b == null) {
	// return false;
	// }
	// return b;
	// }

	public NoNameVo getBranch() {
		return this.sessionService.getProp(BhzqConstants.SESSION_BRANCH);
	}

	public SessionBox buildTeacherSession(boolean rem, HttpServletResponse res,
			MapBean mb) {
		SessionBox su = new SessionBox();
		su.setId(mb.integer("id").toString());
		su.setLoginId(mb.string("loginId"));
		su.setName(mb.string("name"));

		NoNameVo branch = NoNameVo.create(
				String.valueOf(mb.integer("branchId")), mb.string("branchNo"),
				mb.string("branchName"));

		su.getProps().put(BhzqConstants.SESSION_BRANCH, branch);

		this.sessionService.set(su);

		// String g = null;
		// if (a.getBelongsToGroup() != null) {
		// g = a.getBelongsToGroup().getId();
		// } else {
		//
		// }

		// String menu = HgMenuHelper.toString(
		// actorPurviewDao.loadMenuList(a.getId(), PuertaWeb.AS_WEBUSER),
		// true);
		// su.setMenu(menu);
		// su.setPurviewActorId(a.getId());
		// if (a.getRole() != null) {
		// NoNameVo role = NoNameVo.create(a.getRole().getId(), a.getRole()
		// .getNo(), a.getRole().getName());
		// su.getProps().put(BhzqConstants.SESSION_ROLE, role);
		// }
		//
		// // headquarter
		// if (a.getBranch() != null) {
		// NoNameVo branch = NoNameVo.create(String.valueOf(a.getBranch()
		// .getId()), a.getBranch().getNo(), a.getBranch().getName());
		// su.getProps().put(BhzqConstants.SESSION_BRANCH, branch);
		// su.getProps().put(BhzqConstants.SESSION_HEADQUARTER,
		// a.getBranch().isHeadquarter());
		// }

		// 如果是工组长，还要加载工组类型
		// Integer[] type_sess = workgroupDao.getTypes(a.getId());
		// su.getProps().put(BhzqConstants.SESSION_TYPES, type_sess);
		//
		// su.getProps().put(BhzqConstants.SESSION_CAM_NO, a.getCamNo());
		// su.getProps().put(BhzqConstants.SESSION_PRINTER, a.getPrinter());

		// if (res != null) {
		// sessionService.set(su);
		// Cookie c = UserUtils.buildCookie(sessionService.env()
		// .getAppFieldNo(), su.getId());
		// if (rem) {
		// c.setMaxAge(31536000);
		// }
		// res.addCookie(c);
		// }
		return su;

	}

	public void buildStudentSession(boolean rem, HttpServletResponse res,
			MapBean mb) {
		SessionBox su = new SessionBox();
		su.setId(mb.integer("id").toString());
		su.setLoginId(mb.string("loginId"));
		su.setName(mb.string("name"));

		NoNameVo branch = NoNameVo.create(
				String.valueOf(mb.integer("branchId")), mb.string("branchNo"),
				mb.string("branchName"));

		su.getProps().put(BhzqConstants.SESSION_BRANCH, branch);
		this.sessionService.set(su);

	}
}
