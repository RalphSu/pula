package pula.sys.domains;

import java.util.Calendar;
import java.util.List;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("开课记录")
public class CourseTaskResult implements LoggablePo {

	// public static final int STATUS_PLAN = 1;
	// public static final int STATUS_EXECUTE = 2;
	// public static final int STATUS_CANCELED = 3;
	// public static final int STATUS_COMPLETED = 4;

	public static final int ST_MANUAL = 1;
	public static final int ST_CLIENT = 2;

	private long id;
	private Teacher master, assistant1, assistant2;

	private Calendar createdTime, updatedTime, startTime, endTime;

	private Classroom classroom;
	private Course course;

	private SysUser creator, updater;

	private boolean removed;
	private Branch branch;
	private String comments;

	private List<CourseTaskResultStudent> students;
	private List<CourseTaskResultWork> works;

	private int studentCount;
	private int submitType;

	private long localId;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public int getSubmitType() {
		return submitType;
	}

	public void setSubmitType(int fromType) {
		this.submitType = fromType;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public List<CourseTaskResultWork> getWorks() {
		return works;
	}

	public void setWorks(List<CourseTaskResultWork> works) {
		this.works = works;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public List<CourseTaskResultStudent> getStudents() {
		return students;
	}

	public void setStudents(List<CourseTaskResultStudent> students) {
		this.students = students;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Teacher getMaster() {
		return master;
	}

	public void setMaster(Teacher master) {
		this.master = master;
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

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Classroom getClassroom() {
		return classroom;
	}

	public void setClassroom(Classroom classroom) {
		this.classroom = classroom;
	}

	@Override
	public String toLogString() {
		return String.valueOf(id);
	}

	public static CourseTaskResult create(Long courseTaskResultId) {
		CourseTaskResult r = new CourseTaskResult();
		r.id = courseTaskResultId;
		return r;
	}

}
