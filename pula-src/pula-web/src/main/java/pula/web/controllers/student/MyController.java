package pula.web.controllers.student;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

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
import pula.controllers.http.JsonResultWithMap;
import pula.web.services.SessionUserService;
import pula.web.services.StudentService;

@Controller("myStudent")
public class MyController {

	@Resource
	private SessionService sessionService;
	@Resource
	private SessionUserService sessionUserService;
	@Resource
	private StudentService studentService;

	@RequestMapping
	public ModelAndView entry() { // 如果没有登录,就去登录

		// 如果已经登录，直接显示首页
		ModelAndView m = new ModelAndView();
		SessionBox sb = sessionService.get();
		if (sb != null) {
			// 加載首頁
			m.addObject("sessionUser", sb);
		} else {
			// 登录
			m.setViewName("student/my/login");
		}
		return m;
	}

	@RequestMapping
	@Transactional(readOnly = true)
	@Barrier()
	public ModelMap welcome() {
		ModelMap m = new ModelMap();
		return m;
	}

	@ResponseBody
	@RequestMapping
	public JsonResult login(@RequestParam("loginId") String loginId,
			@RequestParam(value = "password") String password,
			@RequestParam(value = "remember", required = false) boolean rem,
			HttpServletResponse res) {
		JsonResultWithMap a = studentService.login(loginId, password);
		if (a.isError()) {
			return JsonResult.e(a.getMessage());
		}
		sessionUserService.buildStudentSession(rem, res, a.getData());

		return JsonResult.s();
	}

	@RequestMapping
	@Barrier()
	public ModelAndView logout(HttpServletResponse res) {
		if (sessionService.has()) {
			studentService.logout(sessionService.getActorId());
			sessionService.abandon();
		}

		return new ModelAndView();
	}

	@RequestMapping
	@Barrier()
	public ModelMap changePassword() {
		SessionBox sb = sessionService.get();
		return new ModelMap("sessionUser", sb);
	}

	@ResponseBody
	@RequestMapping
	@Barrier()
	public JsonResult _changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam(value = "newPassword") String newPassword) {

		return studentService.updatePassword(sessionService.getActorId(),
				oldPassword, newPassword);

	}
}
