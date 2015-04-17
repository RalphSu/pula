/**
 * Created on 2008-12-21 08:15:08
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.condition;

import puerta.support.dao.CommonCondition;

/**
 * 
 * @author tiyi
 * 
 */
public class DictLimbCondition extends CommonCondition {

	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
