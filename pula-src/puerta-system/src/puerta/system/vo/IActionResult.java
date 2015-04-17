/**
 * Created on 2008-3-23 下午06:26:42
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

/**
 * 
 * @author tiyi
 * 
 */
public interface IActionResult {

	public static final String SAVE_SUCCESS = "saveSuccess";

	public static final String JSON_SUCCESS = "jsonSuccess";

	public static final String EXPORT_FILE = "exportFile";

	public static final String GO_URL = "goURL";

	public static final String GO_URL_NO_ALERT = "goURLNoAlert";

	public static final String PAGE_ERROR = "pageError";

	public static final String NO_PURVIEW = "noPurview";

	public static final String NOT_LOGIN = "notLogin";

	public static final String NOT_LOGIN_JSON = "notLoginJson";

	public static final String NOT_REGISTER = "notRegister";

	public static final String MAINTAIN = "maintain";

	public static final String BUSINESS = "business";

	public static final String SYSTEM = "system";

	public static final String JSON_ERROR = "jsonError";

	public static final String STATIC = "static";

	public static final String JSON_LOAD = "jsonLoad";

	public static final String CLOSE_AND_REFRESH = "closeAndRefresh";
}
