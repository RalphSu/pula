package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import pula.sys.intfs.RefIdSupport;

/**
 * 课程作品,一节课有数个作品？
 * 
 * @author tiyi
 * 
 */
@WxlDomain("课程作品")
public class CourseTaskResultWork implements RefIdSupport {

	private long id;
	private CourseTaskResultStudent courseTaskResultStudent;

	private String attachmentKey;

	// 评分
	private int score1, score2, score3, score4, score5; // 评分,5个角度
	private Calendar scoreTime, createdTime; // 评分时间

	// 理论上评分的都是上课老师？？那么

	public String getAttachmentKey() {
		return attachmentKey;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1(int score1) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2(int score2) {
		this.score2 = score2;
	}

	public int getScore3() {
		return score3;
	}

	public void setScore3(int score3) {
		this.score3 = score3;
	}

	public int getScore4() {
		return score4;
	}

	public void setScore4(int score4) {
		this.score4 = score4;
	}

	public int getScore5() {
		return score5;
	}

	public void setScore5(int score5) {
		this.score5 = score5;
	}

	public void setAttachmentKey(String attachmentKey) {
		this.attachmentKey = attachmentKey;
	}

	public long getId() {
		return id;
	}

	public Calendar getScoreTime() {
		return scoreTime;
	}

	public void setScoreTime(Calendar scoreTime) {
		this.scoreTime = scoreTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String toRefId() {
		return buildRefId(id, attachmentKey);
	}

	@Override
	public int getTypeRange() {

		return FileAttachment.TYPE_STUENDT_WORK;
	}

	public static String buildRefId(Long id2, String string) {
		return id2 + string;
	}

	public CourseTaskResultStudent getCourseTaskResultStudent() {
		return courseTaskResultStudent;
	}

	public void setCourseTaskResultStudent(
			CourseTaskResultStudent courseTaskResultStudent) {
		this.courseTaskResultStudent = courseTaskResultStudent;
	}

}
