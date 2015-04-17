/**
 * Created on 2008-3-23 11:56:50
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;

import puerta.support.dao.BaseDao;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.ParameterPage;

/**
 * 
 * @author tiyi
 * 
 */
public interface ParameterPageDao extends BaseDao<ParameterPage, String>,
		IWxlPluginSetting<ParameterPage> {

	ParameterPage save(ParameterPage pp);

	ParameterPage update(ParameterPage pp);

	void doAppendPage(List<ParameterPage> pages);

	List<ParameterPage> loadPages();

	void doImportPage(List<ParameterPage> pages);

	List<ParameterPage> loadPages(String appFieldNo);

	/**
	 * @param id
	 * @return
	 */
	ParameterPage loadPageAndAppField(String id);

}
