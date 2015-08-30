package pula.sys.conditions;

public class AuditionCondition {

	private String beginDate, endDate;
	private String keywords;
	private long branchId;
	private String salesmanNo;
	private String studentNo;
	private String studentName;
	private String resultId;
	private int closedStatus;
	private long id;
	
	private String phone;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getSalesmanNo() {
		return salesmanNo;
	}

	public void setSalesmanNo(String userNo) {
		this.salesmanNo = userNo;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public int getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(int closedStatus) {
		this.closedStatus = closedStatus;
	}

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
