package pula.sys.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.Pe;

@Controller("myError")
public class ErrorController {

	@RequestMapping
	public ModelAndView notlogin(
			@RequestParam(value = "_json", required = false) boolean _json) {
		if (_json) {
			Pe.raise("尚未登录或登录已超时");
			return null;
		}
		return new ModelAndView("/app/notLogin");

	}

	@RequestMapping
	public void forbidden() {
		Pe.raise("无权限访问");
	}

}
