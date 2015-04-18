package pula.sys.app;

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
@Controller("defaultConsole")
public class DefaultController {

	
	@Resource
	private SessionService sessionService;
	@Resource
	ParameterKeeper parameterKeeper;

	@RequestMapping("/app")
	public ModelAndView _default(HttpServletRequest req) {

		ModelAndView FORWARD = new ModelAndView("/forward");
		String cp = req.getContextPath();
		return FORWARD.addObject("goURL", StringTool.fixPath(cp, "/app/"));
	}

	private ModelAndView entry() {
		ModelAndView m = new ModelAndView();
		SessionBox sb = sessionService.get();
		if (sb != null) {
			// 加載首頁
			m.addObject("sessionUser", sb);
			m.setViewName("app/my/entry");
		} else {
			// 登录
			m.setViewName("app/my/login");
		}
		return m;
	}

	@RequestMapping("/app/")
	public ModelAndView _default2() {
		return entry();
	}
}
