package pula.sys.miscs;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class UserUtils {

	public static final String AUTH = "hgsys_auth";
	public static final String KEY = "hgsysherepwd";

	public static Cookie buildCookie(String appFieldNo, String id) {
		Cookie c = new Cookie(AUTH, _buildCookie(appFieldNo, id));
		c.setPath("/");

		return c;
	}

	private static String _buildCookie(String appFieldNo, String id) {
		try {
			DESSSO des = new DESSSO(KEY);// 自定义密钥
			return des.encrypt(appFieldNo + "\t" + String.valueOf(id));
		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;
	}

	public static String getCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		String v = null;
		for (Cookie c : cookies) {
			if (StringUtils.equals(c.getName(), AUTH)) {
				v = c.getValue();
			}
		}

		if (v == null) {
			return v;
		}

		try {
			DESSSO des = new DESSSO(KEY);// 自定义密钥
			return des.decrypt(v);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return v;

	}

	public static String[] readCookie(String authcookie) {
		String[] ss = StringUtils.split(authcookie, "\t");
		return ss;
	}

	public static Cookie empty() {
		Cookie c = new Cookie(AUTH, "");
		c.setPath("/");
		c.setMaxAge(0);
		return c;

	}

}
