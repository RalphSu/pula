/**
 * Created on 2008-12-18 03:14:42
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;

import puerta.support.dao.BaseDao;
import puerta.system.condition.PurviewCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.Purview;
import puerta.system.po.RequestUri;
import puerta.system.vo.PurviewWithModule;

/**
 * 
 * @author tiyi
 * 
 */
public interface PurviewDao extends BaseDao<Purview, String>,
		IWxlPluginSetting<Purview> {
	String PASS = "PASS";

	/**
	 * @param asAdmin
	 * @return
	 */
	List<Purview> loadAll(String asAdmin);

	/**
	 * @param name
	 */
	void save(String name);

	/**
	 * @param purview
	 * @return
	 */
	Purview update(Purview purview);

	/**
	 * @param id
	 * @return
	 */
	PurviewWithModule beforeEdit(String id);

	/**
	 * @param id
	 */
	void doMoveUp(String id);

	/**
	 * @param id
	 */
	void doMoveDown(String id);

	/**
	 * @param id
	 * @param targetPurviewNo
	 */
	String doMoveToPurview(String id, String targetPurviewNo);

	/**
	 * @param id
	 * @param targetModule
	 */
	void doMoveToModule(String id, String targetModule);

	/**
	 * @param objId
	 */
	void remove(String[] objId);

	/**
	 * @param moduleId
	 * @return
	 */
	PurviewWithModule beforeSaveByModuleId(String moduleId);

	/**
	 * @param purviewId
	 * @return
	 */
	PurviewWithModule beforeSaveByPurviewId(String purviewId);

	/**
	 * @param purview
	 * @return
	 */
	Purview save(Purview purview);

	/**
	 * @param condition
	 * @return
	 */
	List<Purview> search(PurviewCondition condition);

	/**
	 * @param id
	 * @return
	 */
	int getMaxIndexNo(String id);

	/**
	 * @param applicableScopeNo
	 * @return
	 */
	List<Purview> loadFunctionPoints(String applicableScopeNo);

	/**
	 * 
	 */
	void doClear();

	/**
	 * @param purviews
	 * @param b
	 */
	void doImport(List<Purview> purviews, boolean b, String actorId);

	/**
	 * @param ma
	 * @param aid
	 * @return
	 */
	String doCheck(RequestUri url, String aid);

	/**
	 * @param id
	 * @return
	 */
	Purview fetchPurviewWithModule(String id);

	/**
	 * @param id
	 * @return
	 */
	Purview fetchAll(String id);

	List<Purview> getChildren(String id);

	Purview getByDefaultURL(String processUri, String string);

	String getIdByNo(String no);

}
