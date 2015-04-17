/**
 * Created on 2006-12-5 08:31:48
 *
 * DiagCN.COM 2004-2006
 * $Id: MaintainerLogMgrImpl.java,v 1.1 2007/01/28 16:13:10 tiyi Exp $
 */
package puerta.system.dao.impl;

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
import puerta.system.condition.InsiderLogCondition;
import puerta.system.dao.InsiderLogDao;
import puerta.system.po.InsiderLog;

/**
 * 
 * @author tiyi
 * 
 */
@Repository
public class InsiderLogDaoImpl extends HibernateGenericDao<InsiderLog, String>
		implements InsiderLogDao {

	@Resource
	Mls mls;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.insiderlog.InsiderLogMgr#deleteInsiderLog
	 * (com.nut.groundwork.platform.insiderlog.InsiderLogCondition)
	 */
	public void deleteInsiderLog(InsiderLogCondition condition) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM InsiderLog WHERE 1=1 ");
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(condition.getUserId())) {
			sb.append(" AND insider.loginId LIKE ? ");
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
			list.add(beginDate);
		}
		if (endDate != null) {
			sb.append(" AND eventTime  <? ");
			list.add(endDate);
		}

		String hql = sb.toString();
		logger.debug("hql=" + hql);
		logger.debug("list.size=" + list.size());
		super.delete(hql, list.toArray());
		getHibernateTemplate().flush();
		loggerDao.doLog(mls.t("platform.insiderlog.clearLog"),
				condition.getBeginDate() + " >" + condition.getEndDate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.insiderlog.InsiderLogMgr#search(wxl.pew.platform.insiderlog
	 * .InsiderLogCondition, int)
	 */
	public PaginationSupport<InsiderLog> search(InsiderLogCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);

		PaginationSupport<InsiderLog> als = super.findPageByCriteria(criteria,
				new PageInfo(pageNo), Order.desc("eventTime"));

		for (InsiderLog al : als.getItems()) {
			getHibernateTemplate().initialize(al.getInsider());
		}

		return als;
	}

	public DetachedCriteria makeDetachedCriteria(InsiderLogCondition condition) {

		DetachedCriteria dc = DetachedCriteria.forClass(InsiderLog.class);

		if (!StringUtils.isEmpty(condition.getUserId())) {
			dc.add(Restrictions.like("insider.loginId", condition.getUserId(),
					MatchMode.ANYWHERE));
		}
		HibernateTool.eqIfNotEmpty(dc, "insider.id", condition.getActorId());

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

}
