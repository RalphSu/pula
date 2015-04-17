package puerta.system.condition;

import puerta.support.dao.CommonCondition;

public class InsiderCondition extends CommonCondition {
	private String loginId;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

}
