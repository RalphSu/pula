package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import puerta.support.utils.DateExTool;

//由教学总监制定
@WxlDomain("课程任务计划")
public class CourseTaskPlan implements LoggablePo {

	private long id;

	private Teacher master, assistant1, assistant2;

	private Calendar createdTime, updatedTime, startTime, endTime;

	private Classroom classroom;
	private Course course;

	private SysUser creator, updater;

	private Branch branch;
	private boolean removed;
	private String comments;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public Teacher getAssistant1() {
		return assistant1;
	}

	public void setAssistant1(Teacher assistant1) {
		this.assistant1 = assistant1;
	}

	public Teacher getAssistant2() {
		return assistant2;
	}

	public void setAssistant2(Teacher assistant2) {
		this.assistant2 = assistant2;
	}

	public Teacher getMaster() {
		return master;
	}

	public void setMaster(Teacher master) {
		this.master = master;
	}

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

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
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

	@Override
	public String toLogString() {
		return "[" + this.id + "]" + DateExTool.getText(this.startTime) + " "
				+ startTime.get(Calendar.HOUR_OF_DAY);
	}

}
