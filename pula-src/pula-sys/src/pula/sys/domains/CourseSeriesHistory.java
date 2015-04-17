package pula.sys.domains;

import java.util.Calendar;

/**
 * 该序列已经上过的课程情况
 * 
 * @author tiyi
 * 
 */
public class CourseSeriesHistory {

	private long id;
	private CourseSeries series;
	private int indexNo; // 课程序号
	private Course course; // 课程
	private Calendar createdTime; // 当时时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CourseSeries getSeries() {
		return series;
	}

	public void setSeries(CourseSeries series) {
		this.series = series;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

}
