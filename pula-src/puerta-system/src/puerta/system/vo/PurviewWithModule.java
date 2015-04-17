/**
 * Created on 2008-12-20 11:58:59
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;

import java.util.List;

import puerta.system.po.Module;
import puerta.system.po.Purview;

/**
 * 
 * @author tiyi
 * 
 */
public class PurviewWithModule {

	private Purview purview;
	private Module module;
	private int maxIndexNo;
	private List<Module> allModules;

	/**
	 * @param purview2
	 * @param module2
	 * @param maxIndexNo2
	 * @param modules
	 */
	public PurviewWithModule(Purview purview2, Module module2,
			int maxIndexNo2, List<Module> modules) {
		this.purview = purview2;
		this.module = module2;
		this.maxIndexNo = maxIndexNo2;
		this.allModules = modules;
	}

	public Purview getPurview() {
		return purview;
	}

	public void setPurview(Purview purview) {
		this.purview = purview;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public int getMaxIndexNo() {
		return maxIndexNo;
	}

	public void setMaxIndexNo(int maxIndexNo) {
		this.maxIndexNo = maxIndexNo;
	}

	public List<Module> getAllModules() {
		return allModules;
	}

	public void setAllModules(List<Module> allModules) {
		this.allModules = allModules;
	}

}
