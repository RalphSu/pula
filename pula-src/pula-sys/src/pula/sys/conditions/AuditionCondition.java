package pula.sys.conditions;

public class AuditionCondition {

	private String beginDate, endDate;
	private String keywords;
	private long branchId;
	private String salesmanNo;
	private String resultId;
	private int closedStatus;
	private long id;

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

}
