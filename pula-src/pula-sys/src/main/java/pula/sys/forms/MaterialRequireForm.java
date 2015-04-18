package pula.sys.forms;

import pula.sys.domains.MaterialRequire;

public class MaterialRequireForm extends MaterialRequire {

	private String materialNo;

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public MaterialRequire toMaterialRequire() {
		MaterialRequire obj = new MaterialRequire();
		obj.setBranch(this.getBranch());
		obj.setComments(this.getComments());
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setMaterial(this.getMaterial());
		obj.setQuantity(this.getQuantity());
		obj.setUpdater(this.getUpdater());
		return obj;
	}

}
