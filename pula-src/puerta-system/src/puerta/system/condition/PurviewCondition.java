package puerta.system.condition;

import puerta.support.dao.CommonCondition;

public class PurviewCondition extends CommonCondition {

	private String appFieldNo;

	public String getAppFieldNo() {
		return appFieldNo;
	}

	public void setAppFieldNo(String appFieldNo) {
		this.appFieldNo = appFieldNo;
	}

	private String moduleId;

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

}
