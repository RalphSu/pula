package pula.sys.forms;

import pula.sys.domains.Chargeback;
import pula.sys.domains.OrderForm;

public class ChargebackForm extends Chargeback {

	private long orderFormId;

	public long getOrderFormId() {
		return orderFormId;
	}

	public void setOrderFormId(long orderFormId) {
		this.orderFormId = orderFormId;
	}

	public Chargeback toChargeback() {
		Chargeback obj = new Chargeback();
		obj.setBackAmount(this.getBackAmount());
		obj.setBackCourses(this.getBackCourses());
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setNo(this.getNo());
		obj.setOrderForm(OrderForm.create(orderFormId));
		obj.setStatus(this.getStatus());
		obj.setUpdater(this.getUpdater());
		return obj;
	}
}
