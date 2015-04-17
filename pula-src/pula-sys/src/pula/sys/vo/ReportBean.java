package pula.sys.vo;

import java.text.ParseException;

import puerta.support.utils.DateExTool;
import pula.sys.domains.CourseTaskResult;

public class ReportBean {

	private long id;
	private String beginTime, endTime;
	private String masterNo, masterRfid, masterName;
	private String assistant1No, assistant1Rfid, assistant1Name;
	private String assistant2No, assistant2Rfid, assistant2Name;

	private String courseNo;
	private String[] studentRfid, studentNo, studentName;

	public String[] getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String[] studentNo) {
		this.studentNo = studentNo;
	}

	public String[] getStudentName() {
		return studentName;
	}

	public void setStudentName(String[] studentName) {
		this.studentName = studentName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getMasterNo() {
		return masterNo;
	}

	public void setMasterNo(String masterNo) {
		this.masterNo = masterNo;
	}

	public String getMasterRfid() {
		return masterRfid;
	}

	public void setMasterRfid(String masterRfid) {
		this.masterRfid = masterRfid;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public String getAssistant1No() {
		return assistant1No;
	}

	public void setAssistant1No(String assistant1No) {
		this.assistant1No = assistant1No;
	}

	public String getAssistant1Rfid() {
		return assistant1Rfid;
	}

	public void setAssistant1Rfid(String assistant1Rfid) {
		this.assistant1Rfid = assistant1Rfid;
	}

	public String getAssistant1Name() {
		return assistant1Name;
	}

	public void setAssistant1Name(String assistant1Name) {
		this.assistant1Name = assistant1Name;
	}

	public String getAssistant2No() {
		return assistant2No;
	}

	public void setAssistant2No(String assistant2No) {
		this.assistant2No = assistant2No;
	}

	public String getAssistant2Rfid() {
		return assistant2Rfid;
	}

	public void setAssistant2Rfid(String assistant2Rfid) {
		this.assistant2Rfid = assistant2Rfid;
	}

	public String getAssistant2Name() {
		return assistant2Name;
	}

	public void setAssistant2Name(String assistant2Name) {
		this.assistant2Name = assistant2Name;
	}

	public String getCourseNo() {
		return courseNo;
	}

	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}

	public String[] getStudentRfid() {
		return studentRfid;
	}

	public void setStudentRfid(String[] studentRfid) {
		this.studentRfid = studentRfid;
	}

	public CourseTaskResult toCourseTaskResult() throws ParseException {
		CourseTaskResult c = new CourseTaskResult();
		c.setStartTime(DateExTool.string2datetime(this.beginTime));
		c.setEndTime(DateExTool.string2datetime(this.endTime));
		c.setLocalId(id);

		return c;
	}

}
