package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Activity;

public class ActivityForm extends Activity {
	private String beginDateText;
	private String endDateText;

	private Long[] branchId;

	public String getBeginDateText() {
		return beginDateText;
	}

	public void setBeginDateText(String beginDateText) {
		this.beginDateText = beginDateText;
	}

	public String getEndDateText() {
		return endDateText;
	}

	public void setEndDateText(String endDateText) {
		this.endDateText = endDateText;
	}

	public Long[] getBranchId() {
		return branchId;
	}

	public void setBranchId(Long[] branchId) {
		this.branchId = branchId;
	}

	public Activity toActivity() {
		Activity obj = new Activity();
		obj.setActivityBranches(this.getActivityBranches());
		obj.setBeginDate(DateExTool.getDate(this.beginDateText));
		obj.setComments(this.getComments());
		obj.setEndDate(DateExTool.getDate(this.endDateText));
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setPartner(this.getPartner());
		return obj;
	}

}
