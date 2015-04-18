package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TeacherAssignmentCondition;
import pula.sys.daos.TeacherAssignmentDao;
import pula.sys.domains.Branch;
import pula.sys.domains.SysUser;
import pula.sys.domains.Teacher;
import pula.sys.domains.TeacherAssignment;

@Repository
public class TeacherAssignmentDaoImpl extends
		HibernateGenericDao<TeacherAssignment, Long> implements
		TeacherAssignmentDao {

	@Override
	public boolean isCurrent(long id, long branchId) {
		String sql = "select id from TeacherAssignment where teacher.id=? and branch.id=? and current=?";
		return exists(sql, id, branchId,true);
	}

	@Override
	public void save(long id, long branchId, String actorId) {

		// update old
		String sql = "update TeacherAssignment set current=?,endTime=? where teacher.id=? and current=?";
		Calendar now = Calendar.getInstance();
		updateBatch(sql, false, now, id, true);

		// save
		TeacherAssignment ta = new TeacherAssignment();
		ta.setBranch(Branch.create(branchId));
		ta.setCreatedTime(now);
		ta.setCurrent(true);
		ta.setTeacher(Teacher.create(id));
		ta.setAssigner(SysUser.create(actorId));

		getHibernateTemplate().save(ta);

	}

	@Override
	public void checkAllowUse(List<Long> teacher_ids, long branchId,
			Calendar trainDate) {

		//
		WxlSugar.removeDuplicate(teacher_ids);

		// count
		String sql = "select count(u.id) from TeacherAssignment u where u.branch.id=?"
				+ " and u.createdTime<=? and (u.endTime>? or u.current=?) and ";
		QueryJedi qj = new QueryJedi(sql, branchId, trainDate, trainDate, true);
		qj.eqOr("u.teacher.id", teacher_ids.toArray());

		int c = getInt(qj.hql(), qj.parameters());

		if (c != teacher_ids.size()) {
			Pe.raise("包含当前分校不允许使用的教师数据");
		}

	}

	private static final String[] SINGLE_MAPPING = new String[] {
			"createdTime", "id", "current" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "t.no", "teacherNo", "t.name",
			"teacherName","a.name","assignerName" };

	@Override
	public PaginationSupport<MapBean> search(
			TeacherAssignmentCondition condition, int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("uu.createdTime"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(
			TeacherAssignmentCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(
				TeacherAssignment.class, "uu");
		dc.createAlias("uu.teacher", "t");
		dc.createAlias("uu.branch", "b");
		dc.createAlias("uu.assigner", "a", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "t.id", condition.getTeacherId());

		return dc;

	}

}
