/**
 * Created on 2007-1-19 11:04:34
 *
 * DiagCN.COM 2004-2006
 * $Id: InsiderMgr.java,v 1.1 2007/01/19 07:02:12 tiyi Exp $
 */
package puerta.system.dao;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.InsiderCondition;
import puerta.system.intfs.IWxlPluginActor;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.Insider;
import puerta.system.vo.ActorWithPurview;
import puerta.system.vo.ActorWithPurviewAll;

/**
 * 
 * @author tiyi
 * 
 */
public interface InsiderDao extends BaseDao<Insider, String>,
		IWxlPluginSetting<Insider>, IWxlPluginActor<Insider> {
	ActorWithPurview<Insider> doLogin(String loginId, String password);

	void doChangePassword(String id, String oldPassword, String newPassword);

	void doLogout(String id);

	Insider save(Insider insider);

	Insider findByLoginId(String loginId);

	PaginationSupport<Insider> search(InsiderCondition condition, int pageNo);

	Insider update(Insider insider, boolean changePassword);

	/**
	 * @return
	 */
	Insider doDefaultInsider();

	/**
	 * @param id
	 * @return
	 */
	ActorWithPurviewAll<Insider> beforeSetupPurview(String id);

	/**
	 * @param no
	 * @param password
	 * @return
	 */
	boolean checkLogin(String no, String password);

}
