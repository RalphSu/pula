package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.ActorWithPurviewAll;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.SysUserCondition;
import pula.sys.domains.SysUser;

public interface SysUserDao extends BaseDao<SysUser, String> {

	/**
	 * @param actorId
	 */
	void doLogout(String actorId);

	/**
	 * @param id
	 * @param oldPassword
	 * @param newPassword
	 */
	void updatePassword(String id, String oldPassword, String newPassword);

	/**
	 * @param id
	 * @return
	 */
	SysUser findById(String id);

	/**
	 * @param admin
	 * @param changePassword
	 * @return
	 */
	Mix<SysUser, Boolean> update(SysUser admin, boolean changePassword);

	/**
	 * @param objId
	 * @param enable
	 */
	void doEnable(String[] objId, boolean enable);

	/**
	 * @param objId
	 */
	void deleteById(String[] objId);

	/**
	 * @param admin
	 * @return
	 */
	SysUser save(SysUser admin);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<SysUser> search(SysUserCondition condition, int pageNo);

	/**
	 * @param id
	 * @return
	 */
	ActorWithPurviewAll<SysUser> beforeSetupPurview(String id);

	/**
	 * @param loginId
	 * @return
	 */
	SysUser findByLoginId(String loginId);

	void registerSetting();

	SysUser doLogin(String loginId, String password);

	List<SysUser> loadByKeywords(String no, long bid);

	MapList loadByKeywords(String no, String role, long bid);

	List<SysUser> loadByRole(String... role);

	List<String> listByGroup(String id);

	SysUser loadWithRole(String id);

	void updateSalesmen(String name, String no, long branch_id, long id);

	void removeBySalesmen(Long[] id);

	void enableBySalesmen(Long[] id, boolean enable);

	MapBean meta4Login(String username, String password);

}
