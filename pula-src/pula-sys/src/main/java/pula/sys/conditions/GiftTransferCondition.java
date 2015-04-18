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
public class GiftTransferCondition extends CommonCondition {

	public static final int TARGET_MY = 1;
	public static final int TARGET_AUDIT = 2;
	public static final int TARGET_SENT = 3;
	public static final int TARGET_RECEIVE = 3;

	private long branchId, id, toBranchId;
	private String giftNo;
	private int status;
	private String beginDate, endDate;
	private int target;

	public long getToBranchId() {
		return toBranchId;
	}

	public void setToBranchId(long toBranchId) {
		this.toBranchId = toBranchId;
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

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int enabledStatus) {
		this.status = enabledStatus;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGiftNo() {
		return giftNo;
	}

	public void setGiftNo(String keywords) {
		this.giftNo = keywords;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long typeId) {
		this.branchId = typeId;
	}

}
