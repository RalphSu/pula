package pula.web.controllers.teacher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.service.SessionBox;
import puerta.support.utils.StringTool;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;

@RequestMapping
@Controller("defaultTeacherConsole")
public class DefaultController {

	@Resource
	private SessionService sessionService;
	@Resource
	ParameterKeeper parameterKeeper;

	@RequestMapping("/teacher")
	public ModelAndView _default(HttpServletRequest req) {

		ModelAndView FORWARD = new ModelAndView("/forward");
		String cp = req.getContextPath();
		return FORWARD.addObject("goURL", StringTool.fixPath(cp, "/teacher/"));
	}

	private ModelAndView entry() {
		ModelAndView m = new ModelAndView();
		SessionBox sb = sessionService.get();
		if (sb != null) {
			// 加載首頁
			m.addObject("sessionUser", sb);
			m.setViewName("teacher/my/entry");
		} else {
			// 登录
			m.setViewName("teacher/my/login");
		}
		return m;
	}

	@RequestMapping("/teacher/")
	public ModelAndView _default2() {
		return entry();
	}
}
