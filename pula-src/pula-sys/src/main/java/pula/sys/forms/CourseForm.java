package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Course;
import pula.sys.domains.SysCategory;

public class CourseForm extends Course {
	private String expiredTimeText;
	private String publishTimeText;
	private String categoryId, subCategoryId;

	public String getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(String subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getExpiredTimeText() {
		return expiredTimeText;
	}

	public void setExpiredTimeText(String expiredTimeText) {
		this.expiredTimeText = expiredTimeText;
	}

	public String getPublishTimeText() {
		return publishTimeText;
	}

	public void setPublishTimeText(String publishTimeText) {
		this.publishTimeText = publishTimeText;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Course toCourse() {
		Course obj = new Course();
		obj.setCategory(SysCategory.create(categoryId));
		obj.setSubCategory(SysCategory.create(subCategoryId));
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		obj.setExpiredTime(DateExTool.getDate(this.expiredTimeText));
		obj.setId(this.getId());
		obj.setIndexNo(this.getIndexNo());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setPublishTime(DateExTool.getDate(this.publishTimeText));
		obj.setShowInWeb(this.isShowInWeb());
		obj.setUpdater(this.getUpdater());
		obj.setKey(this.getKey());
		obj.setMinutes(this.getMinutes());
		return obj;
	}

}
