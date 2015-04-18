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
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.ChargebackCondition;
import pula.sys.daos.ChargebackDao;
import pula.sys.domains.Chargeback;
import pula.sys.domains.OrderForm;
import pula.sys.helpers.OrderFormHelper;

@Repository
public class ChargebackDaoImpl extends HibernateGenericDao<Chargeback, Long>
		implements ChargebackDao {

	@Override
	public boolean hasChargeback(Long orderform_id, long filter_id) {
		String sql = "select id from Chargeback where removed=? and orderForm.id=? and id<>?";
		return exists(sql, false, orderform_id, filter_id);
	}

	@Override
	public Chargeback save(Chargeback cc) {
		cc.setCreatedTime(Calendar.getInstance());
		cc.setUpdatedTime(cc.getCreatedTime());
		cc.setUpdater(cc.getCreator());

		cc.setStatus(Chargeback.STATUS_INPUT);

		_save(cc);
		return cc;
	}

	@Override
	public void checkAllowEdit(long id, long branch_id) {
		String sql = "select u.id from Chargeback u where u.id=? and u.orderForm.branch.id=? and u.status=? and removed=?";
		if (!exists(sql, id, branch_id, Chargeback.STATUS_INPUT, false)) {
			Pe.raise("不允许修改退单;退单状态已确认或越权访问");
		}

	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"createdTime", "backCourses", "status", "backAmount" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "t.name", "teacherName", "t.no",
			"teacherNo", "s.no", "studentNo", "s.name", "studentName",
			"ms.name", "masterName", "ss.name", "slaveName", "cp.name",
			"courseProductName", "cu.name", "creatorName", "of.no",
			"orderFormNo", "of.totalAmount", "totalAmount",
			"of.commissionType", "commissionType" };

	@Override
	public PaginationSupport<MapBean> search(ChargebackCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("uu.id"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(ChargebackCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.orderForm", "of");

		dc.createAlias("of.branch", "b", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("of.student", "s", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("of.teacher", "t", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("of.masterSalesman", "ms", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("of.slaveSalesman", "ss", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("of.courseProduct", "cp", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.creator", "cu", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "of.no", condition.getOrderFormNo());
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
		// HibernateTool.eqIfNotZero(dc, "uu.payStatus",
		// condition.getPayStatus());
		HibernateTool.eqIfNotZero(dc, "of.commissionType",
				condition.getCommissionType());

		HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId());

		String dateField = "createdTime";
		// if (condition.getTarget() == ChargebackCondition.TARGET_MY) {
		// dateField = "createdTime";
		// } else if (condition.getTarget() == ChargebackCondition.TARGET_SENT)
		// {
		// dateField = "auditTime";
		// } else if (condition.getTarget() ==
		// ChargebackCondition.TARGET_RECEIVE) {
		// dateField = "sentTime";
		// }

		HibernateTool.betweenIfNotNull(dc, dateField, condition.getBeginDate(),
				condition.getEndDate(), -1);

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
		String sql = "update Chargeback set status=?,auditTime=?,auditor.id=?,statYear=?,statMonth=? where removed=? and status=? and ";
		QueryJedi qj = new QueryJedi(sql, Chargeback.STATUS_CONFIRM, cal,
				actorId, year, month, false, Chargeback.STATUS_INPUT);
		qj.eqOr("id", WxlSugar.asObjects(id));

		updateBatch(qj.hql(), qj.parameters());

		// log
		qj = new QueryJedi("select no from Chargeback where ").eqOr("id",
				WxlSugar.asObjects(id));
		List<String> no = find(qj.hql(), qj.parameters());
		for (String s : no) {
			loggerDao.doLog("确认订单", s);
		}

	}

	private boolean is_owner_or_status_ok(Long[] id, long branchId) {
		String sql = "select count(u.id) from Chargeback u where u.orderForm.branch.id=? and u.status=? and ";
		QueryJedi qj = new QueryJedi(sql, branchId, Chargeback.STATUS_INPUT);

		qj.eqOr("u.id", WxlSugar.asObjects(id));

		int c = getInt(qj.hql(), qj.parameters());

		if (c != id.length) {
			return false;
		}
		return true;
	}

	@Override
	public void precheckForRemove(Long[] id, long branchId) {
		String sql = "select count(u.id) from Chargeback u where u.orderForm.branch.id=? and u.status=? and ";
		QueryJedi qj = new QueryJedi(sql, branchId, Chargeback.STATUS_INPUT);

		qj.eqOr("u.id", WxlSugar.asObjects(id));

		int c = getInt(qj.hql(), qj.parameters());

		if (c != id.length) {
			Pe.raise("包含不能删除的数据，请刷新后重试");
		}
	}

	@Override
	public void checkAllowView(Long id, long branchId) {

		String sql = "select u.id from Chargeback u where u.id=? and u.orderForm.branch.id=? and removed=? ";
		if (!exists(sql, id, branchId, false)) {
			Pe.raise("越权访问");
		}
	}

	@Override
	public Chargeback update(Chargeback rt) {
		Chargeback po = this.findById(rt.getId());
		po.setBackAmount(rt.getBackAmount());
		po.setBackCourses(rt.getBackCourses());
		po.setComments(rt.getComments());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		_update(po);
		return po;

	}

	@Override
	public Map<String, MapBean> stat4Salesman(int year, int month, long branchId) {
		Calendar begin = DateJedi.create(year, month).firstDayOfMonth()
				.resetToZero().to();
		Calendar end = DateJedi.create(begin).moveMonth(+1).to();

		String sql = "select ms.no as mSalesmanNo,ms.name as mSalesmanName ,ss.no as sSalesmanNo,ss.name as sSalesmanName"
				+ ",of1.commissionType as commissionType ,cb.backAmount as backAmount"
				+ " from Chargeback cb LEFT JOIN cb.orderForm as of1 left join of1.masterSalesman as ms LEFT join of1.slaveSalesman as ss"
				+ " where cb.auditTime>=? and cb.auditTime<? and cb.removed=? and cb.status!=? and of1.canceled=? and of1.branch.id=? "; // 审核时间为准

		MapList ml = mapList(sql, begin, end, false, OrderForm.STATUS_INPUT,
				false, branchId);

		Map<String, MapBean> result = WxlSugar.newHashMap();

		for (MapBean mb : ml) {
			double backAmount = mb.asDouble("backAmount");

			// 主
			String mSmNo = mb.string("mSalesmanNo");
			String mSmName = mb.string("mSalesmanName");

			// 辅
			String sSmNo = mb.string("sSalesmanNo");
			String sSmName = mb.string("sSalesmanName");

			int ct = mb.asInteger("commissionType");

			// 根据ct的类型,决定要放到哪一列

			putToList(result, backAmount, mSmNo, mSmName,
					OrderFormHelper.getKeyByComissionType(ct));
			// 不是单人的,则辅销售有份
			if (ct != OrderForm.COMMISSION_TYPE_100) {
				putToList(result, backAmount, sSmNo, sSmName,
						OrderFormHelper.getKeyByComissionTypeInvs(ct));
			}

		}

		return result;
	}

	private void putToList(Map<String, MapBean> result, double totalAmount,
			String no, String name, String key) {

		key += "_r";

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
		String sql = "select count(cb.id) as totalBackOrders,sum(cb.backAmount) as totalBackAmount ,cb.statYear as statYear ,cb.statMonth as statMonth"
				+ " from Chargeback cb LEFT JOIN cb.orderForm as of1 where "
				+ " cb.statYear=? and cb.removed=? and cb.status!=? and of1.canceled=? and of1.branch.id=?"
				+ " group by cb.statYear,cb.statMonth order by cb.statMonth"; // 审核时间为准

		MapList ml = mapList(sql, year, false, Chargeback.STATUS_INPUT, false,
				branchId);
		return ml;
	}

	@Override
	public Map<String, MapBean> stat4Teacher(int year, int java_month,
			long branchId) {
		String sql = "select count(cb.id) as totalBackOrders,t.id as teacherId"
				+ ",t.no as teacherNo,t.name as teacherName"
				+ " from Chargeback cb LEFT JOIN cb.orderForm as of1 inner join of1.teacher as t where "
				+ " cb.statYear=? and cb.statMonth=? and cb.removed=? and cb.status!=? and of1.canceled=? and of1.branch.id=? group by t.id,t.no,t.name";

		MapList ml = mapList(sql, year, java_month, false,
				Chargeback.STATUS_INPUT, false, branchId);
		Map<String, MapBean> m = WxlSugar.newHashMap();
		for (MapBean mb : ml) {
			String key = mb.string("teacherNo");
			m.put(key, mb);
		}
		return m;
	}
}
