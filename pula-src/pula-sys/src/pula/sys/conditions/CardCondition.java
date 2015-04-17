package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

public class CardCondition extends CommonCondition {

	private String mac;
	private int enabledStatus, status;
	private long id;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getEnabledStatus() {
		return enabledStatus;
	}

	public void setEnabledStatus(int blockStatus) {
		this.enabledStatus = blockStatus;
	}

}
