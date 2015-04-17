package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("课程结果学生数据")
public class CourseTaskResultStudent implements LoggablePo {

	private long id;
	private CourseTaskResult courseTaskResult;
	private Student student;
	private int submitType;
	private Calendar createdTime;
	private boolean removed, gamePlayed;
	// one -to-one;
	private CourseTaskResultWork courseTaskResultWork;
	private Calendar gamePlayAt;

	public Calendar getGamePlayAt() {
		return gamePlayAt;
	}

	public void setGamePlayAt(Calendar gamePlayAt) {
		this.gamePlayAt = gamePlayAt;
	}

	public boolean isGamePlayed() {
		return gamePlayed;
	}

	public void setGamePlayed(boolean gamePlayed) {
		this.gamePlayed = gamePlayed;
	}

	public CourseTaskResultWork getCourseTaskResultWork() {
		return courseTaskResultWork;
	}

	public void setCourseTaskResultWork(
			CourseTaskResultWork courseTaskResultWork) {
		this.courseTaskResultWork = courseTaskResultWork;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public int getSubmitType() {
		return submitType;
	}

	public void setSubmitType(int submitType) {
		this.submitType = submitType;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CourseTaskResult getCourseTaskResult() {
		return courseTaskResult;
	}

	public void setCourseTaskResult(CourseTaskResult courseSession) {
		this.courseTaskResult = courseSession;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Override
	public String toLogString() {
		return String.valueOf(this.id);
	}

	public static CourseTaskResultStudent create(Long courseTaskResultStudentId) {
		CourseTaskResultStudent c = new CourseTaskResultStudent();
		c.id = courseTaskResultStudentId;
		return c;
	}

}
