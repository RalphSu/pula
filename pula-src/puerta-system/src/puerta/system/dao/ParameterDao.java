/**
 * Created on 2008-12-18 03:48:48
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import puerta.support.dao.BaseDao;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.Parameter;
import puerta.system.vo.ParameterWithPageAndFolder;

/**
 * 
 * @author tiyi
 * 
 */
public interface ParameterDao extends BaseDao<Parameter, String>,
		IWxlPluginSetting<Parameter> {

	/**
	 * @return
	 */
	Map<String, Parameter> loadParameters();

	/**
	 * @param hm
	 */
	void saveParameters(HashMap<String, Parameter> hm);

	/**
	 * @param parameterPageId
	 * @return
	 */
	Map<String, List<Parameter>> loadParametersByPage(String parameterPageId);

	/**
	 * @param parameter
	 */
	Parameter update(Parameter parameter);

	/**
	 * @param className
	 * @param id
	 */
	void remove(String className, String id);

	/**
	 * @param appFieldNo
	 * @return
	 */
	Map<String, List<Parameter>> loadParameterByAppField(String appFieldNo);

	/**
	 * @param params
	 * @param values
	 */
	void updateParams(String[] params, String[] values);

	/**
	 * @param parameter
	 */
	Parameter save(Parameter parameter);

	/**
	 * @param id
	 * @return
	 */
	ParameterWithPageAndFolder beforeEdit(String id);

	/**
	 * 
	 */
	void doClearAll();

	/**
	 * @param params
	 * @param b
	 */
	void doImport(List<Parameter> params, boolean b);

	/**
	 * @param parameterFolderId
	 * @return
	 */
	int getMaxIndexNo(String parameterFolderId);

	/**
	 * @param id
	 * @return
	 */
	Parameter loadInstance(String id);

}
