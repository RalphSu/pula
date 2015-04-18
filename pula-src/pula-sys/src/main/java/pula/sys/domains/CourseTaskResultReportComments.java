package pula.sys.domains;

import java.util.Calendar;

public class CourseTaskResultReportComments {

	private long id;
	private CourseTaskResult result;
	private String comments;
	private Calendar createdTime;

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public CourseTaskResult getResult() {
		return result;
	}

	public void setResult(CourseTaskResult result) {
		this.result = result;
	}

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

	public static CourseTaskResultReportComments create(String string) {
		CourseTaskResultReportComments c = new CourseTaskResultReportComments();
		c.comments = string;
		return c;
	}

}
