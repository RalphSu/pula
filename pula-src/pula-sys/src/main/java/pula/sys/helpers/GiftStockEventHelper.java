package pula.sys.helpers;

import puerta.support.vo.SelectNameFetch;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.GiftStockLog;

public class GiftStockEventHelper {

	private static final int[] TARGETS = new int[] { GiftStockEvent.TARGET_BUY,
			GiftStockEvent.TARGET_CHECK_ADD, GiftStockEvent.TARGET_CONSUME,
			GiftStockEvent.TARGET_LOST };

	private static final String[] TARGETS_NAMES = new String[] { "购买", "盘盈",
			"消耗", "报损" };

	public static SelectOptionList getTargets(int n) {
		// SelectOptionList list = SelectOption.getList(0, TARGETS,
		// TARGETS_NAMES);
		//
		// if(n == GiftStockLog.IN){
		// return list.match ( )
		// }

		if (n == GiftStockLog.IN) {
			return SelectOption.getList(0, new Integer[] {
					GiftStockEvent.TARGET_BUY, GiftStockEvent.TARGET_CHECK_ADD },
					new SelectNameFetch<Integer>() {
						@Override
						public String getName(Integer n) {
							return GiftStockEventHelper.getTargetName(n);
						}
					});
		} else {

			return SelectOption.getList(0, new Integer[] {
					GiftStockEvent.TARGET_CONSUME, GiftStockEvent.TARGET_LOST },
					new SelectNameFetch<Integer>() {
						@Override
						public String getName(Integer n) {
							return GiftStockEventHelper.getTargetName(n);
						}
					});
		}

	}

	public static String getTargetName(int n) {
		return SelectOption.getName(n, TARGETS, TARGETS_NAMES);
	}

	public static boolean manual(int target, int in) {
		if (target > 0 && in > 0 && target < 100) {
			return true;
		}
		if (target < 0 && in < 0 && target > -100) {
			return true;
		}
		return false;
	}

	public static int toType(int target) {

		if (target > 0) {
			return GiftStockLog.IN;
		}
		return GiftStockLog.OUT;
	}
}
