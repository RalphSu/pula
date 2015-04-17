/**
 * Created on 2008-12-18 12:31:11
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.ModuleCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.Module;
import puerta.system.vo.ModuleWithAppField;

/**
 * 
 * @author tiyi
 * 
 */
public interface ModuleDao extends BaseDao<Module, String>,
		IWxlPluginSetting<Module> {

	/**
	 * @param id
	 * @return
	 */
	List<Module> loadModules(String id);

	/**
	 * @param asAdmin
	 * @return
	 */
	List<Module> loadAll(String asAdmin);

	/**
	 * @param module
	 * @return
	 */
	Module save(Module module);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<Module> search(ModuleCondition condition, int pageNo);

	/**
	 * @param module
	 * @return
	 */
	Module update(Module module);

	/**
	 * @param id
	 * @return
	 */
	ModuleWithAppField beforeEdit(String id);

	/**
	 * 
	 */
	void doClear();

	/**
	 * @param modules
	 * @param b
	 */
	void doImport(List<Module> modules, boolean b);

	int getMaxIndexNo(String appFieldNo);



}
