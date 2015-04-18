/*
 * Created on 2005-7-12
 *$Id: UserLogMgr.java,v 1.2 2006/12/10 14:41:27 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.SysUserLogCondition;
import pula.sys.domains.SysUserLog;

/**
 * @author tiyi 2005-7-12 17:47:49
 */
public interface SysUserLogDao extends BaseDao<SysUserLog, String> {

	// public UserLog doSaveSystemLog(UserLog log);
	//
	// public UserLog doSaveSystemLog(String event, String userId,
	// String extendInfo);

	public void deleteSysUserLog(SysUserLogCondition condition);

	public PaginationSupport<SysUserLog> search(SysUserLogCondition condition,
			int pageNo);

	public List<SysUserLog> loadLogs(SysUserLogCondition condition);

}
