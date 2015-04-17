package pula.sys.helpers;

import java.util.Calendar;

import puerta.support.Pe;
import puerta.support.utils.DateJedi;
import puerta.support.utils.StringTool;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;

public class CourseTaskPlanHelper {

	public static boolean allowEdit(int year, int month) {

		Calendar this_month_first_day = DateJedi.createNow().firstDayOfMonth()
				.resetToZero().to();
		Calendar targetDate = DateJedi.create(year, month - 1)
				.firstDayOfMonth().resetToZero().to();
		Calendar three_month_later = DateJedi.create(this_month_first_day)
				.moveMonth(3).firstDayOfMonth().yesterday().to();

		return (!targetDate.before(this_month_first_day) && targetDate
				.before(three_month_later));
	}

	public static void checkDate(int hour, int minute) {
		if (hour >= 0 && hour < 24 && minute >= 0 && minute < 60) {

		} else {
			Pe.raise("时间数据无效:" + hour + ":" + minute);
		}

	}

	public static String getTimeShortText(Calendar startTime) {
		int hours = startTime.get(Calendar.HOUR_OF_DAY);
		int mins = startTime.get(Calendar.MINUTE);
		return StringTool.fillZero(hours, 2) + ":"
				+ StringTool.fillZero(mins, 2);
	}

	public static SelectOptionList getHours() {
		return SelectOption.getList(0, new int[] { 8, 9, 10, 11, 12, 13, 14,
				15, 16, 17, 18 }, new String[] { "08", "09", "10", "11", "12",
				"13", "14", "15", "16", "17", "18" });
	}

	public static SelectOptionList getMinutes() {

		return SelectOption.getList(0, new int[] { 0, 15, 30, 45 },
				new String[] { "00", "15", "35", "45" });
	}
}
