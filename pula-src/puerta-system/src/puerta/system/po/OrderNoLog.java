/**
 * Created on 2007-6-6 07:28:53
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.po;

import java.util.Calendar;

/**
 * 
 * @author tiyi
 * 
 */
public class OrderNoLog {

	private String id;

	private Calendar logTime;

	private String no;

	private int counter;

	private Calendar date;

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int count) {
		this.counter = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Calendar getLogTime() {
		return logTime;
	}

	public void setLogTime(Calendar logTime) {
		this.logTime = logTime;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
