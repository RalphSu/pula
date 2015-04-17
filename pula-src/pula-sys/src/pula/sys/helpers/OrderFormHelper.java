package pula.sys.helpers;

import java.util.Map;

import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.domains.OrderForm;

public class OrderFormHelper {

	private static final int[] COMMISSION_TYPES = new int[] {
			OrderForm.COMMISSION_TYPE_55, OrderForm.COMMISSION_TYPE_28,
			OrderForm.COMMISSION_TYPE_37, OrderForm.COMMISSION_TYPE_100 };

	private static final String[] COMMISSION_TYPES_NAMES = new String[] {
			"5/5", "7/3", "8/2", "单人" };

	private static final String[] COMMISSION_TYPES_KEYS = new String[] { "5",
			"7", "8", "100" };

	private static final String[] COMMISSION_TYPES_KEYS_INVS = new String[] {
			"5", "3", "2", "0" };

	public static SelectOptionList getCommissionTypes(int n) {
		return SelectOption
				.getList(n, COMMISSION_TYPES, COMMISSION_TYPES_NAMES);
	}

	public static String getCommissionTypeName(int n) {
		return SelectOption
				.getName(n, COMMISSION_TYPES, COMMISSION_TYPES_NAMES);
	}

	private static final int[] STATUSES = new int[] { OrderForm.STATUS_INPUT,
			OrderForm.STATUS_CONFIRM, OrderForm.STATUS_COMPLETED,
			OrderForm.STATUS_RETURN };

	private static final String[] STATUSES_NAMES = new String[] { "待审", "已审",
			"完成", "退单" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, STATUSES, STATUSES_NAMES);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, STATUSES, STATUSES_NAMES);
	}

	private static final int[] PAY_STATUSES = new int[] {
			OrderForm.PAY_STATUS_NONE, OrderForm.PAY_STATUS_PREPAY,
			OrderForm.PAY_STATUS_PAID };

	private static final String[] PAY_STATUS_NAMES = new String[] { "未付", "预付",
			"已付" };

	public static SelectOptionList getPayStatusList(int n) {
		return SelectOption.getList(n, PAY_STATUSES, PAY_STATUS_NAMES);
	}

	public static String getPayStatusName(int n) {
		return SelectOption.getName(n, PAY_STATUSES, PAY_STATUS_NAMES);
	}

	public static String getKeyByComissionType(int ct) {
		return SelectOption
				.getName(ct, COMMISSION_TYPES, COMMISSION_TYPES_KEYS);
	}

	// 辅销售反向
	public static String getKeyByComissionTypeInvs(int ct) {
		return SelectOption.getName(ct, COMMISSION_TYPES,
				COMMISSION_TYPES_KEYS_INVS);
	}

	public static Map<String, MapBean> merge(MapList orders, MapList chargebacks) {
		Map<String, MapBean> result = WxlSugar.newHashMap();

		for (MapBean mb : orders) {
			Integer month = mb.asInteger("statMonth") + 1;
			result.put(month.toString(), mb);
		}

		for (MapBean mb : chargebacks) {
			Integer month = mb.asInteger("statMonth") + 1;
			String key = month.toString();

			if (result.containsKey(key)) {
				result.get(key).putAll(mb);
			} else {
				result.put(key, mb);
			}

		}

		return result;
	}

	public static String buildRefId(long id) {
		return "OF:" + id;
	}

}
