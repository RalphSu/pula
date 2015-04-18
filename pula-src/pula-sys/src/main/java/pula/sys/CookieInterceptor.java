package pula.sys;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import puerta.support.service.SessionBox;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.vo.AppFieldData;
import pula.sys.miscs.UserUtils;
import pula.sys.services.SysUserService;
import pula.sys.services.UtilsService;

/**
 * 正常访问时的登录判断，以及是否需要导向 向导界面（例如向导未完成）
 * 
 * @author tiyi
 * 
 */

public class CookieInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger
			.getLogger(CookieInterceptor.class);

	public static final String STOP_WATCH = "stop_watch";

	@Resource
	SysUserService sysUserService;
	@Resource
	AppFieldKeeper appFieldKeeper;
	@Resource
	UtilsService utilsService;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// UserRoleAuthorizationInterceptor
		// 根据uri拿到领域对象
		// 根据领域对象key拿到session
		// 注入到sessionService中

		StopWatch sw = new StopWatch();
		sw.start();
		//

		request.setAttribute(STOP_WATCH, sw);
		String uri = request.getRequestURI();

		logger.debug("working:" + uri + " handler=" + handler);
		String processUri = processUri(uri, request);
		AppFieldData apData = appFieldKeeper.getAppFieldData(processUri);

		SessionBox sessionBox = (SessionBox) request.getSession().getAttribute(
				apData.getNo());

		if (sessionBox == null) {
			// login here S

			String authcookie = UserUtils.getCookie(request);

			if (authcookie != null) {
				String[] data = UserUtils.readCookie(authcookie);
				if (data == null || data.length != 2) {
					// cookie无效
					return true;
				}

				String appNo = data[0];
				String userId = data[1];

				if (!StringUtils.equals(appNo, apData.getNo())) {
					// 不是这里的cookie
					return true;
				}
				logger.debug("process login");
				SessionBox user = sysUserService.login(userId);

				if (user != null) { // make sesssion
					request.getSession().setAttribute(apData.getNo(), user);
				}

			}
		}
		if (sessionBox != null)
			sysUserService.tracker(request, sessionBox.getId());

		return true;
	}

	public static String processUri(String uri, HttpServletRequest request) {
		String s = request.getContextPath() + "/service";
		if (uri.startsWith(s)) {
			return uri.substring(s.length(), uri.length());
		}
		return uri;

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		StopWatch sw = (StopWatch) (request.getAttribute(STOP_WATCH));

		if (sw != null) {
			sw.split();
			logger.debug("cost times=" + sw.getSplitTime() + " ms");
			sw.stop();
		}

		if (modelAndView != null) {
			modelAndView.addObject("utils", utilsService);
		}

	}
}
