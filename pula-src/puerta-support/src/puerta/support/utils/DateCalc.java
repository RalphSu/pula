/**
 * Created on 2010-3-27
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.Calendar;

/**
 * 
 * @author tiyi
 * 
 */
public class DateCalc {

	

	/**
	 * @param beginMonth
	 * @return
	 */
	public static Calendar getLastMonthBegin(Calendar beginMonth) {
		Calendar cal = (Calendar) beginMonth.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1); // 本月第一天
		cal.add(Calendar.MONTH, -1);// 后退一个月
		return clearTime(cal);
	}

	/**
	 * @param beginMonth
	 * @return
	 */
	public static Calendar getLastMonthEnd(Calendar beginMonth) {
		Calendar cal = (Calendar) beginMonth.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1);// 本月第一天
		cal.add(Calendar.DAY_OF_MONTH, -1); // 后退一天即为上月的最后一天
		return clearTime(cal);
	}

	public static Calendar clearTime(Calendar incal) {
		Calendar cal = (Calendar) incal.clone();
		// cal.setTime(incal.getTime());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

	/**
	 * @param endMonth
	 * @return
	 */
	public static Calendar getLastYearSameMonth(Calendar endMonth) {
		Calendar cal = (Calendar) endMonth.clone();
		cal.add(Calendar.YEAR, -1);
		return cal;
	}

	public static Calendar tomorrow(Calendar d) {
		Calendar cal = (Calendar) d.clone();
		cal.add(Calendar.DATE, 1);
		cal = clearTime(cal);
		return cal;
	}

	public static Calendar yesterday(Calendar d) {
		Calendar cal = (Calendar) d.clone();
		cal = clearTime(cal);
		cal.add(Calendar.MILLISECOND, -1);
		return cal;

	}

	/**
	 * @param thisMonthBegin
	 * @param i
	 * @return
	 */
	public static Calendar moveMonth(Calendar d, int i) {
		Calendar cal = (Calendar) d.clone();
		cal.add(Calendar.MONTH, i);
		return cal;
	}

	// chain

	
}
