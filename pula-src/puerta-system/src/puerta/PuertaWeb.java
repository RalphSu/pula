package puerta;

import org.springframework.http.MediaType;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;

/**
 * Created on 2008-12-20 04:28:36
 *
 * NILVAR 2008
 * $Id$
 */

/**
 * 
 * @author tiyi
 * 
 */
public class PuertaWeb {

	// public static final String ADMIN_SESSION = "adminSession";
	public static final String CURRENT_LANGUAGE = "CURRENT_LANGUAGE";
	public static final String LANGUAGE_SUPPORT_CONFIG = "languageSupport.properties";
	public static final String AS_ADMIN = "admin";
	public static final String AS_WEBUSER = "webUser";
	public static final String AS_INSIDER = "insider";

	// params of mail
	// mail
	public static final String SMTP_HOST = "SMTP_HOST";
	public static final String SMTP_FROM = "SMTP_FROM";
	public static final String SMTP_FROM_NAME = "SMTP_FROM_NAME";
	public static final String SMTP_PASSWORD = "SMTP_PASSWORD";
	public static final String SMTP_USER = "SMTP_USER";
	public static final String SMTP_PORT = "SMTP_PORT";

	// system parameters
	public static final String STATIC_PATH = "STATIC_PATH";
	public static final String CHECK_ACTION = "CHECK_ACTION";

	public static final String RECORD_HISTORY = "P_RECORD_HISTORY";

	// parameters of action check

	public static final String ROLLING_STONE = "ROLLING_STONE";
	public static final MediaType CSV = new MediaType("application", "csv");
	public static final String PUERTA_NO = "PUERTA_NO";
	public static final int YES = 1;
	public static final int NO = -1;
	public static final int NONE = 0;
	public static final String PARAMETER_MAP = "_pars";
	public static final String SESSION_BOX = "_sess";

	public static final int[] YES_NO = new int[] { YES, NO };
	public static final int[] ALL_YES_NO = new int[] { NONE, YES, NO };
	public static final String SESSION_PURVIEW = "SESSION_PURVIEW";

	public static SelectOptionList getYesNoList(int n, String[] payStatus) {
		return SelectOption.getList(n, YES_NO, payStatus);
	}

}
