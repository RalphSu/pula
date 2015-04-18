package pula.sys;

import puerta.PuertaWeb;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;

public class BaseHelper {

	private static final String[] ENABLED_STATUS = new String[] { "启用", "禁用" };

	private static final int[] GENDER_TYPES = new int[] {
			BhzqConstants.GENDER_MALE, BhzqConstants.GENDER_FEMALE };
	private static final String[] GENDER_NAMES = new String[] { "男", "女" };

	public static SelectOptionList getEnabledStatus(int i) {
		return PuertaWeb.getYesNoList(i, ENABLED_STATUS).reverse();
	}

	public static SelectOptionList getGenderList(int i) {
		return SelectOption.getList(i, GENDER_TYPES, GENDER_NAMES);
	}

	public static String getGenderName(int n) {
		return SelectOption.getName(n, GENDER_TYPES, GENDER_NAMES);
	}

}
