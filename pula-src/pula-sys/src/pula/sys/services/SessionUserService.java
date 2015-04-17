/**
 * Created on 2010-1-24
 * WXL 2009
 * $Id$
 */
package pula.sys.services;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import puerta.PuertaWeb;
import puerta.support.Pe;
import puerta.support.service.SessionBox;
import puerta.support.vo.NoNameVo;
import puerta.system.dao.ActorPurviewDao;
import puerta.system.service.SessionService;
import puerta.system.vo.PurviewHolder;
import pula.sys.BhzqConstants;
import pula.sys.domains.SysUser;
import pula.sys.helpers.HgMenuHelper;
import pula.sys.miscs.UserUtils;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class SessionUserService {

	@Resource
	private SessionService sessionService;
	@Resource
	private ActorPurviewDao actorPurviewDao;

	/**
	 * @return
	 */
	public String getActorId() {
		return sessionService.getActorId();
	}

	/**
	 * @param string
	 * @return
	 */
	public PurviewHolder loadPurview(String string) {
		Map<String, String> ps = actorPurviewDao.getPurviewUnder(string,
				getActorId());
		PurviewHolder ph = new PurviewHolder(ps);
		ph.setRole(((NoNameVo) this.sessionService
				.getProp(BhzqConstants.SESSION_ROLE)).getNo());
		return ph;
	}

	public NoNameVo getRole() {
		return this.sessionService.getProp(BhzqConstants.SESSION_ROLE);
	}

	public <T> T getProps(String no) {
		return sessionService.getProp(no);
	}

	public boolean teachDirector() {
		NoNameVo role = getRole();
		String no = role.getNo();
		if (BhzqConstants.ROLE_TEACH_DIRECTOR.equals(no)) {
			return true;
		}
		return false;
	}

	public boolean salesDirector() {
		NoNameVo role = getRole();
		String no = role.getNo();
		if (BhzqConstants.ROLE_SALES_DIRECTOR.equals(no)) {
			return true;
		}
		return false;
	}

	public boolean sales() {
		NoNameVo role = getRole();
		String no = role.getNo();
		if (BhzqConstants.ROLE_SALES.equals(no)) {
			return true;
		}
		return false;
	}

	public void only(String... roles) {
		if (!can(roles)) {

			Pe.raise("您没有角色权限访问当前功能");
		}
	}

	public boolean can(String... roles) {
		NoNameVo role = getRole();
		String no = role.getNo();

		for (Object s : ArrayUtils.addAll(roles, new Object[] {
				BhzqConstants.ROLE_ADMIN, BhzqConstants.ROLE_CEO })) {
			if (StringUtils.equals(no, s.toString())) {
				return true;
			}
		}
		return false;
	}

	public void setPartBatch(Map<String, Object> nav) {

	}

	public boolean admin() {
		NoNameVo role = getRole();
		if (role == null)
			return false;
		String no = role.getNo();
		if (BhzqConstants.ROLE_ADMIN.equals(no)) {
			return true;
		}
		return false;
	}

	public SysUser getUser() {
		return SysUser.create(sessionService.getActorId());
	}

	public boolean isHeadquarter() {
		Boolean b = this.sessionService
				.getProp(BhzqConstants.SESSION_HEADQUARTER);
		if (b == null) {
			return false;
		}
		return b;
	}

	public NoNameVo getBranch() {
		return this.sessionService.getProp(BhzqConstants.SESSION_BRANCH);
	}

	public SessionBox buildSession(boolean rem, HttpServletResponse res,
			SysUser a) {
		SessionBox su = new SessionBox();
		su.setId(a.getId());
		su.setLoginId(a.getLoginId());
		su.setName(a.getName());

		// String g = null;
		// if (a.getBelongsToGroup() != null) {
		// g = a.getBelongsToGroup().getId();
		// } else {
		//
		// }

		String menu = HgMenuHelper.toString(
				actorPurviewDao.loadMenuList(a.getId(), PuertaWeb.AS_WEBUSER),
				true);
		su.setMenu(menu);
		su.setPurviewActorId(a.getId());
		if (a.getRole() != null) {
			NoNameVo role = NoNameVo.create(a.getRole().getId(), a.getRole()
					.getNo(), a.getRole().getName());
			su.getProps().put(BhzqConstants.SESSION_ROLE, role);
		}

		// headquarter
		if (a.getBranch() != null) {
			NoNameVo branch = NoNameVo.create(String.valueOf(a.getBranch()
					.getId()), a.getBranch().getNo(), a.getBranch().getName());
			su.getProps().put(BhzqConstants.SESSION_BRANCH, branch);
			su.getProps().put(BhzqConstants.SESSION_HEADQUARTER,
					a.getBranch().isHeadquarter());
		}

		// 如果是工组长，还要加载工组类型
		// Integer[] type_sess = workgroupDao.getTypes(a.getId());
		// su.getProps().put(BhzqConstants.SESSION_TYPES, type_sess);
		//
		// su.getProps().put(BhzqConstants.SESSION_CAM_NO, a.getCamNo());
		// su.getProps().put(BhzqConstants.SESSION_PRINTER, a.getPrinter());

		if (res != null) {
			sessionService.set(su);
			Cookie c = UserUtils.buildCookie(sessionService.env()
					.getAppFieldNo(), su.getId());
			if (rem) {
				c.setMaxAge(31536000);
			}
			res.addCookie(c);
		}
		return su;

	}
}
