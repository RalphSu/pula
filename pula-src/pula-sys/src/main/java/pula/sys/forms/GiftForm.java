package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Gift;
import pula.sys.domains.SysCategory;

public class GiftForm extends Gift {

	private String categoryId;
	private String beginTimeText, endTimeText;

	public String getBeginTimeText() {
		return beginTimeText;
	}

	public void setBeginTimeText(String beginDate) {
		this.beginTimeText = beginDate;
	}

	public String getEndTimeText() {
		return endTimeText;
	}

	public void setEndTimeText(String endDate) {
		this.endTimeText = endDate;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Gift toGift() {
		Gift obj = new Gift();
		obj.setBrand(this.getBrand());
		obj.setCategory(SysCategory.create(categoryId));
		obj.setComments(this.getComments());
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setRaw(this.getRaw());
		obj.setSuperficialArea(this.getSuperficialArea());
		obj.setUnit(this.getUnit());
		obj.setWeight(this.getWeight());
		obj.setPinyin(this.getPinyin());
		obj.setBeginTime(DateExTool.getDate(this.beginTimeText));
		obj.setEndTime(DateExTool.getDate(this.endTimeText));
		obj.setPoints(this.getPoints());
		return obj;
	}
}
