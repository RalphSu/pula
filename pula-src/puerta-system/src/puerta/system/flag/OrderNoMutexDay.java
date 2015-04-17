/**
 * Created on 2010-4-23
 * WXL 2009
 * $Id$
 */
package puerta.system.flag;

import java.util.Calendar;

/**
 * 
 * @author tiyi
 * 
 */
public class OrderNoMutexDay {

	private int counter;
	private long timestamp;
	private String logId;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	/**
	 * @param counter2
	 * @param logId2
	 * @return
	 */
	public static OrderNoMutexDay create(int counter2, String logId2) {
		OrderNoMutexDay od = new OrderNoMutexDay();
		od.counter = counter2;
		od.logId = logId2;
		od.timestamp = Calendar.getInstance().getTimeInMillis();
		return od;
	}

}
