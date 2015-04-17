/**
 * Created on 2007-6-6 下午09:57:48
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.flag;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import puerta.system.po.OrderNoRule;

/**
 * 
 * @author tiyi
 * 
 */
public class OrderNoMutex {

	private String no;
	private int counter;
	private int day;
	private OrderNoRule rule;
	private String logId;

	private String lastDay;

	private Map<String, OrderNoMutexDay> mapCounter = new HashMap<String, OrderNoMutexDay>();

	public String getLastDay() {
		return lastDay;
	}

	public void setLastDay(String lastDay) {
		this.lastDay = lastDay;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int nowCount) {
		this.counter = nowCount;
	}

	public OrderNoRule getRule() {
		return rule;
	}

	public void setRule(OrderNoRule rule) {
		this.rule = rule;
	}

	public OrderNoMutex(String no) {
		this.no = no;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * 
	 */
	public int incCounter() {
		return ++this.counter;

	}

	/**
	 * 
	 */
	public void putCounter() {
		if (!StringUtils.isEmpty(this.lastDay)) {
			this.mapCounter.put(this.lastDay, OrderNoMutexDay.create(
					this.counter, this.logId));

			fit();
		} else {

		}

	}

	/**
	 * 
	 */
	private void fit() {
		if (mapCounter.size() < 20) {
			return;
		}

		String minKey = null;
		long minValue = Long.MAX_VALUE;
		for (String key : mapCounter.keySet()) {
			long accv = mapCounter.get(key).getTimestamp();
			if (minValue > accv) {
				minKey = key;
				minValue = accv;
			}
		}

		// kill the min key
		if (minKey != null) {

			mapCounter.remove(minKey);
		}
	}

	/**
	 * @param day2
	 * @param counter2
	 * @param logId
	 */
	public void loadCounter(String day2, int counter2, String logId) {
		this.putCounter();

		this.lastDay = day2;
		this.counter = counter2;
		this.logId = logId;

	}

	/**
	 * @param theDay
	 */
	public boolean loadCounter(String theDay) {

		this.putCounter();
		if (this.mapCounter.containsKey(theDay)) {
			OrderNoMutexDay od = this.mapCounter.get(theDay);
			this.counter = od.getCounter();
			this.lastDay = theDay;
			this.logId = od.getLogId();
			return true;
		}
		return false;

	}

}
