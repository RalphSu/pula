package pula.sys.conditions;

public class AttendanceRecordCondition {

	private String beginDate, endDate;
	private String ownerNo;
	private int dataFrom;
	private String no;

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

	public String getOwnerNo() {
		return ownerNo;
	}

	public void setOwnerNo(String employeeNo) {
		this.ownerNo = employeeNo;
	}

	public int getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(int dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
