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
public class ClassroomCondition extends CommonCondition {

	private long branchId;
	private String keywords;

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long typeId) {
		this.branchId = typeId;
	}

}
