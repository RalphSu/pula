package puerta.system.base;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import puerta.PuertaWeb;
import puerta.support.mls.Mls;
import puerta.support.mls.SystemLanguageSource;
import puerta.support.service.Environment;
import puerta.support.service.SessionBox;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.keeper.RequestUriKeeper;
import puerta.system.keeper.VendeeKeeper;
import puerta.system.po.RequestUri;
import puerta.system.service.PurviewService;
import puerta.system.service.RequestHistoryService;
import puerta.system.service.SessionService;
import puerta.system.vo.AppFieldData;
import puerta.system.vo.RollingStone;

public class CheckInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger
			.getLogger(CheckInterceptor.class);
	@Resource
	private SessionService sessionService;
	@Resource
	private AppFieldKeeper appFieldKeeper;
	@Resource
	private Mls mls;
	@Resource
	private SystemLanguageSource systemLanguageSource;
	@Resource
	private RequestUriKeeper requestUriKeeper;
	@Resource
	private PurviewService purviewService;
	@Resource
	private VendeeKeeper vendeeKeeper;
	@Resource
	private ParameterKeeper parameterKeeper;
	@Resource
	private RequestHistoryService requestHistoryService;

	public static boolean ROLLING_STONE = false;
	public static final String NOT_LOGIN = "/error/notlogin";
	public static final String FORBIDDEN = "/error/forbidden";
	public static final String MY_ENTRY = "/my/entry";
	public static final String MY_LOGIN = "/my/login";
	public static final String SETUP_BEGIN = "/setup/begin";
	public static final String SETUP_SETUP = "/setup/setup";
	public static final String SETUP_REGISTER = "/setup/register";
	public static final String SETUP_REGISTER2 = "/setup/_register";
	public static final String[] IGNORE = new String[] { NOT_LOGIN, FORBIDDEN,
			MY_ENTRY, MY_LOGIN, SETUP_BEGIN, SETUP_SETUP, SETUP_REGISTER,
			SETUP_REGISTER2 };

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// UserRoleAuthorizationInterceptor
		// 根据uri拿到领域对象
		// 根据领域对象key拿到session
		// 注入到sessionService中
		//

		// messageSource.getMessage(arg0, Locale.CANADA)
		String uri = request.getRequestURI();

		logger.debug("working:" + uri + " handler=" + handler);
		String processUri = UrlHelper.processUri(uri, request);
		// if (1 == 1 && uri.endsWith("welcome")) {
		// String url = "../" + NOT_LOGIN;
		// request.getRequestDispatcher(url).forward(request, response);
		// // response.sendRedirect(url);
		// return false;
		// }

		AppFieldData apData = appFieldKeeper.getAppFieldData(processUri);
		String ip = request.getRemoteAddr();
		String backURL = request.getParameter("backURL");
		if (apData != null) {
			Environment env = Environment.create(apData.getNo(), ip,
					mls.getCurrentLanguage(), backURL, processUri);
			env.setLang(getLang(request));
			sessionService.set(env);
			SessionBox sessionBox = (SessionBox) request.getSession()
					.getAttribute(apData.getNo());
			sessionService.set(sessionBox);

			// 是否跳过检查
			for (String ignore : IGNORE) {
				if (uri.endsWith(ignore)) {
					return true;
				}
			}
			if (vendeeKeeper.isExpired() || vendeeKeeper.isInvalid()) {
				logger.warn("vendee is expired or invalid");
				return false;
			}

			if (ROLLING_STONE) {
				// env
				SessionBox insiderSb = (SessionBox) request.getSession()
						.getAttribute(PuertaWeb.AS_INSIDER);
				RollingStone rs = RollingStone.get(insiderSb);
				rs.add(env);
			} else {

				// 检查开始了
				RequestUri ruri = requestUriKeeper.getRequestUri(processUri);
				if (ruri != null && ruri.isOnlineCheck()) {
					logger.debug("check online=" + ruri.getUri());
					// 检查是否在线
					if (sessionBox == null) {
						// Pe.raise("登录超时,请重新登录");
						String url = "../" + NOT_LOGIN;
						request.getRequestDispatcher(url).forward(request,
								response);
						return false;
					}
					// 检查是否允许
					if (ruri.isPurviewCheck()) {
						logger.debug("check purview=" + ruri.getUri());
						if (!purviewService.check(ruri,
								sessionBox.getPurviewActorId())) {
							// 转向到处理的页面去
							String url = "../" + FORBIDDEN;
							request.getRequestDispatcher(url).forward(request,
									response);
							return false;
						}
					}
				}

				if (parameterKeeper.getBoolean(PuertaWeb.RECORD_HISTORY, false)) {
					logger.debug("recording requestHistory=" + processUri);
					requestHistoryService.record(sessionBox, processUri,
							apData, request);
				}

			}

		}
		logger.debug("preHandle done=" + uri);
		return true;
	}

	private String getLang(HttpServletRequest request) {
		Cookie[] cs = request.getCookies();
		if (cs == null)
			return systemLanguageSource.getCurrentLanguage();
		String lang = null;
		for (Cookie c : cs) {
			if (StringUtils.equals("p-lang", c.getName())) {
				lang = c.getValue();
			}
		}
		if (StringUtils.isEmpty(lang)) {
			return systemLanguageSource.getCurrentLanguage();
		}
		return lang;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("postHandle enter:" + request.getRequestURI());
		// 检查sessionService中的session对象是否更新过。【用版本号】
		// 如果已经更新过就提交到request
		String uri = request.getRequestURI();
		String processUri = UrlHelper.processUri(uri, request);
		SessionBox sb = sessionService.get();
		if (sb != null && sb.committable()) {
			AppFieldData apData = appFieldKeeper.getAppFieldData(processUri);
			if (apData != null) {
				if (sb.isExpired()) {
					request.getSession().removeAttribute(apData.getNo());
					sb = null;
				} else {
					request.getSession().setAttribute(apData.getNo(), sb);
				}
			}
		}

		if (!PuertaExceptionResolver.jsonSupport(request)
				&& modelAndView != null) {
			modelAndView.addObject("mls", mls);
			modelAndView.addObject("env", sessionService.env());
			modelAndView.addObject("backURL", request.getParameter("backURL"));
		}

		if (modelAndView != null) {
			if (modelAndView.getModel().containsKey(PuertaWeb.PARAMETER_MAP)) {
				// Pe.raise("already got key=" + PuertaWeb.PARAMETER_MAP);
				logger.warn("already got key=" + PuertaWeb.PARAMETER_MAP);
			} else {
				modelAndView.addObject(PuertaWeb.PARAMETER_MAP,
						parameterKeeper.complied);
			}

			if (modelAndView.getModel().containsKey(PuertaWeb.SESSION_BOX)
					|| sb == null) {
				// Pe.raise("already got key=" + PuertaWeb.PARAMETER_MAP);
				logger.warn("already got key=" + PuertaWeb.SESSION_BOX + "&sb="
						+ sb);
			} else if (!sb.isExpired()) {
				modelAndView.addObject(PuertaWeb.SESSION_BOX, sb);
			}
		}

		try {
			super.postHandle(request, response, handler, modelAndView);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		logger.debug("finish:" + request.getRequestURI());
	}
}
