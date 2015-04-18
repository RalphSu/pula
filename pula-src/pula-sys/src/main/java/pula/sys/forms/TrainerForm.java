package pula.sys.forms;

import pula.sys.domains.Trainer;

public class TrainerForm extends Trainer {

	// private String[] userNo;
	//
	// public String[] getUserNo() {
	// return userNo;
	// }
	//
	// public void setUserNo(String[] userNo) {
	// this.userNo = userNo;
	// }

	public Trainer toTrainer() {
		Trainer obj = new Trainer();
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		return obj;
	}

}
