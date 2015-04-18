package pula.sys.helpers;

import java.util.Calendar;

import puerta.support.utils.DateJedi;

public class TrainLogHelper {

	public static Calendar getAllowEditDate() {
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		if (day > 5) {
			// 只能修改本月的数据
			return DateJedi.createNow().resetToZero().firstDayOfMonth().to();
		} else {
			// 修改上月的
			return DateJedi.createNow().resetToZero().moveMonth(-1)
					.firstDayOfMonth().to();
		}

	}

	public static boolean allowEdit(int year, int month) {

		Calendar cal = TrainLogHelper.getAllowEditDate();
		Calendar firstDay = DateJedi.create(year, month - 1).firstDayOfMonth()
				.resetToZero().to();

		if (!firstDay.after(cal)) {
			return false;
		}

		return true;
	}

	public static boolean allowEdit(Calendar cal1) {

		Calendar cal = TrainLogHelper.getAllowEditDate();
		Calendar firstDay = DateJedi.create(cal1).firstDayOfMonth()
				.resetToZero().to();

		if (firstDay.before(cal)) {
			return false;
		}

		return true;
	}

}
