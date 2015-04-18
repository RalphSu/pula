package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

public class StudentCondition extends CommonCondition {

	private int status, gender;
	private String identity;
	private long branchId, id;
	private int level;
	private String barcode;
	private int enabledStatus;

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

}
