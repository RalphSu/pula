/**
 * Created on 2009-8-25
 * WXL 2009
 * $Id$
 */
package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.GiftTransfer;

/**
 * 
 * @author tiyi
 * 
 */
public class GiftTransferHelper {

	public static int[] status = new int[] { GiftTransfer.STATUS_INPUT,
			GiftTransfer.STATUS_SUBMIT, GiftTransfer.STATUS_SENT,
			GiftTransfer.STATUS_DELIVERIED };

	public static String[] status_names = new String[] { "录入", "提交", "发出", "送达" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, status, status_names);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, status, status_names);
	}

}
