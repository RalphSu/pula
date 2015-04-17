package pula.sys.forms;

import pula.sys.domains.StockEvent;

public class StockEventForm extends StockEvent {
	// private String eventTimeText;

	private String materialNo;

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public StockEvent toStockEvent() {
		StockEvent obj = new StockEvent();
		obj.setBranch(this.getBranch());
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		// obj.setEventTime(DateExTool.getDate(this.eventTimeText));
		obj.setId(this.getId());
		obj.setOutNo(this.getOutNo());
		obj.setQuantity(this.getQuantity());
		obj.setTarget(this.getTarget());
		return obj;
	}

}
