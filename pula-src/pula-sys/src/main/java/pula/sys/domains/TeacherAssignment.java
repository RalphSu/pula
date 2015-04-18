package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;

@WxlDomain("教师指派")
public class TeacherAssignment {

	private long id;
	private Teacher teacher;
	private Calendar createdTime, endTime;
	private Branch branch;
	private boolean current; // 当前
	private SysUser assigner; // 指派人

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public SysUser getAssigner() {
		return assigner;
	}

	public void setAssigner(SysUser assigner) {
		this.assigner = assigner;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch school) {
		this.branch = school;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

}
