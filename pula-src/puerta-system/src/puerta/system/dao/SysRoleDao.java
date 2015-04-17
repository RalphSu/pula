/**
 * Created on 2008-12-18 12:31:11
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.SysRoleCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.AppField;
import puerta.system.po.Purview;
import puerta.system.po.SysRole;
import puerta.system.vo.ActorWithPurviewAll;
import puerta.system.vo.EntityWithTypes;

/**
 * 
 * @author tiyi
 * 
 */
public interface SysRoleDao extends BaseDao<SysRole, String>,
		IWxlPluginSetting<SysRole> {

	/**
	 * @param id
	 * @return
	 */
	List<SysRole> loadSysRoles(String id);

	/**
	 * @param asAdmin
	 * @return
	 */
	List<SysRole> loadAll(String asAdmin);

	/**
	 * @param actorRole
	 * @return
	 */
	SysRole save(SysRole actorRole);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<SysRole> search(SysRoleCondition condition, int pageNo);

	/**
	 * @param actorRole
	 * @return
	 */
	SysRole update(SysRole actorRole);

	/**
	 * @param id
	 * @return
	 */
	EntityWithTypes<SysRole, AppField> beforeEdit(String id);

	/**
	 * 
	 */
	void doClear();

	/**
	 * @param actorRoles
	 * @param b
	 */
	void doImport(List<SysRole> actorRoles, boolean b);

	/**
	 * @param id
	 * @return
	 */
	ActorWithPurviewAll<SysRole> beforeSetupPurview(String id);

	/**
	 * @param roleNo
	 * @return
	 */
	List<Purview> loadPurviews(String roleNo);

	/**
	 * @param userId
	 * @param roleComposeDirector
	 * @return
	 */
	boolean hasRole(String userId, String role);

	/**
	 * @param id
	 * @return
	 */
	Map<String, String> loadRoles(String id);

	/**
	 * @param id
	 * @param roleId
	 */
	void doAssign(String id, String[] roleId);

	String getName(String id);

	String getIdByNo(String roleSales);

}
