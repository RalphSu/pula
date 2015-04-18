package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.support.utils.DateJedi;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.AttendanceRecordCondition;
import pula.sys.daos.AttendanceRecordDao;
import pula.sys.domains.AttendanceRecord;

@Repository
public class AttendanceRecordDaoImpl extends
		HibernateGenericDao<AttendanceRecord, Long> implements
		AttendanceRecordDao {

	@Override
	public PaginationSupport<AttendanceRecord> search(
			AttendanceRecordCondition condition, int pageIndex) {

		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageIndex),
				Order.desc("checkTime"));
	}

	private DetachedCriteria makeDetachedCriteria(
			AttendanceRecordCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(AttendanceRecord.class,
				"uu");

		dc.createAlias("uu.owner", "empl", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "empl.no", condition.getOwnerNo());
		HibernateTool.eqIfNotEmpty(dc, "uu.no", condition.getNo());

		HibernateTool.eqIfNotZero(dc, "uu.dataFrom", condition.getDataFrom());

		HibernateTool.betweenIfNotNull(dc, "uu.checkTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public AttendanceRecord save(AttendanceRecord cli) {
		cli.setCollectTime(Calendar.getInstance());
		getHibernateTemplate().save(cli);
		return cli;
	}

	@Override
	public Map<Long, List<AttendanceRecord>> map(Calendar date) {

		DateJedi dj = new DateJedi(date);
		String sql = "select e.id , u from AttendanceRecord u left join u.owner as e where u.enabled=? and u.checkTime>=? and u.checkTime<=? ";

		return HibernateTool.asMapList(find(sql, true, dj.yesterday().to(), dj
				.tomorrow().tomorrow().to()));

	}

}
