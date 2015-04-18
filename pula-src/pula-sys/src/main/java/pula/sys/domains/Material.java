package pula.sys.domains;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 物料
 * 
 * @author tiyi
 * 
 */
@WxlDomain("物料")
public class Material implements LoggablePo {
	private long id;
	private String no, name, brand;
	private String unit, raw; // 规格，原材料
	private double weight, superficialArea;
	private String comments;

	private boolean removed;
	private SysCategory category;
	private String pinyin;
	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getSuperficialArea() {
		return superficialArea;
	}

	public void setSuperficialArea(double superficialArea) {
		this.superficialArea = superficialArea;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public SysCategory getCategory() {
		return category;
	}

	public void setCategory(SysCategory category) {
		this.category = category;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static Material create(Long id) {
		if (id == null) {
			return null;
		}
		Material m = new Material();
		m.id = id;
		return m;
	}

}
