/**
 * Created on 2010-4-10
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author tiyi
 * 
 */
public class DateJedi {
	Calendar data = null;

	/**
	 * 构造器
	 * 
	 * @param cal
	 */
	public DateJedi(Calendar cal) {
		if (cal != null)
			this.data = (Calendar) cal.clone();
	}

	/**
	 * 移动月份
	 * 
	 * @param i
	 * @return
	 */
	public DateJedi moveMonth(int i) {
		if (data == null)
			return this;
		this.data.add(Calendar.MONTH, i);
		return this;
	}

	/**
	 * 昨日
	 * 
	 * @return
	 */
	public DateJedi yesterday() {
		if (data == null)
			return this;
		resetToZero();

		data.add(Calendar.MILLISECOND, -1);

		return this;
	}

	/**
	 * 输出
	 * 
	 * @return
	 */
	public Calendar to() {
		if (data == null)
			return null;
		return (Calendar) this.data.clone();
	}

	/**
	 * 设为每月第一天
	 * 
	 * @return
	 */
	public DateJedi firstDayOfMonth() {
		data.set(Calendar.DAY_OF_MONTH, 1);
		return this;
	}

	/**
	 * 跳转到零时
	 * 
	 * @return
	 */
	public DateJedi resetToZero() {
		Calendar cal = data;
		if (cal == null)
			return this;
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return this;
	}

	/**
	 * @return
	 */
	public DateJedi tomorrow() {
		resetToZero();
		data.add(Calendar.DAY_OF_MONTH, 1);

		return this;
	}

	/**
	 * @param pd
	 * @return
	 */
	public static DateJedi create(Calendar pd) {
		return new DateJedi(pd);
	}

	/**
	 * @param year
	 * @param month
	 * @return
	 */
	public static DateJedi create(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		return DateJedi.create(cal).resetToZero();
	}

	/**
	 * @param string
	 */
	public String toString(String string) {
		if (this.data == null) {
			return StringUtils.EMPTY;
		}
		return DateExTool.formatDateTime(this.data, string);

	}

	public String toString() {
		return DateExTool.formatDateTime(this.data, "yyyy-MM-dd");

	}

	/**
	 * @param i
	 * @return
	 */
	public DateJedi moveWeek(int i) {
		this.data.add(Calendar.DAY_OF_MONTH, i * 7);
		return this;
	}

	/**
	 * @return
	 */
	public DateJedi setToWeekDay(int d) {
		int thisWday = this.data.get(Calendar.DAY_OF_WEEK);

		int days = thisWday - d;

		if (days > 0) {
			this.data.add(Calendar.DAY_OF_MONTH, -1 * days);
		} else if (days < 0) {
			this.data.add(Calendar.DAY_OF_MONTH, -6);
		}

		return this;
	}

	/**
	 * @return
	 */
	public DateJedi setToWeekFirst() {
		return setToWeekDay(Calendar.MONDAY);
	}

	/**
	 * @param i
	 * @return
	 */
	public DateJedi moveDay(int i) {
		this.data.add(Calendar.DAY_OF_MONTH, i);
		return this;
	}

	/**
	 * @param endDate
	 * @return
	 */
	public static DateJedi create(String endDate) {
		DateJedi dj = new DateJedi(DateExTool.getDate(endDate));
		return dj;
	}

	/**
	 * @return
	 */
	public DateJedi firstDayOfYear() {
		Calendar cal = data;
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, 0);
		return this;
	}

	/**
	 * @return
	 */
	public DateJedi lastDayOfMonth() {
		// 必需先调用firstday ofmonth，否则movemonth时会超过一个月（的可能性）
		return firstDayOfMonth().moveMonth(1).yesterday();
	}

	/**
	 * @return
	 */
	public static DateJedi createNow() {
		return DateJedi.create(Calendar.getInstance());
	}

	public DateJedi firstDayOfWeek(int monday) {
		Calendar beginDate = this.data;
		beginDate.setFirstDayOfWeek(monday);
		beginDate.set(Calendar.DAY_OF_WEEK, monday);
		return this;
	}

	public DateJedi lastDayOfWeek(int sunday) {
		if (sunday == Calendar.SUNDAY) {
			Calendar beginDate = this.data;
			beginDate.setFirstDayOfWeek(Calendar.MONDAY);
			beginDate.set(Calendar.DAY_OF_WEEK, sunday);
		} else {
			Calendar beginDate = this.data;
			beginDate.setFirstDayOfWeek(Calendar.SUNDAY);
			beginDate.set(Calendar.DAY_OF_WEEK, sunday);
		}
		return this;
	}

	public static DateJedi create(int y, int m, int d, int i, int j) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, y);
		cal.set(Calendar.MONTH, m);
		cal.set(Calendar.DAY_OF_MONTH, d);
		cal.set(Calendar.HOUR_OF_DAY, i);
		cal.set(Calendar.MINUTE, j);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return DateJedi.create(cal);
	}

	public DateJedi setMonth(int january) {
		this.data.set(Calendar.MONTH, january);
		return this;
	}

	public DateJedi moveHour(int i) {
		this.data.add(Calendar.HOUR_OF_DAY, i);
		return this;
	}

	public DateJedi setDay(int day) {

		this.data.set(Calendar.DAY_OF_MONTH, day);
		return this;
	}

	public DateJedi setHour(int h) {
		this.data.set(Calendar.HOUR_OF_DAY, h);
		return this;
	}

	public DateJedi setMinute(int h) {

		this.data.set(Calendar.MINUTE, h);
		return this;
	}

	public void setDate(int i) {
		// TODO Auto-generated method stub

	}

	public DateJedi setTimeInMillis(Long time) {
		this.data.setTimeInMillis(time);
		return this;
	}
}
