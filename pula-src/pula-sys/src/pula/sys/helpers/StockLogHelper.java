package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.StockLog;

public class StockLogHelper {

	private static final int[] TYPES = new int[] { StockLog.IN, StockLog.OUT };

	private static final String[] TYPES_NAMES = new String[] { "进入", "离开" };

	public static SelectOptionList getTypes(int n) {
		return SelectOption.getList(n, TYPES, TYPES_NAMES);
	}

	public static String getTypeName(int n) {
		return SelectOption.getName(n, TYPES, TYPES_NAMES);
	}

}
