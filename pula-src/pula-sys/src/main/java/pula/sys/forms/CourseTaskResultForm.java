package pula.sys.forms;

import java.text.ParseException;

import puerta.support.utils.DateHelper;
import pula.sys.domains.Branch;
import pula.sys.domains.Classroom;
import pula.sys.domains.Course;
import pula.sys.domains.CourseTaskResult;

public class CourseTaskResultForm extends CourseTaskResult {
	private String endTimeText;
	private String startTimeText;
	private int startTimeMinute, startTimeHour;
	private int endTimeMinute, endTimeHour;

	private long branchId, classroomId;
	private long courseId;
	private String masterNo, assistant1No, assistant2No;

	public String getEndTimeText() {
		return endTimeText;
	}

	public void setEndTimeText(String endTimeText) {
		this.endTimeText = endTimeText;
	}

	public String getStartTimeText() {
		return startTimeText;
	}

	public void setStartTimeText(String startTimeText) {
		this.startTimeText = startTimeText;
	}

	public int getStartTimeMinute() {
		return startTimeMinute;
	}

	public void setStartTimeMinute(int startTimeMinute) {
		this.startTimeMinute = startTimeMinute;
	}

	public int getStartTimeHour() {
		return startTimeHour;
	}

	public void setStartTimeHour(int startTimeHour) {
		this.startTimeHour = startTimeHour;
	}

	public int getEndTimeMinute() {
		return endTimeMinute;
	}

	public void setEndTimeMinute(int endTimeMinute) {
		this.endTimeMinute = endTimeMinute;
	}

	public int getEndTimeHour() {
		return endTimeHour;
	}

	public void setEndTimeHour(int endTimeHour) {
		this.endTimeHour = endTimeHour;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(long classroomId) {
		this.classroomId = classroomId;
	}

	public long getCourseId() {
		return courseId;
	}

	public void setCourseId(long courseId) {
		this.courseId = courseId;
	}

	public String getMasterNo() {
		return masterNo;
	}

	public void setMasterNo(String masterNo) {
		this.masterNo = masterNo;
	}

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

	public CourseTaskResult toCourseTaskResult() {
		CourseTaskResult obj = new CourseTaskResult();
		obj.setAssistant1(this.getAssistant1());
		obj.setAssistant2(this.getAssistant2());
		obj.setBranch(Branch.create(branchId));
		obj.setClassroom(Classroom.create(classroomId));
		obj.setComments(this.getComments());
		obj.setCourse(Course.create(this.courseId));
		obj.setCreator(this.getCreator());
		try {
			obj.setEndTime(DateHelper.getCalendar(this.endTimeText,
					this.endTimeHour, this.endTimeMinute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		obj.setId(this.getId());
		obj.setMaster(this.getMaster());

		try {
			obj.setStartTime(DateHelper.getCalendar(this.startTimeText,
					this.startTimeHour, this.startTimeMinute));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		obj.setStudentCount(this.getStudentCount());
		obj.setStudents(this.getStudents());
		obj.setSubmitType(this.getSubmitType());
		obj.setUpdater(this.getUpdater());
		obj.setWorks(this.getWorks());
		return obj;
	}

}
