/**
 * Created on 2009-8-25
 * WXL 2009
 * $Id$
 */
package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.MaterialRequire;

/**
 * 
 * @author tiyi
 * 
 */
public class MaterialRequireHelper {

	public static int[] status = new int[] { MaterialRequire.STATUS_INPUT,
			MaterialRequire.STATUS_SUBMIT, MaterialRequire.STATUS_ACCEPT,
			MaterialRequire.STATUS_REJECT, MaterialRequire.STATUS_SENT,
			MaterialRequire.STATUS_DELIVERIED };

	public static String[] status_names = new String[] { "录入", "提交", "已审",
			"拒绝", "发出", "送达" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, status, status_names);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, status, status_names);
	}

}
