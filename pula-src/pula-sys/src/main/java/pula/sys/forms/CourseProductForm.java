package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Branch;
import pula.sys.domains.CourseProduct;

public class CourseProductForm extends CourseProduct {
	private String beginTimeText;
	private String endTimeText;
	private long branchId;

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public String getBeginTimeText() {
		return beginTimeText;
	}

	public void setBeginTimeText(String beginTimeText) {
		this.beginTimeText = beginTimeText;
	}

	public String getEndTimeText() {
		return endTimeText;
	}

	public void setEndTimeText(String endTimeText) {
		this.endTimeText = endTimeText;
	}

	public CourseProduct toCourseProduct() {
		CourseProduct obj = new CourseProduct();
		obj.setBeginTime(DateExTool.getDate(this.beginTimeText));
		obj.setComments(this.getComments());
		obj.setCourseCount(this.getCourseCount());
		obj.setCreator(this.getCreator());
		obj.setEndTime(DateExTool.getDate(this.endTimeText));
		obj.setGiftCount(this.getGiftCount());
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setPrice(this.getPrice());
		obj.setUpdater(this.getUpdater());
		obj.setBranch(Branch.create(branchId));
		return obj;
	}

}
