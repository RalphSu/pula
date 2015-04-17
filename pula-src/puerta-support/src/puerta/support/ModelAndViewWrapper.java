package puerta.support;

import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewWrapper {

	private ModelAndView mv;

	public ModelAndViewWrapper(ModelAndView mv) {
		this.mv = mv;
	}

	public ModelAndViewWrapper add(Object... objects) {

		if (objects.length % 2 != 0) {
			Pe.raise("not match");
		}
		for (int i = 0; i < objects.length; i += 2) {
			String key = (String) objects[i];
			Object v = objects[i + 1];
			mv.addObject(key, v);
		}

		return this;
	}

	public ModelAndView to() {
		return this.mv;
	}
}
