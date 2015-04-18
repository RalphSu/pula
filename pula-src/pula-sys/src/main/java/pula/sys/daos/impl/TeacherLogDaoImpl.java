package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TeacherLogCondition;
import pula.sys.daos.TeacherLogDao;
import pula.sys.domains.Branch;
import pula.sys.domains.Teacher;
import pula.sys.domains.TeacherLog;

@Repository
public class TeacherLogDaoImpl extends HibernateGenericDao<TeacherLog, Long>
		implements TeacherLogDao {

	private static final String[] SINGLE_MAPPING = new String[] { "eventTime",
			"event", "extendInfo", "ip", "id" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "t.name", "teacherName", "t.no",
			"teacherNo", "b.no", "branchNo" };

	@Override
	public PaginationSupport<MapBean> search(TeacherLogCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("uu.eventTime"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(TeacherLogCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.teacher", "t", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "t.id", condition.getTeacherId());
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.likeIfNotEmpty(dc, "t.name", "t.no",
				condition.getUserId());
		HibernateTool.likeIfNotEmpty(dc, "uu.event", condition.getEvent());
		HibernateTool.likeIfNotEmpty(dc, "uu.extendInfo",
				condition.getExtendInfo());
		HibernateTool.betweenIfNotNull(dc, "uu.eventTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public void save(Long teacherId, Long branchId, String ip, String string) {
		TeacherLog tl = new TeacherLog();
		tl.setBranch(Branch.create(branchId));
		tl.setEvent(string);
		tl.setEventTime(Calendar.getInstance());
		tl.setIp(ip);
		tl.setTeacher(Teacher.create(teacherId));
		getHibernateTemplate().save(tl);

	}
}
