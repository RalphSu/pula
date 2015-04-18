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
public class GiftCondition extends CommonCondition {

	private String categoryId;
	private String keywords;
	private int enabledStatus;

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int status) {
		this.enabledStatus = status;
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
