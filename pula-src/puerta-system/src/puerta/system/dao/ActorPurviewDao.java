/**
 * Created on 2008-12-20 11:31:02
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.vo.MenuVo;

/**
 * 
 * @author tiyi
 * 
 */
public interface ActorPurviewDao {
	/**
	 * @param id
	 * @param asNo
	 * @return
	 */
	String loadMenu(String id, String asNo, boolean strict);

	/**
	 * @param id
	 * @param asAdmin
	 * @return
	 */
	List<Purview> loadByActor(String id, String asAdmin);

	/**
	 * @param id
	 * @param objId
	 * @param apUser
	 */
	void doAssign(String id, String[] objId, Integer apUser);

	/**
	 * @param actorId
	 * @param type
	 */
	void doAssignAll(String actorId, String type);

	/**
	 * @param id
	 * @param asWebuser
	 * @return
	 */
	Map<Module, List<Purview>> getPurviews(String id, String asWebuser);

	/**
	 * @param id
	 * @param as
	 * @return
	 */
	Map<String, String> getFunctions(String id, String as);

	/**
	 * @param asWebuser
	 * @return
	 */
	String loadMenu(String asWebuser, boolean strict);

	/**
	 * @param purviewWorkformManage
	 * @param actorId
	 * @return
	 */
	boolean hasPurview(String purivewNo, String actorId);

	/**
	 * @param string
	 * @param actorId
	 * @return
	 */
	Map<String, String> getPurviewUnder(String no, String actorId);

	/**
	 * @param id
	 * @param no
	 */
	void doAssign(String id, String no);

	/**
	 * @param id
	 * @param purviews
	 */
	void doAssign(String id, List<Purview> purviews);

	/**
	 * @param actorId
	 * @return
	 */
	List<Purview> loadMenuByActor(String actorId);

	/**
	 * @param id
	 * @param actorId
	 */
	void doCopy(String id, String actorId);

	// List<MenuVo> loadMenuWithGroup(String g, String id, String asWebuser);

	List<MenuVo> loadMenuList(String id, String asNo);

	// void doAttach(String id, String[] objId,Integer dataFrom);

	void doAssign(String id, String[] objId);

	List<Object[]> loadByActorWithDataFrom(String id, String asWebuser);

	String[] loadIdByActor(String groupId, String asWebuser);
}
