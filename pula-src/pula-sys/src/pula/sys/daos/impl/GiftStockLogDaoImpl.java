package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.GiftStockLogCondition;
import pula.sys.daos.GiftStockLogDao;
import pula.sys.domains.Branch;
import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.GiftStockLog;
import pula.sys.domains.GiftTransfer;
import pula.sys.domains.SysUser;
import pula.sys.services.SessionUserService;

@Repository
public class GiftStockLogDaoImpl extends
		HibernateGenericDao<GiftStockLog, Long> implements GiftStockLogDao {
	private static final String[] ALIAS_MAPPING = new String[] {
			"uu.eventTime", "eventTime", "m.no", "no", "uu.type", "type",
			"m.name", "name", "uu.quantity", "quantity", "w.name",
			"warehouseName", "s.name", "spaceName", "si.id", "siid", "so.id",
			"soid", "is.id", "isid", "to.id", "toid", "uu.no", "orderNo",
			"uu.outNo", "outNo" };

	@Resource
	SessionUserService sessionUserService;

	@Override
	public GiftStockLog log(GiftStockLog sl) {

		sl.setCreatedTime(Calendar.getInstance());
		sl.setCreator(SysUser.create(sessionUserService.getActorId()));

		getHibernateTemplate().save(sl);
		return sl;

	}

	private static final String[] ALIAS_MAPPING_SEARCH = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.type", "type", "m.unit", "giftUnit", "uu.id", "id", "se.id",
			"stockEventId", "mr.id", "giftTransferId" };

	@Override
	public PaginationSupport<MapBean> search(GiftStockLogCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		// proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_SEARCH);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.desc("eventTime"),
				Order.desc("id"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(
			GiftStockLogCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(GiftStockLog.class,
				"uu");

		dc.createAlias("uu.gift", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.giftTransfer", "mr", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.giftStockEvent", "se", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "uu.no", condition.getNo());
		HibernateTool.likeIfNotEmpty(dc, "uu.outNo", condition.getOutNo());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		HibernateTool.betweenIfNotNull(dc, "uu.eventTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		HibernateTool.eqIfNotZero(dc, "uu.type", condition.getType());
		HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId());

		return dc;
	}

	@Override
	public MapList export(GiftStockLogCondition condition) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		dc.addOrder(Order.desc("eventTime"));
		dc.addOrder(Order.desc("id"));

		List<Map<String, Object>> es = super.findByCriteria(dc);
		return MapList.create(es);
	}

	private static final String[] ALIAS_MAPPING_FULL = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.type", "type", "m.unit", "giftUnit", "uu.id", "id" };

	@Override
	public MapBean unique(Long id) {

		GiftStockLogCondition condition = new GiftStockLogCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_FULL);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public GiftStockLog save(GiftStockEvent se) {
		GiftStockLog sl = se.toGiftStockLog();
		return log(sl);
	}

	@Override
	public Mix<GiftStockLog, GiftStockLog> save(
			Mix<GiftStockEvent, GiftStockEvent> mix1) {

		// 两个,一个出,一个入,总是相反
		GiftStockLog sl = mix1.object1.toGiftStockLog();
		if (sl.getQuantity() > 0) {
			sl.setType(GiftStockLog.IN);
		} else {
			sl.setType(GiftStockLog.OUT);
			// 绝对值好了
			sl.setQuantity(Math.abs(sl.getQuantity()));
		}

		GiftStockLog l1 = log(sl);
		// 修改的那个
		GiftStockLog l2 = save(mix1.object2);

		// 修改
		return Mix.create(l1, l2);
	}

	@Override
	public void log(List<GiftStockLog> logs) {

		for (GiftStockLog sl : logs) {
			log(sl);
		}

	}

	@Override
	public List<GiftStockLog> saveSend(List<GiftTransfer> mrs, long branchId) {
		List<GiftStockLog> sls = WxlSugar.newArrayList();
		// 根据MR 生成
		Branch b = Branch.create(branchId);
		for (GiftTransfer mr : mrs) {
			GiftStockLog sl = mr.toGiftStockLog(GiftStockLog.OUT);
			sl.setBranch(b);
			sls.add(sl);
			log(sl);
		}
		return sls;
	}

	@Override
	public List<GiftStockLog> saveReceive(Collection<GiftTransfer> mrs,
			long branch_id) {
		List<GiftStockLog> sls = WxlSugar.newArrayList();
		// 根据MR 生成
		Branch b = Branch.create(branch_id);
		for (GiftTransfer mr : mrs) {
			GiftStockLog sl = mr.toGiftStockLog(GiftStockLog.IN);
			sl.setBranch(b);
			sls.add(sl);

			log(sl);
		}
		return sls;
	}

	@Override
	public void save(GiftStockLog gsl) {
		gsl.setCreatedTime(Calendar.getInstance());
		getHibernateTemplate().save(gsl);
	}
}
