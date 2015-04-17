package puerta.system.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface PuertaExceptionHandler {

	ModelAndView processException(HttpServletRequest request,
			HttpServletResponse response, Exception ex,
			ModelAndView modelAndView);

	boolean handleException(Exception ex);

}
