package puerta.system.base;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import puerta.support.NotFoundException;
import puerta.support.NotLoginException;
import puerta.support.mls.Mls;
import puerta.support.mls.MlsException;
import puerta.support.utils.StringTool;
import puerta.system.keeper.AppFieldKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.AppFieldData;

public class PuertaExceptionResolver extends SimpleMappingExceptionResolver {

	@Resource
	Mls mls;
	@Resource
	SessionService sessionService;
	@Resource
	AppFieldKeeper appFieldKeeper;

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView = super.resolveException(request, response,
				handler, ex);
		if (ex instanceof NotFoundException) {
			modelAndView.setViewName("notFound");
			response.setStatus(404);
			return modelAndView;
		}

		// ex.printStackTrace();
		printStackTrace(ex);
		if (ex instanceof MlsException) {
			ex = new RuntimeException((mls.parseException((MlsException) ex)));
			modelAndView.addObject("exception", ex);
		}

		if (ex instanceof org.springframework.web.multipart.MaxUploadSizeExceededException) {
			MaxUploadSizeExceededException exx = ((MaxUploadSizeExceededException) ex);
			ex = new RuntimeException(mls.t("upload.maxSize",
					String.valueOf(exx.getMaxUploadSize() / (1024))));
			modelAndView.addObject("exception", ex);
		}

		if (handler instanceof PuertaExceptionHandler) {
			PuertaExceptionHandler h = (PuertaExceptionHandler) handler;
			if (h.handleException(ex)) {
				return h.processException(request, response, ex, modelAndView);
			}
		}

		if (modelAndView != null) {
			String uri = request.getRequestURI();
			String processUri = UrlHelper.processUri(uri, request);
			AppFieldData apData = appFieldKeeper.getAppFieldData(processUri);

			// 加上標記來檢查是不是json格式，如果是json格式應該返回不同的視圖
			if (jsonSupport(request)) {
				modelAndView.setViewName(fixPath(apData, "/jsonError"));
			} else {

				modelAndView.addObject("mls", mls);
				modelAndView.addObject("env", sessionService.env());
				modelAndView.addObject("backURL",
						request.getParameter("backURL"));

				logger.warn("working:" + uri + " handler=" + handler);

				String goThere = "/error";
				if (ex instanceof NotLoginException) {
					goThere = "/go_login";
				}

				modelAndView.setViewName(fixPath(apData, goThere));

			}
		}

		// System.out.println("name=" + apData.getPath()
		// + modelAndView.getViewName());

		return modelAndView;
	}

	private String fixPath(AppFieldData apData, String goThere) {
		if (apData == null) {
			return goThere;
		}
		String cp = (apData.getPath());
		return StringTool.fixPath(cp, goThere);

	}

	private void printStackTrace(Exception ex) {
		ex.printStackTrace();

		String[] st = ExceptionUtils.getStackFrames(ex);
		System.err.println(ex);
		for (int i = 0; i < st.length; i++) {
			if (StringUtils.indexOf(st[i], "puerta.") >= 0) {
				System.err.println(st[i]);
			}
		}

	}

	public static boolean jsonSupport(HttpServletRequest request) {
		// Map m = request.getParameterMap();
		// for (Object key : m.keySet()) {
		// System.out.println(key + "=" + m.get(key));
		// }

		String v = request.getParameter("_json");
		if (v != null) {
			return v.equals("1") || v.equals("true");
		}
		return false;
	}

}
