package pula.sys.forms;

import org.apache.commons.lang.StringUtils;

import pula.sys.domains.SysCategory;

public class SysCategoryForm extends SysCategory {

	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public SysCategory toSysCategory() {
		SysCategory p = new SysCategory();
		p.setNo(StringUtils.trim(this.getNo()));
		p.setName(this.getName());
		p.setParentCategory(SysCategory.create(parentId));
		p.setId(this.getIdentify());
		p.setIndexNo(this.getIndexNo());

		return p;
	}
}
