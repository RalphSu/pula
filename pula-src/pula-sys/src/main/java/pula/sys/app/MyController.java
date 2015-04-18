package pula.sys.app;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.annotation.Barrier;
import puerta.support.service.SessionBox;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import pula.sys.daos.SysUserDao;
import pula.sys.domains.SysUser;
import pula.sys.miscs.UserUtils;
import pula.sys.services.SessionUserService;

@Controller("myWebUser")
public class MyController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyController.class);

	@Resource
	private SessionService sessionService;

	@Resource
	private SysUserDao sysUserDao;

	@Resource
	private SessionUserService sessionUserService;

	// @Resource
	// private ViewTrackerDao viewTrackerDao;

	@RequestMapping
	public ModelAndView entry() { // 如果没有登录,就去登录

		// 如果已经登录，直接显示首页 // sessionService.get() ModelAndView m = new
		ModelAndView m = new ModelAndView();
		SessionBox sb = sessionService.get();
		if (sb != null) {
			// 加載首頁
			m.addObject("sessionUser", sb).addObject("headquarter",
					sessionUserService.isHeadquarter());
		} else {
			// 登录
			m.setViewName("app/my/login");
		}
		return m;
	}

	@RequestMapping
	@Transactional(readOnly = true)
	@Barrier()
	public ModelMap welcome() {
		logger.debug("welecome method");
		ModelMap m = new ModelMap();
		return m;
	}

	@ResponseBody
	@Transactional
	@RequestMapping
	public JsonResult login(@RequestParam("loginId") String loginId,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "remember", required = false) boolean rem,
			HttpServletResponse res) {
		SysUser a = sysUserDao.doLogin(loginId, password);
		sessionUserService.buildSession(rem, res, a);

		return JsonResult.s();
	}

	@RequestMapping
	@Transactional
	@Barrier()
	public ModelAndView logout(HttpServletResponse res) {
		if (sessionService.has()) {
			sysUserDao.doLogout(sessionService.getActorId());
			sessionService.abandon();
		}

		// remove
		Cookie c = UserUtils.empty();
		res.addCookie(c);

		return new ModelAndView();
	}

	@RequestMapping
	@Barrier()
	public ModelAndView window(@RequestParam("u") String url) {
		return new ModelAndView().addObject("u", url);
	}

	@RequestMapping
	@Barrier()
	public ModelMap changePassword() {
		SessionBox sb = sessionService.get();
		return new ModelMap("sessionUser", sb);
	}

	@ResponseBody
	@Transactional
	@RequestMapping
	@Barrier()
	public JsonResult _changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam(value = "newPassword") String newPassword) {

		sysUserDao.updatePassword(sessionService.getActorId(), oldPassword,
				newPassword);

		return JsonResult.s();
	}

	// @ResponseBody
	// @Transactional
	// @RequestMapping
	// public String _tracker(
	// @RequestParam(value = "p", required = false) String p,
	// @RequestParam(value = "id", required = false) String id,
	// @RequestHeader("user-agent") String user_agent,
	// HttpServletRequest req) {
	//
	// ViewTracker vt = new ViewTracker();
	// vt.setBrowser(user_agent);
	// vt.setIp(req.getRemoteAddr());
	// vt.setNo(id);
	// vt.setPath(p);
	// vt.setViewer(SysUser.create(sessionService.getActorId()));
	// viewTrackerDao.save(vt);
	//
	// return "{}";
	// }
}
