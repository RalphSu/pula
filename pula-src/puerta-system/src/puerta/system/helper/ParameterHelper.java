/**
 * Created on 2007-5-7 08:24:17
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.helper;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.po.Parameter;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterHelper {

	public static final String YES = "yes";

	public static final String NO = "no";

	public static final String KEY_CODE = "KEY_CODE";

	public static final String APP_MODE = "APP_MODE";

	public static final String WORK_DIR = "WORK_PATH";

	public static final String INIT_CLASS = "INIT_CLASS";

	public static final String STATIC_PATH = "STATIC_PATH";

	public static final String CORPBRANCH_AUTO_DATARANGE = "CORPBRANCH_AUTO_DATARANGE";

	public static final String PURVIEW_CHECK_PASS_NOTREGED = "PURVIEW_CHECK_PASS_NOTREGED";

	public static final String PURVIEW_CHECK_FORCE_SESSION = "PURVIEW_CHECK_FORCE_SESSION";

	public static final String TIME_OUT_MINS = "TIME_OUT_MINS";

	public static final String ATOM_PACK_PATH = "ATOM_PACK_PATH";

	public static final String DEV_MODE = "DEV_MODE";

	public static int[] paramTypes = new int[] { Parameter.TYPE_STRING,
			Parameter.TYPE_FILE_PATH, Parameter.TYPE_WEB_PATH,
			Parameter.TYPE_BOOL, Parameter.TYPE_TIME, Parameter.TYPE_DATE,
			Parameter.TYPE_DATETIME, Parameter.TYPE_INT, Parameter.TYPE_DOUBLE,
			Parameter.TYPE_MONEY, Parameter.TYPE_FILE, Parameter.TYPE_DICT,
			Parameter.TYPE_CLASSPATH, Parameter.TYPE_PASSWORD

	};

	public static String[] paramTypesName = new String[] {
			"web.platform.string", "web.platform.filePath",
			"web.platform.webPath", "web.platform.bool", "web.platform.time",
			"web.platform.date", "web.platform.dateTime",
			"web.platform.integer", "web.platform.float", "web.platform.money",
			"web.platform.file", "web.platform.dict", "web.platform.classPath",
			"web.platform.password" };

	public static String getName(int type) {
		return SelectOption.getName(type, paramTypes, paramTypesName);
	}

	public static SelectOptionList getParamTypes(int type) {
		return SelectOption.getList(type, paramTypes, paramTypesName);
	}

}
