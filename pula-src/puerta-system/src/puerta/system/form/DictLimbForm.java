package puerta.system.form;

import puerta.system.po.DictLimb;

public class DictLimbForm extends DictLimb {

	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public DictLimb toDictLimb() {
		DictLimb p = new DictLimb();
		p.setNo(this.getNo());
		p.setName(this.getName());
		p.setParent(DictLimb.create(parentId));
		p.setId(this.getIdentify());
		p.setIndexNo(this.getIndexNo());
		return p;
	}
}
