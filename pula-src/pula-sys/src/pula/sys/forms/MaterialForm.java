package pula.sys.forms;

import pula.sys.domains.Material;
import pula.sys.domains.SysCategory;

public class MaterialForm extends Material {

	private String categoryId;

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public Material toMaterial() {
		Material obj = new Material();
		obj.setBrand(this.getBrand());
		obj.setCategory(SysCategory.create(categoryId));
		obj.setComments(this.getComments());
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setRaw(this.getRaw());
		obj.setSuperficialArea(this.getSuperficialArea());
		obj.setUnit(this.getUnit());
		obj.setWeight(this.getWeight());
		obj.setPinyin(this.getPinyin());
		return obj;
	}
}
