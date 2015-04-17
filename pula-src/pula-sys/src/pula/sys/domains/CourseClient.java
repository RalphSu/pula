package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("开课客户端")
public class CourseClient implements LoggablePo {

	public static final int STATUS_NEW = 1;
	public static final int STATUS_NORMAL = 2;
	public static final int STATUS_LOCKED = 3;
	public static final int STATUS_INGORE = 4;
	public static final int STATUS_RENEW = 5;

	private long id; // 请求流水
	private String machineNo;
	private boolean enabled;
	private String licenseKey;
	private Calendar expiredTime, createdTime;
	private Branch branch; // 从属于某个分部
	private Classroom classroom;
	private String name;
	private boolean removed;
	private int status;
	private String comments, applyComments;
	private String ip; // 申领者ip
	private SysUser applier; // 受理人
	private Calendar applyTime; // 受理时间

	public String getApplyComments() {
		return applyComments;
	}

	public void setApplyComments(String applyComments) {
		this.applyComments = applyComments;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public SysUser getApplier() {
		return applier;
	}

	public void setApplier(SysUser applier) {
		this.applier = applier;
	}

	public Calendar getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Calendar applyTime) {
		this.applyTime = applyTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	public Calendar getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Calendar expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toLogString() {
		return this.id + "(" + this.name + ")";
	}

}
