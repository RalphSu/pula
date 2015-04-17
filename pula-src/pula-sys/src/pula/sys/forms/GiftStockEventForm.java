package pula.sys.forms;

import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.StockEvent;

public class GiftStockEventForm extends StockEvent {
	// private String eventTimeText;

	private String giftNo;

	public String getGiftNo() {
		return giftNo;
	}

	public void setGiftNo(String materialNo) {
		this.giftNo = materialNo;
	}

	public GiftStockEvent toGiftStockEvent() {
		GiftStockEvent obj = new GiftStockEvent();
		obj.setBranch(this.getBranch());
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setOutNo(this.getOutNo());
		obj.setQuantity(this.getQuantity());
		obj.setTarget(this.getTarget());
		return obj;
	}

}
