/**
 * Created on 2009-7-25
 * WXL 2009
 * $Id$
 */
package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

/**
 * 
 * @author tiyi
 * 
 */
public class CourseCondition extends CommonCondition {

	private String categoryId;
	private String keywords;

	private int enabledStatus;

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

}
