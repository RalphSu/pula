package pula.sys.domains;

import java.util.Calendar;

/**
 * 考勤记录
 * 
 * @author tiyi
 * 
 */

public class AttendanceRecord {

	public static final int DATA_FROM_MATCHINE = 1;
	public static final int DATA_FROM_MANUAL = 2;

	private long id;
	private Teacher owner;
	private Calendar checkTime, collectTime;
	private String no; // 考勤编号
	private int dataFrom; // 数据来源
	private String ip; // reporter client ip ;
	private int machine; // 机器编号，设备编号

	private boolean enabled;

	private Branch branch;

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Teacher getOwner() {
		return owner;
	}

	public void setOwner(Teacher owner) {
		this.owner = owner;
	}

	public Calendar getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Calendar checkTime) {
		this.checkTime = checkTime;
	}

	public Calendar getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Calendar collectTime) {
		this.collectTime = collectTime;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(int dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getMachine() {
		return machine;
	}

	public void setMachine(int machine) {
		this.machine = machine;
	}

}
