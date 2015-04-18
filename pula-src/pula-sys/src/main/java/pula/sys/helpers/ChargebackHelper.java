package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.Chargeback;

public class ChargebackHelper {

	private static final int[] STATUSES = new int[] { Chargeback.STATUS_INPUT,
			Chargeback.STATUS_CONFIRM };

	private static final String[] STATUSES_NAMES = new String[] { "待审", "已审",
			"完成" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, STATUSES, STATUSES_NAMES);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, STATUSES, STATUSES_NAMES);
	}

}
