/**
 * Created on 2006-12-6 09:37:20
 *
 * DiagCN.COM 2004-2006
 * $Id: ModuleCondition.java,v 1.3 2006/12/20 13:38:00 tiyi Exp $
 */
package puerta.system.condition;

import puerta.support.dao.CommonCondition;

/**
 * 
 * @author tiyi
 * 
 */
public class SysRoleCondition extends CommonCondition {

	private String appFieldNo;

	public String getAppFieldNo() {
		return appFieldNo;
	}

	public void setAppFieldNo(String appFieldNo) {
		this.appFieldNo = appFieldNo;
	}

}
