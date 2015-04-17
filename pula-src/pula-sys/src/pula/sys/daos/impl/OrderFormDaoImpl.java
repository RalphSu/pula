package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.Arith;
import puerta.support.utils.DateJedi;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.OrderFormCondition;
import pula.sys.daos.OrderFormDao;
import pula.sys.domains.Chargeback;
import pula.sys.domains.OrderForm;
import pula.sys.domains.StudentPoints;
import pula.sys.helpers.OrderFormHelper;

@Repository
public class OrderFormDaoImpl extends HibernateGenericDao<OrderForm, Long>
		implements OrderFormDao {

	@Override
	public OrderForm save(OrderForm cc) {
		cc.setCreatedTime(Calendar.getInstance());
		cc.setUpdatedTime(cc.getCreatedTime());
		cc.setUpdater(cc.getCreator());

		cc.setStatus(OrderForm.STATUS_INPUT);

		_save(cc);
		return cc;
	}

	@Override
	public void checkAllowEdit(long id, long branchId) {
		String sql = "select u.id from OrderForm u where u.id=? and u.branch.id=? and u.status=? and removed=?";
		if (!exists(sql, id, branchId, OrderForm.STATUS_INPUT, false)) {
			Pe.raise("不允许修改订单;订单状态已确认或越权访问");
		}

	}

	@Override
	public Mix<OrderForm, StudentPoints> update(OrderForm rt, boolean hq) {
		OrderForm po = this.findById(rt.getId());
		if (!hq && po.getStatus() != OrderForm.STATUS_INPUT) {
			Pe.raise("指定的申请不处于输入状态，不可修改");
		}

		StudentPoints sp = StudentPoints.create(po.getStudent(),
				StudentPoints.FROM_ORDER_FORM, po.getPoints() * -1, "订单修改回滚:"
						+ po.getNo(), OrderFormHelper.buildRefId(po.getId()));

		po.setComments(rt.getComments());
		po.setCommissionType(rt.getCommissionType());
		po.setCourseProduct(rt.getCourseProduct());
		po.setMasterSalesman(rt.getMasterSalesman());
		po.setPayStatus(rt.getPayStatus());
		po.setPoints(rt.getPoints());
		po.setPrepay(rt.getPrepay());
		po.setSlaveSalesman(rt.getSlaveSalesman());
		po.setStudent(rt.getStudent());
		po.setTeacher(rt.getTeacher());
		po.setTotalAmount(rt.getTotalAmount());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		po.setCourseCount(rt.getCourseCount());

		_update(po);
		return Mix.create(po, sp);

	}

	@Override
	public void checkAllowView(Long id, long branchId) {
		String sql = "select u.id from OrderForm u where u.id=? and u.branch.id=? and removed=? ";
		if (!exists(sql, id, branchId, false)) {
			Pe.raise("越权访问");
		}

	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"createdTime", "payStatus", "status", "totalAmount", "prepay",
			"commissionType", "points" };

	private static final String[] SINGLE_MAPPING_4STUDENTS = new String[] {
			"id", "no", "createdTime", "courseCount", "consumeCourseCount" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "t.name", "teacherName", "t.no",
			"teacherNo", "s.no", "studentNo", "s.name", "studentName",
			"ms.name", "masterName", "ss.name", "slaveName", "cp.name",
			"courseProductName", "cu.name", "creatorName" };

	@Override
	public PaginationSupport<MapBean> search(OrderFormCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		if (condition.isForStudents()) {
			proList = HibernateTool.injectSingle(proList,
					SINGLE_MAPPING_4STUDENTS, "uu");
		} else {
			proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		}
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("uu.id"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(OrderFormCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.student", "s", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.teacher", "t", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.masterSalesman", "ms", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.slaveSalesman", "ss", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.courseProduct", "cp", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.creator", "cu", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "cp.id", condition.getCourseProductId());
		HibernateTool.eqIfNotEmpty(dc, "s.no", condition.getStudentNo());
		HibernateTool.eqIfNotEmpty(dc, "t.no", condition.getTeacherNo());
		if (!StringUtils.isEmpty(condition.getSalesmanNo())) {
			dc.add(HibernateTool.or(
					Restrictions.eq("ms.no", condition.getSalesmanNo()),
					Restrictions.eq("ss.no", condition.getSalesmanNo())));

		}
		HibernateTool.eqIfNotZero(dc, "uu.status", condition.getStatus());
		HibernateTool.eqIfNotZero(dc, "uu.payStatus", condition.getPayStatus());
		HibernateTool.eqIfNotZero(dc, "uu.commissionType",
				condition.getCommissionType());

		HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId());

		String dateField = "createdTime";
		// if (condition.getTarget() == OrderFormCondition.TARGET_MY) {
		// dateField = "createdTime";
		// } else if (condition.getTarget() == OrderFormCondition.TARGET_SENT) {
		// dateField = "auditTime";
		// } else if (condition.getTarget() ==
		// OrderFormCondition.TARGET_RECEIVE) {
		// dateField = "sentTime";
		// }

		HibernateTool.betweenIfNotNull(dc, dateField, condition.getBeginDate(),
				condition.getEndDate(), -1);

		if (condition.isForChargeback()) {
			// sub
			DetachedCriteria sub_dc = DetachedCriteria.forClass(
					Chargeback.class, "cb");

			sub_dc.add(Restrictions.eqProperty("cb.orderForm.id", "uu.id"));
			sub_dc.add(Restrictions.eq("removed", false));
			sub_dc.setProjection(Projections.id());

			// 不存在退单
			dc.add(Subqueries.notExists(sub_dc));
		}
		if (condition.isForStudents()) {
			dc.add(HibernateTool.or(
					Restrictions.eq("uu.status", OrderForm.STATUS_COMPLETED),
					Restrictions.eq("uu.status", OrderForm.STATUS_CONFIRM)));
		}

		return dc;
	}

	@Override
	public void confirm(Long[] id, long branchId, String actorId) {
		// 预检

		if (!is_owner_or_status_ok(id, branchId)) {
			Pe.raise("包含不能确认的数据，请刷新后重试");
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		String sql = "update OrderForm set status=?,auditTime=?,auditor.id=?,statYear=?,statMonth=? where removed=? and status=? and ";
		QueryJedi qj = new QueryJedi(sql, OrderForm.STATUS_CONFIRM, cal,
				actorId, year, month, false, OrderForm.STATUS_INPUT);
		qj.eqOr("id", WxlSugar.asObjects(id));

		updateBatch(qj.hql(), qj.parameters());

		// log
		qj = new QueryJedi("select no from OrderForm where ").eqOr("id",
				WxlSugar.asObjects(id));
		List<String> no = find(qj.hql(), qj.parameters());
		for (String s : no) {
			loggerDao.doLog("确认订单", s);
		}

	}

	private boolean is_owner_or_status_ok(Long[] id, long branchId) {
		String sql = "select count(u.id) from OrderForm u where u.branch.id=? and u.status=? and u.payStatus=? and ";
		QueryJedi qj = new QueryJedi(sql, branchId, OrderForm.STATUS_INPUT,
				OrderForm.PAY_STATUS_PAID);

		qj.eqOr("u.id", WxlSugar.asObjects(id));

		int c = getInt(qj.hql(), qj.parameters());

		if (c != id.length) {
			return false;
		}
		return true;
	}

	@Override
	public MapList precheckForRemove(Long[] id, long branchId) {

		String sql = "select id as id, points as points,no as no,u.student.id as studentId from OrderForm u where u.branch.id=? and u.status=? and u.removed=? and ";
		QueryJedi qj = new QueryJedi(sql, branchId, OrderForm.STATUS_INPUT,
				false);

		qj.eqOr("u.id", WxlSugar.asObjects(id));

		MapList objs = mapList(qj.hql(), qj.parameters());

		if (objs.size() != id.length) {
			Pe.raise("包含不能删除的数据，请刷新后重试");
		}
		return objs;

	}

	@Override
	public String getOpenOrderForm(Long student, long id) {
		String sql = "select no from OrderForm where removed=? and student.id=? and canceled=? and (status=? or status=?)";
		QueryJedi qj = new QueryJedi(sql, false, student, false,
				OrderForm.STATUS_INPUT, OrderForm.STATUS_CONFIRM);
		if (id != 0) {
			qj.append(" and id<>?", id);
		}

		return findSingle(qj.hql(), qj.parameters());

	}

	@Override
	public void checkAllowChargeback(Long id, long branch_id) {
		String sql = "select u.id from OrderForm u where u.id=? and u.removed=? and u.status=? and u.branch.id=?";
		if (!exists(sql, id, false, OrderForm.STATUS_CONFIRM, branch_id)) {
			Pe.raise("指定的订单不允许退单;可能是状态未确认或已完成;可能是越权访问");
		}

	}

	@Override
	public Map<String, MapBean> stat4Salesman(int year, int month, long branchId) {

		Calendar begin = DateJedi.create(year, month).firstDayOfMonth()
				.resetToZero().to();
		Calendar end = DateJedi.create(begin).moveMonth(+1).to();

		String sql = "select of1.no as orderFormNo, ms.no as mSalesmanNo,ms.name as mSalesmanName ,ss.no as sSalesmanNo,ss.name as sSalesmanName"
				+ ",of1.commissionType as commissionType ,of1.totalAmount as totalAmount"
				+ " from OrderForm of1 left join of1.masterSalesman as ms LEFT join of1.slaveSalesman as ss"
				+ " where of1.auditTime>=? and of1.auditTime<? and of1.removed=? and of1.status!=? and of1.canceled=? and of1.branch.id=? "; // 审核时间为准

		MapList ml = mapList(sql, begin, end, false, OrderForm.STATUS_INPUT,
				false, branchId);

		Map<String, MapBean> result = WxlSugar.newHashMap();

		for (MapBean mb : ml) {
			double totalAmount = mb.asDouble("totalAmount");

			// 主
			String mSmNo = mb.string("mSalesmanNo");
			String mSmName = mb.string("mSalesmanName");

			// 辅
			String sSmNo = mb.string("sSalesmanNo");
			String sSmName = mb.string("sSalesmanName");

			int ct = mb.asInteger("commissionType");

			// 根据ct的类型,决定要放到哪一列

			putToList(result, totalAmount, mSmNo, mSmName,
					OrderFormHelper.getKeyByComissionType(ct));
			// 不是单人的,则辅销售有份
			if (ct != OrderForm.COMMISSION_TYPE_100) {
				putToList(result, totalAmount, sSmNo, sSmName,
						OrderFormHelper.getKeyByComissionTypeInvs(ct));
			}

		}

		return result;

	}

	private void putToList(Map<String, MapBean> result, double totalAmount,
			String no, String name, String key) {

		String key_amount = key + "_amount";

		// put twice

		MapBean r = null;
		if (result.containsKey(no)) {
			r = result.get(no);
		} else {
			r = new MapBean();
			r.add("salesmanName", name).add("salesmanNo", no);
			result.put(no, r);
		}
		int orders = r.asInteger(key);
		double amount = r.asDouble(key_amount);

		r.put(key, orders + 1);
		r.put(key_amount, Arith.add(amount, totalAmount));

	}

	@Override
	public MapList stat4Monthly(int year, long branchId) {

		String sql = "select count(of1.id) as totalOrders,sum(of1.totalAmount) as totalAmount ,of1.statYear as statYear ,of1.statMonth as statMonth"

				+ " from OrderForm of1 where of1.statYear=? and of1.removed=? and of1.status!=? and of1.canceled=? and of1.branch.id=?"
				+ " group by of1.statYear,of1.statMonth order by of1.statMonth"; // 审核时间为准

		MapList ml = mapList(sql, year, false, OrderForm.STATUS_INPUT, false,
				branchId);

		return ml;
	}

	@Override
	public Map<String, MapBean> stat4Teacher(int year, int java_month,
			long branchId) {
		String sql = "select count(of1.id) as totalOrders,t.id as teacherId,t.no as teacherNo,t.name as teacherName"

				+ " from OrderForm of1 inner join of1.teacher as t where of1.statYear=? and of1.statMonth=?"
				+ " and of1.removed=? and of1.status!=? and of1.canceled=? and of1.branch.id=? group by t.id,t.no,t.name";

		MapList ml = mapList(sql, year, java_month, false,
				OrderForm.STATUS_INPUT, false, branchId);

		Map<String, MapBean> m = WxlSugar.newHashMap();
		for (MapBean mb : ml) {
			String key = mb.string("teacherNo");
			m.put(key, mb);
		}
		return m;
	}

	@Override
	public void consumeRestore(Long id) {
		String sql = "update OrderForm set consumeCourseCount=consumeCourseCount-1,status=? where id=?";
		updateBatch(sql, OrderForm.STATUS_CONFIRM, id);
	}

	@Override
	public MapBean meta4consumeRestore(Long studentId) {
		String sql = "select id as id from OrderForm where (status=? or (consumeCourseCount>=1 and status=?)) and student.id=? order by status ";

		return mapBean(sql, OrderForm.STATUS_COMPLETED,
				OrderForm.STATUS_CONFIRM, studentId);
	}

	@Override
	public MapBean meta4consume(long studentId) {
		String sql = "select id as id ,courseCount as courseCount,consumeCourseCount as consumeCourseCount from OrderForm"
				+ " where student.id=? and status=?";
		return mapBean(sql, studentId, OrderForm.STATUS_CONFIRM);
	}

	@Override
	public void complete(Long id) {
		String sql = "update OrderForm set status=? where id=?";
		updateBatch(sql, OrderForm.STATUS_COMPLETED, id);

	}

	@Override
	public void consume(Long id) {
		String sql = "update OrderForm set consumeCourseCount=consumeCourseCount+1 where id=?";
		updateBatch(sql, id);

	}
}
