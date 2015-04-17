/**
 * Created on 2009-8-25
 * WXL 2009
 * $Id$
 */
package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.CourseClient;

/**
 * 
 * @author tiyi
 * 
 */
public class CourseClientHelper {

	public static int[] status = new int[] { CourseClient.STATUS_NEW,
			CourseClient.STATUS_NORMAL, CourseClient.STATUS_LOCKED,
			CourseClient.STATUS_INGORE, CourseClient.STATUS_RENEW };

	public static String[] status_names = new String[] { "申请", "正常", "锁定", "忽略","作废" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, status, status_names);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, status, status_names);
	}
}
