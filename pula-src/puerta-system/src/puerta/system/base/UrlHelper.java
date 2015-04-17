package puerta.system.base;

import javax.servlet.http.HttpServletRequest;

public class UrlHelper {
	public static String processUri(String uri, HttpServletRequest request) {
		String s = request.getContextPath() + "/service";
		if (uri.startsWith(s)) {
			return uri.substring(s.length(), uri.length());
		}
		return uri;

	}
}
