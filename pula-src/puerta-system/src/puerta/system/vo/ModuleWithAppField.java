/**
 * Created on 2008-12-19 01:59:35
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

import java.util.List;

import puerta.system.po.AppField;
import puerta.system.po.Module;

/**
 * 
 * @author tiyi
 * 
 */
public class ModuleWithAppField {

	private Module module;
	private List<AppField> scopes;

	/**
	 * @param module2
	 * @param scopes2
	 */
	public ModuleWithAppField(Module module2, List<AppField> scopes2) {
		this.module = module2;
		this.scopes = scopes2;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<AppField> getScopes() {
		return scopes;
	}

	public void setScopes(List<AppField> scopes) {
		this.scopes = scopes;
	}

}
