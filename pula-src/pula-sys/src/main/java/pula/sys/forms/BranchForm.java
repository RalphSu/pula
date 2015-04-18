package pula.sys.forms;

import pula.sys.domains.Branch;

public class BranchForm extends Branch {

	public Branch toBranch() {
		Branch obj = new Branch();
		obj.setAddress(this.getAddress());
		obj.setCreatedTime(this.getCreatedTime());
		obj.setCreator(this.getCreator());
		obj.setEmail(this.getEmail());
		obj.setEnabled(this.isEnabled());
		obj.setShowInWeb(this.isShowInWeb());
		obj.setId(this.getId());
		obj.setLinkman(this.getLinkman());
		obj.setMobile(this.getMobile());
		obj.setFax(this.getFax());
		obj.setComments(this.getComments());

		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setPhone(this.getPhone());
		obj.setUpdatedTime(this.getUpdatedTime());
		obj.setUpdater(this.getUpdater());
		obj.setPrefix(this.getPrefix());

		return obj;
	}

}
