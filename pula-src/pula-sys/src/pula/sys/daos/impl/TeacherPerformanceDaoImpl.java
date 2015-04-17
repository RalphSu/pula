package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Collection;
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
import puerta.support.dao.QueryJedi;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TeacherPerformanceCondition;
import pula.sys.daos.TeacherPerformanceDao;
import pula.sys.domains.SysUser;
import pula.sys.domains.TeacherPerformance;

@Repository
public class TeacherPerformanceDaoImpl extends
		HibernateGenericDao<TeacherPerformance, Long> implements
		TeacherPerformanceDao {

	@Override
	public Map<String, MapBean> map(int year, int java_month, long branchId) {
		String sql = "select t.no as teacherNo,t.name as teacherName ,t.id as teacherId ,u.courseCount as courseCount,u.workdays as workdays,"
				+ "u.factWorkDays as factWorkDays,u.later as later,u.earlier as earlier,u.leave as leave,"
				+ "u.complex as complex,u.performance as performance,u.orders as orders,u.chargebacks as chargebacks"
				+ " from TeacherPerformance u LEFT JOIN u.teacher as t where u.removed=? and u.statYear=?"
				+ " and u.statMonth=? and u.branch.id=?";

		MapList mapList = mapList(sql, false, year, java_month, branchId);

		Map<String, MapBean> m = WxlSugar.newHashMap();
		for (MapBean mb : mapList) {
			String key = mb.string("teacherNo");
			m.put(key, mb);
		}

		return m;

	}

	@Override
	public Map<Long, Long> getExists(int year, int java_month, long branchId) {
		String sql = "select id,id from TeacherPerformance where statYear=? and statMonth=? and branch.id=? and removed=?";
		Map<Long, Long> map = HibernateTool.asMap(find(sql, year, java_month,
				branchId, false));

		return map;

	}

	@Override
	public void update(TeacherPerformance rt, String actorId, boolean update) {
		if (update) {
			TeacherPerformance po = this.findById(rt.getId());

			po.setBranch(rt.getBranch());
			po.setChargebacks(rt.getChargebacks());
			po.setComplex(rt.getComplex());
			po.setCourseCount(rt.getCourseCount());
			po.setCreator(rt.getCreator());
			po.setEarlier(rt.getEarlier());
			po.setFactWorkDays(rt.getFactWorkDays());
			po.setId(rt.getId());
			po.setLater(rt.getLater());
			po.setLeave(rt.getLeave());
			po.setOrders(rt.getOrders());
			po.setPerformance(rt.getPerformance());
			po.setTeacher(rt.getTeacher());
			po.setUpdater(SysUser.create(actorId));
			po.setUpdatedTime(Calendar.getInstance());
			po.setWorkdays(rt.getWorkdays());
			_update(po);
		} else {
			rt.setCreator(SysUser.create(actorId));
			rt.setCreatedTime(Calendar.getInstance());
			rt.setUpdatedTime(rt.getCreatedTime());
			rt.setUpdater(rt.getCreator());

			_save(rt);
		}

	}

	@Override
	public void remove(Collection<Long> values) {
		QueryJedi qj = new QueryJedi(
				"update TeacherPerformance set removed=? where ", true).eqOr(
				"id", values.toArray());

		updateBatch(qj.hql(), qj.parameters());

	}

	private static final String[] SINGLE_MAPPING = new String[] {
			"createdTime", "id", "courseCount", "workdays", "factWorkDays",
			"later", "earlier", "leave", "complex", "performance", "orders",
			"chargebacks", "statYear", "statMonth" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "t.no", "teacherNo", "t.name",
			"teacherName" };

	@Override
	public PaginationSupport<MapBean> search(
			TeacherPerformanceCondition condition, int pageIndex) {
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
			TeacherPerformanceCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(
				TeacherPerformance.class, "uu");
		dc.createAlias("uu.teacher", "t");
		dc.createAlias("uu.branch", "b");
		// dc.createAlias("uu.assigner", "a", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "t.id", condition.getTeacherId());
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		HibernateTool.eq(dc, "removed", false);
		return dc;

	}

}
