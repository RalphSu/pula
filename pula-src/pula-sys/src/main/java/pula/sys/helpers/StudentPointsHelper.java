package pula.sys.helpers;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.StudentPoints;

public class StudentPointsHelper {

	public static final int[] TYPES = new int[] {
			StudentPoints.FROM_ORDER_FORM, StudentPoints.FORM_TAKE_CLASS,
			StudentPoints.FROM_COUSE_GAME, StudentPoints.FROM_MANUAL,
			StudentPoints.FROM_EXCHANGE };

	public static final String[] TYPES_NAME = new String[] { "订单", "上课", "游戏",
			"手动", "兑换" };

	public static SelectOptionList getType(int n) {
		return SelectOption.getList(n, TYPES, (TYPES_NAME));
	}

	public static String getTypeName(int n) {
		return SelectOption.getName(n, TYPES, (TYPES_NAME));
	}

}
