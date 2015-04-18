package pula.sys.forms;

import java.text.ParseException;

import puerta.support.utils.DateExTool;
import puerta.support.utils.DateHelper;
import pula.sys.domains.AttendanceRecord;

public class AttendanceRecordForm extends AttendanceRecord {
	private String checkTimeText;
	private String collectTimeText;
	private int hour, minute;
	private String branchNo;

	public String getCheckTimeText() {
		return checkTimeText;
	}

	public void setCheckTimeText(String checkTimeText) {
		this.checkTimeText = checkTimeText;
	}

	public String getCollectTimeText() {
		return collectTimeText;
	}

	public void setCollectTimeText(String collectTimeText) {
		this.collectTimeText = collectTimeText;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	public AttendanceRecord toAttendanceRecord() {
		AttendanceRecord obj = new AttendanceRecord();
		try {
			obj.setCheckTime(DateHelper.getCalendar(this.checkTimeText, hour,
					minute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		obj.setCollectTime(DateExTool.getDate(this.collectTimeText));
		obj.setDataFrom(this.getDataFrom());
		obj.setId(this.getId());
		obj.setIp(this.getIp());
		obj.setMachine(this.getMachine());
		obj.setNo(this.getNo());
		obj.setOwner(this.getOwner());
		obj.setBranch(this.getBranch());
		return obj;
	}

}
