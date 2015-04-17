package pula.sys.forms;

import pula.sys.domains.Branch;
import pula.sys.domains.GiftTransfer;

public class GiftTransferForm extends GiftTransfer {

	private String giftNo;
	private long toBranchId;

	public long getToBranchId() {
		return toBranchId;
	}

	public void setToBranchId(long toBranchId) {
		this.toBranchId = toBranchId;
	}

	public String getGiftNo() {
		return giftNo;
	}

	public void setGiftNo(String materialNo) {
		this.giftNo = materialNo;
	}

	public GiftTransfer toGiftTransfer() {
		GiftTransfer obj = new GiftTransfer();
		obj.setBranch(this.getBranch());
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setSentQuantity(this.getSentQuantity());
		obj.setUpdater(this.getUpdater());
		obj.setToBranch(Branch.create(toBranchId));
		return obj;
	}

}
