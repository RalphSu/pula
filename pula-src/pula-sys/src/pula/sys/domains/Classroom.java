package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;

@WxlDomain("教室")
public class Classroom {

	private long id;
	private String no, name;
	private Branch branch;

	private boolean removed;
	private boolean enabled; // 有效与否

	private Calendar createdTime, updatedTime;
	private SysUser creator, updater;

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser creator) {
		this.creator = creator;
	}

	public SysUser getUpdater() {
		return updater;
	}

	public void setUpdater(SysUser updater) {
		this.updater = updater;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch school) {
		this.branch = school;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static Classroom create(long classroomId) {
		if (classroomId == 0) {
			return null;
		}
		Classroom cl = new Classroom();
		cl.id = classroomId;
		return cl;
	}

}
