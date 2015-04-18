package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

public class OrderFormCondition extends CommonCondition {

	private String studentNo, teacherNo;
	private String beginDate, endDate;
	private int payStatus;
	private int status;
	private String salesmanNo;
	private long courseProductId;

	private long branchId, id;
	private int commissionType;

	private boolean forChargeback, forStudents;

	public boolean isForStudents() {
		return forStudents;
	}

	public void setForStudents(boolean forStudents) {
		this.forStudents = forStudents;
	}

	public boolean isForChargeback() {
		return forChargeback;
	}

	public void setForChargeback(boolean forChargeback) {
		this.forChargeback = forChargeback;
	}

	public long getCourseProductId() {
		return courseProductId;
	}

	public void setCourseProductId(long courseProductId) {
		this.courseProductId = courseProductId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(int commissionType) {
		this.commissionType = commissionType;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSalesmanNo() {
		return salesmanNo;
	}

	public void setSalesmanNo(String salesmanNo) {
		this.salesmanNo = salesmanNo;
	}

}
