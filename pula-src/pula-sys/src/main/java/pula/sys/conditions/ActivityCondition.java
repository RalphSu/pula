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
public class ActivityCondition extends CommonCondition {

	private long branchId;
	private int enabledStatus;
	private boolean history;

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

}
