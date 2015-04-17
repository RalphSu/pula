/*
 * Created on 2005-8-4
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.utils;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang.math.NumberUtils;

/**
 * @author tiyi 2005-8-4
 */
public class DateHelper {

	public static int getNowYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static int getNowMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	// public static String getMonthBegin(int year, int month) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.YEAR, year);
	// calendar.set(Calendar.MONTH, month - 1);
	// calendar.set(Calendar.DAY_OF_MONTH, 1);
	// return DateTool.date2String(calendar);
	// }
	//
	// public static String getMonthEnd(int year, int month) {
	// Calendar calendar = Calendar.getInstance();
	// calendar.set(Calendar.YEAR, year);
	// calendar.set(Calendar.MONTH, month - 1);
	// calendar.set(Calendar.DAY_OF_MONTH, getDaysOfMonth(month));
	// return DateTool.date2String(calendar);
	// }

	/**
	 * 
	 * 
	 * @return
	 */
	public static String getThisMonthBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return DateExTool.date2String(calendar);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static String getThisMonthEnd() {
		Calendar calendar = Calendar.getInstance();

		int month = calendar.get(Calendar.MONTH) + 1;
		calendar.set(Calendar.DAY_OF_MONTH, getDaysOfMonth(month));
		return DateExTool.date2String(calendar);
	}

	public static String getMonthBegin(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// calendar.roll(Calendar.MONTH, 1);
		// calendar.roll(Calendar.DAY_OF_MONTH, -1);

		return DateExTool.date2String(calendar);
	}

	public static Calendar getMonthBeginCal(int year, int month) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);

		calendar = DateExTool.clearTime(calendar);

		return calendar;
	}

	public static String getMonthEnd(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return DateExTool.date2String(calendar);
	}

	public static Calendar getMonthEndCal(int year, int month) {
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.add(Calendar.MONTH, 1);

		calendar = DateExTool.clearTime(calendar);

		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar;
	}

	public static String getPrevMonthBegin() {
		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MONTH, -1);

		return getMonthBegin(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
	}

	public static String getPrevMonthEnd() {
		Calendar cal = Calendar.getInstance();

		cal.roll(Calendar.MONTH, -1);

		return getMonthEnd(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
	}

	/**
	 * 
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		int Days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
			Days[1] = 29;
		return Days[month - 1];
	}

	/**
	 * 
	 * 
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int month) {
		int day = 0;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		day = getDaysOfMonth(year, month);
		return day;
	}

	/**
	 * @param year
	 * @param month
	 *            0-11
	 * @return
	 */
	public static int getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @return
	 */
	public static Calendar getCalendar(int year, int month, int day, int hour,
			int minute) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	/**
	 * @param planDate
	 * @param hour
	 * @param minute
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getCalendar(String planDate, int hour, int minute)
			throws ParseException {
		Calendar cal = DateExTool.string2date(planDate);

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	/**
	 * @param dayYear
	 * @param dayMonth
	 * @param day
	 * @return
	 */
	public static Calendar makeCalendar(int dayYear, int dayMonth, int day) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.YEAR, dayYear);
		cal.set(Calendar.MONTH, dayMonth);
		cal.set(Calendar.DAY_OF_MONTH, day);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;

	}

	public static Calendar getThisMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);

		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal;
	}

	/**
	 * @param instance
	 * @return
	 */
	public static Calendar getMonthBegin(Calendar instance) {
		Calendar calendar = (Calendar) instance.clone();

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		calendar = DateExTool.clearTime(calendar);

		return calendar;
	}

	/**
	 * @param instance
	 * @return
	 */
	public static Calendar getMonthEnd(Calendar instance) {
		Calendar calendar = (Calendar) instance.clone();

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, 1);

		calendar = DateExTool.clearTime(calendar);

		calendar.add(Calendar.DAY_OF_MONTH, -1);

		return calendar;
	}

	/**
	 * @param year
	 * @return
	 */
	public static Calendar getYearFirstDay(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.YEAR, year);
		calendar = DateExTool.clearTime(calendar);

		return calendar;
	}

	/**
	 * @param indate
	 * @return
	 */
	public static int[] getYearAndMonth(String indate) {
		int ypos = indate.indexOf("/");
		// int mpos = indate.indexOf("-", ypos + 1);

		int y = NumberUtils.toInt(indate.substring(0, ypos), 0);
		int m = NumberUtils.toInt(indate.substring(ypos + 1, indate.length()),
				0);

		return new int[] { y, m };
	}

	public static String getTodayZip() {
		Calendar cal = Calendar.getInstance();
		return DateExTool.formatDateTime(cal, "yyyyMMdd");

	}
}
