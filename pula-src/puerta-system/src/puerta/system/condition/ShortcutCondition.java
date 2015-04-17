/**
 * Created on 2006-12-6 09:37:20
 *
 * DiagCN.COM 2004-2006
 * $Id: RoleCondition.java,v 1.1 2006/12/08 12:41:37 tiyi Exp $
 */
package puerta.system.condition;

import puerta.support.dao.CommonCondition;

/**
 * 
 * @author tiyi
 * 
 */
public class ShortcutCondition extends CommonCondition {

	private String purviewNo;

	private String purviewName;

	public String getPurviewName() {
		return purviewName;
	}

	public void setPurviewName(String purviewName) {
		this.purviewName = purviewName;
	}

	public String getPurviewNo() {
		return purviewNo;
	}

	public void setPurviewNo(String purviewNo) {
		this.purviewNo = purviewNo;
	}

}
