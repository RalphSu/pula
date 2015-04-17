/*
 * Created on 2004-11-7
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * @author tiyi 2004-11-7
 */
public class DateExTool {
	/**
	 * Logger for this class
	 */
	// private static final Logger logger = Logger.getLogger(DateTool.class);
	public static String formatDateTime(Calendar cal, String mark) {

		SimpleDateFormat format = new SimpleDateFormat(mark);
		if (cal != null) {
			Date date = cal.getTime();
			return format.format(date);
		} else {
			return "";
		}
	}

	public static Calendar yesterday(Calendar d) {
		Calendar cal = (Calendar) d.clone();

		// System.out.println( "date2time: " +dateTime2String( d) ) ;
		// cal.setTime(d);
		// int day = cal.get(Calendar.DATE);
		// cal.add(Calendar.DATE, -1);
		// cal.getTimeZone().get
		cal = clearTime(cal);
		cal.add(Calendar.MILLISECOND, -1);
		return cal;

	}

	public static Calendar tomorrow(Calendar d) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(d.getTime());
		// int day = cal.get(Calendar.DATE);
		cal.add(Calendar.DATE, 1);
		cal = clearTime(cal);
		return cal;
	}

	public static Calendar clearTime(Calendar incal) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(incal.getTime());
		// logger.debug("BEFORE CALENDAR:" + DateTool.dateTime2String(cal));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		// cal.getTimeZone().setRawOffset(0);
		// cal.getTimeZone().setID()
		// logger.debug(Calendar.getInstance());
		// logger.debug("AFTER CALENDAR1:" + DateTool.dateTime2String(cal));
		// cal.add(Calendar.HOUR, -8);
		// logger.debug("AFTER CALENDAR2:" + DateTool.dateTime2String(cal));
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		// logger.debug("AFTER CALENDAR:" + DateTool.dateTime2String(cal));
		return cal;
	}

	public static Date yesterday(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal = yesterday(cal);
		return cal.getTime();

	}

	public static Calendar tomorrow() {

		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// int day = cal.get(Calendar.DATE);
		// cal.add(Calendar.DATE, 1);
		cal = tomorrow(cal);

		return cal;
	}

	public static Date tomorrow(Date d) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		// int day = cal.get(Calendar.DATE);
		cal = tomorrow(cal);
		return cal.getTime();
	}

	public static Calendar string2date(String str, String mark)
			throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(mark);

		cal.setTime(format.parse(str));

		return cal;

	}

	public static Calendar string2date(String str) throws ParseException {
		return string2date(str, "yyyy-MM-dd");

	}

	public static Calendar string2datetime(String str) throws ParseException {
		return string2date(str, "yyyy-MM-dd HH:mm:ss");

	}

	public static String dateTime2String(Calendar cal) {

		return DateExTool.formatDateTime(cal, "yyyy-MM-dd HH:mm:ss");
	}

	public static String date2String(Calendar cal) {

		return DateExTool.formatDateTime(cal, "yyyy-MM-dd");
	}

	public static String dateTime2String(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return DateExTool.formatDateTime(cal, "yyyy-MM-dd HH:mm:ss");
	}

	public static long betweenDays(Calendar cal, Calendar then) {
		return Math.abs((cal.getTimeInMillis() - then.getTimeInMillis()) / 1000
				/ 60 / 60 / 24);
	}

	public static String getToday() {
		return date2String(Calendar.getInstance());
	}

	/**
	 * @param beginDate
	 * @return
	 */
	public static Calendar getDate(String beginDate) {
		if (StringUtils.isEmpty(beginDate))
			return null;
		try {
			Calendar cal = DateExTool.string2date(beginDate);
			return cal;
		} catch (ParseException e) {
			// e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param beginDate
	 * @return
	 */
	public static Calendar getPrevDate(String beginDate) {
		Calendar cal = getDate(beginDate);
		if (cal == null)
			return cal;
		return yesterday(cal);
	}

	/**
	 * @param endDate
	 * @return
	 */
	public static Calendar getNextDate(String endDate) {
		Calendar cal = getDate(endDate);
		if (cal == null)
			return cal;
		return tomorrow(cal);
	}

	public static int dateDiff(Calendar start, Calendar end) {
		long startTime = start.getTimeInMillis();
		long endTime = end.getTimeInMillis();

		long diff = endTime - startTime;
		int diffint = (int) (diff / (1000 * 60 * 60 * 24));

		return diffint;
	}

	public static int dateDiff(Calendar start) {
		return dateDiff(start, Calendar.getInstance());
	}

	public static Calendar today() {
		Calendar cal = Calendar.getInstance();
		cal = clearTime(cal);
		return cal;
	}

	public static String formatDate(Date date, String formatstr) {

		SimpleDateFormat format = new SimpleDateFormat(formatstr);

		return format.format(date);

	}

	/**
	 * @param inputTime
	 * @return
	 */
	public static int getMonth(Calendar inputTime) {
		int month = inputTime.get(Calendar.MONTH) + 1;
		int year = inputTime.get(Calendar.YEAR);
		return year * 100 + month;
	}

	/**
	 * @param cal
	 * @param daysSpan
	 * @return
	 */
	public static Calendar addDays(Calendar cal, int daysSpan) {
		Calendar calc = (Calendar) cal.clone();

		calc.add(Calendar.DAY_OF_MONTH, daysSpan);

		return calc;
	}

	/**
	 * @return
	 */
	public static String getNow() {
		return dateTime2String(Calendar.getInstance());
	}

	/**
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int makeMinutes(int[] begin, int[] end) {
		Calendar beginCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();

		beginCal.set(Calendar.HOUR_OF_DAY, begin[0]);
		beginCal.set(Calendar.MINUTE, begin[1]);

		endCal.set(Calendar.HOUR_OF_DAY, end[0]);
		endCal.set(Calendar.MINUTE, end[1]);

		long ms = endCal.getTimeInMillis() - beginCal.getTimeInMillis();

		return (int) (ms / 60000);

		// return 0;
	}

	/**
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getTime(String str) throws ParseException {
		return string2date(str, "yyyyMMddHHmmss");
	}

	/**
	 * @param cal
	 * @param instance
	 * @return
	 */
	public static boolean sameDay(Calendar cal, Calendar instance) {
		if (cal == instance)
			return true;
		if (cal == null || instance == null)
			return false;
		boolean b = cal.get(Calendar.YEAR) == instance.get(Calendar.YEAR);
		b = b && cal.get(Calendar.MONTH) == instance.get(Calendar.MONTH);
		b = b
				&& cal.get(Calendar.DAY_OF_MONTH) == instance
						.get(Calendar.DAY_OF_MONTH);

		return b;
	}

	/**
	 * @param beginDateCal
	 * @param beginHour
	 * @param beginMinute
	 */
	public static void setHourAndMinute(Calendar beginDateCal, int beginHour,
			int beginMinute) {
		beginDateCal.set(Calendar.HOUR, beginHour);
		beginDateCal.set(Calendar.MINUTE, beginMinute);

	}

	/**
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int betweenMonth(Calendar beginDate, Calendar endDate) {
		if (beginDate == null || endDate == null) {
			return 0;
		}

		Calendar bd = (Calendar) beginDate.clone();
		bd.set(Calendar.DAY_OF_MONTH, 1);
		int m = 0;
		while (bd.before(endDate)) {
			m++;
			bd.add(Calendar.MONTH, 1);
		}

		return m;
	}

	// public static void main(String[] args) throws ParseException {
	// String date = "2007-07-01 10:11:30";
	// String date2 = "2007-06-30 21:10:11";
	//
	// Calendar d1 = DateExTool.string2datetime(date);
	// Calendar d2 = DateExTool.string2datetime(date2);
	//
	// long m = betweenHour(d1, d2);
	//
	// System.out.println("hour=" + m);
	//
	// }

	/**
	 * @param calendar
	 * @return
	 */
	public static int[] getLastYearAndMonth(Calendar calendar) {
		Calendar cal = (Calendar) calendar.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONDAY, -1);
		return new int[] { cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) };
	}

	/**
	 * @param st
	 * @param instance
	 * @return
	 */
	public static long betweenHour(Calendar then, Calendar cal) {
		return Math
				.abs((cal.getTimeInMillis() - then.getTimeInMillis()) / 1000 / 60 / 60);
	}

	public static String toRead(double secs) {
		StringBuilder sb = new StringBuilder();
		if (secs > 3600) {
			sb.append((int) (secs / 3600)).append(" Hour(s) ");
			secs = secs % 3600;
		}
		if (secs > 60) {
			sb.append((int) (secs / 60)).append(" Mins ");
			secs = secs % 60;
		}

		sb.append(secs).append(" Secs ");
		return sb.toString();
	}

	public static Calendar toDate(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			return DateExTool.string2date(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getText(Calendar beginDate) {
		if (beginDate != null) {
			return DateExTool.date2String(beginDate);
		}
		return "";
	}

	public static boolean sameMonthDay(Calendar cal, Calendar instance) {
		if (cal == instance)
			return true;
		if (cal == null || instance == null)
			return false;

		boolean b = cal.get(Calendar.MONTH) == instance.get(Calendar.MONTH)
				&& cal.get(Calendar.DAY_OF_MONTH) == instance
						.get(Calendar.DAY_OF_MONTH);

		return b;
	}
}
