package pula.sys.forms;

import java.text.ParseException;

import puerta.support.utils.DateHelper;
import pula.sys.domains.CourseTaskPlan;

public class CourseTaskPlanForm extends CourseTaskPlan {

	private int hour, minute;
	private String date;
	private long branchId, courseId;
	private long classroomId;
	private String teacherNo, assistant1No, assistant2No;

	public String getAssistant1No() {
		return assistant1No;
	}

	public void setAssistant1No(String assistant1No) {
		this.assistant1No = assistant1No;
	}

	public String getAssistant2No() {
		return assistant2No;
	}

	public void setAssistant2No(String assistant2No) {
		this.assistant2No = assistant2No;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public long getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(long classroomId) {
		this.classroomId = classroomId;
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public CourseTaskPlan toCourseTaskPlan() {
		CourseTaskPlan obj = new CourseTaskPlan();
		obj.setAssistant1(this.getAssistant1());
		obj.setAssistant2(this.getAssistant2());
		obj.setBranch(this.getBranch());
		obj.setClassroom(this.getClassroom());
		obj.setComments(this.getComments());
		obj.setCourse(this.getCourse());
		obj.setCreator(this.getCreator());
		// obj.setEndTime(DateExTool.getDate(this.hour));
		obj.setId(this.getId());
		obj.setMaster(this.getMaster());
		try {
			obj.setStartTime(DateHelper.getCalendar(date, hour, minute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		obj.setUpdater(this.getUpdater());
		return obj;
	}

}
