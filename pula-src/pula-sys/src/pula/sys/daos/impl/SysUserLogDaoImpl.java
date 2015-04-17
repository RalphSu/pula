/*
 * Created on 2005-7-12
 *$Id: AdminLogDaoImpl.java,v 1.5 2006/12/11 14:06:16 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package pula.sys.daos.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.support.mls.Mls;
import puerta.support.utils.DateExTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.LoggerDao;
import pula.sys.conditions.SysUserLogCondition;
import pula.sys.daos.SysUserLogDao;
import pula.sys.domains.SysUserLog;

/**
 * @author tiyi 2005-07-12 17:49:23
 */
@Repository
public class SysUserLogDaoImpl extends HibernateGenericDao<SysUserLog, String>
		implements SysUserLogDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private Mls mls;

	public void deleteSysUserLog(SysUserLogCondition condition) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM SysUserLog WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(condition.getUserId())) {
			sb.append(" AND sysUser.loginId LIKE ? ");
			list.add("%" + condition.getUserId() + "%");
		}

		if (!StringUtils.isEmpty(condition.getEvent())) {
			sb.append(" AND event LIKE ? ");
			list.add("%" + condition.getEvent() + "%");
		}

		if (!StringUtils.isEmpty(condition.getExtendInfo())) {
			sb.append(" AND extendInfo LIKE ? ");
			list.add("%" + condition.getExtendInfo() + "%");
		}

		Calendar beginDate = DateExTool.getPrevDate(condition.getBeginDate());
		Calendar endDate = DateExTool.getNextDate(condition.getEndDate());
		if (beginDate != null) {
			sb.append(" AND eventTime  > ? ");

			logger.debug("beginDate=" + DateExTool.dateTime2String(beginDate));

			list.add(beginDate);
		}
		if (endDate != null) {
			sb.append(" AND eventTime  <? ");
			logger.debug("endDate=" + DateExTool.dateTime2String(endDate));
			list.add(endDate);
		}

		super.delete(sb.toString(), list.toArray());
		getHibernateTemplate().flush();
		loggerDao.doLog(mls.t("platform.userlog.clearLogsLog"),
				condition.getBeginDate() + " >" + condition.getEndDate());

	}

	public PaginationSupport<SysUserLog> search(SysUserLogCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);

		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.desc("eventTime"));
	}

	public DetachedCriteria makeDetachedCriteria(SysUserLogCondition condition) {

		DetachedCriteria dc = DetachedCriteria.forClass(SysUserLog.class);
		dc.createAlias("sysUser", "sysUser");
		// if (!StringUtils.isEmpty(condition.getUserId())) {
		// dc.add(Restrictions.like("sysUser.loginId", condition.getUserId(),
		// MatchMode.ANYWHERE));
		// }
		
		HibernateTool.likeIfNotEmpty(dc, "sysUser.loginId","sysUser.name",condition.getUserId());

		if (!StringUtils.isEmpty(condition.getEvent())) {
			dc.add(Restrictions.like("event", condition.getEvent(),
					MatchMode.ANYWHERE));
		}

		if (!StringUtils.isEmpty(condition.getExtendInfo())) {
			dc.add(Restrictions.like("extendInfo", condition.getExtendInfo(),
					MatchMode.ANYWHERE));
		}

		Calendar beginDate = DateExTool.getPrevDate(condition.getBeginDate());
		Calendar endDate = DateExTool.getNextDate(condition.getEndDate());
		if (beginDate != null) {
			dc.add(Restrictions.gt("eventTime", beginDate));
		}
		if (endDate != null) {
			dc.add(Restrictions.lt("eventTime", endDate));
		}

		return dc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nilnut.groundwork.system.userlog.AdminLogDao#loadSystemLogs(com
	 * .nilnut .groundwork.system.userlog.AdminLogCondition)
	 */
	public List<SysUserLog> loadLogs(SysUserLogCondition condition) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);

		return super.findByCriteria(criteria);

	}

}
