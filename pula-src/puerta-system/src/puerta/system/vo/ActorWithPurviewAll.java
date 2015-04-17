/**
 * Created on 2008-12-18 03:08:26
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
public class ActorWithPurviewAll<T> {

	private T actor;
	private List<Purview> checkedPurviews;
	private List<Purview> allPurviews;
	private List<Module> modules;

	/**
	 * @param admin2
	 * @param purviews
	 * @param checkedPurviews2
	 * @param module
	 */
	public ActorWithPurviewAll(T admin2, List<Purview> purviews,
			List<Purview> checkedPurviews2, List<Module> module) {
		this.actor = admin2;
		this.allPurviews = purviews;
		this.checkedPurviews = checkedPurviews2;
		this.modules = module;
	}

	public T getActor() {
		return actor;
	}

	public void setActor(T actor) {
		this.actor = actor;
	}

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}

	public List<Purview> getCheckedPurviews() {
		return checkedPurviews;
	}

	public void setCheckedPurviews(List<Purview> checkedPurviews) {
		this.checkedPurviews = checkedPurviews;
	}

	public List<Purview> getAllPurviews() {
		return allPurviews;
	}

	public void setAllPurviews(List<Purview> allPurviews) {
		this.allPurviews = allPurviews;
	}

}
