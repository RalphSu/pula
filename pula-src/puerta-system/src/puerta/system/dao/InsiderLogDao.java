/*
 * Created on 2005-7-12
 *$Id: UserLogMgr.java,v 1.2 2006/12/10 14:41:27 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.dao;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.InsiderLogCondition;
import puerta.system.po.InsiderLog;

/**
 * @author tiyi 2005-7-12 17:47:49
 */
public interface InsiderLogDao extends BaseDao<InsiderLog, String> {

	// public UserLog doSaveSystemLog(UserLog log);
	//
	// public UserLog doSaveSystemLog(String event, String userId,
	// String extendInfo);

	public void deleteInsiderLog(InsiderLogCondition condition);

	public PaginationSupport<InsiderLog> search(InsiderLogCondition condition,
			int pageNo);

}
