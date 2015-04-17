/**
 * Created on 2008-3-24 12:04:50
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.support.dao.BaseDao;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.ParameterFolder;
import puerta.system.vo.ParameterFolderWithPage;

/**
 * 
 * @author tiyi
 * 
 */
public interface ParameterFolderDao extends BaseDao<ParameterFolder, String>,
		IWxlPluginSetting<ParameterFolder> {

	/**
	 * @param parameterPageId
	 * @return
	 */
	List<ParameterFolder> loadFolders(String parameterPageId);

	ParameterFolder save(ParameterFolder parameterFolder);

	ParameterFolder update(ParameterFolder parameterFolder);

	/**
	 * @param appFieldNo
	 * @return
	 */
	Map<String, List<ParameterFolder>> loadFoldersByAppField(String appFieldNo);

	/**
	 * @param folders
	 */
	void doImportFolder(List<ParameterFolder> folders);

	/**
	 * @param folders
	 */
	void doAppendFolder(List<ParameterFolder> folders);

	List<ParameterFolder> loadFolders();

	/**
	 * @param id
	 * @return
	 */
	ParameterFolderWithPage beforeEdit(String id);

	/**
	 * @param parameterFolderId
	 * @return
	 */
	ParameterFolder loadFolderWithParent(String parameterFolderId);

}
