package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.GiftStockEventCondition;
import pula.sys.daos.GiftStockEventDao;
import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.GiftStockLog;
import pula.sys.helpers.GiftStockEventHelper;
import pula.sys.services.SessionUserService;

@Repository
public class GiftStockEventDaoImpl extends HibernateGenericDao<GiftStockEvent, Long>
		implements GiftStockEventDao {

	@Resource
	SessionUserService sessionUserService;

	private static final String[] ALIAS_MAPPING_SEARCH = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.target", "target", "m.unit", "giftUnit", "uu.id", "id" };

	@Override
	public PaginationSupport<MapBean> search(GiftStockEventCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		// proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_SEARCH);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("id"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(GiftStockEventCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(GiftStockEvent.class, "uu");

		dc.createAlias("uu.gift", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "uu.no", condition.getNo());
		HibernateTool.likeIfNotEmpty(dc, "uu.outNo", condition.getOutNo());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		HibernateTool.betweenIfNotNull(dc, "uu.eventTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		HibernateTool.eqIfNotZero(dc, "uu.target", condition.getTarget());

		if (!HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId())) {
			if (condition.getType() == GiftStockLog.IN) {
				dc.add(Restrictions.gt("target", 0));
			} else {
				dc.add(Restrictions.lt("target", 0));
			}
		}

		HibernateTool.eq(dc, "removed", false);

		return dc;
	}

	@Override
	public GiftStockEvent save(GiftStockEvent cc) {

		// 保存，就两种情况，一种是入库，一个是消费

		cc.setEventTime(Calendar.getInstance());

		_save(cc);

		return cc;

	}

	@Override
	public Mix<GiftStockEvent, GiftStockEvent> update(GiftStockEvent cc) {

		GiftStockEvent po = this.findById(cc.getId());

		// 拿到类型
		int type = GiftStockEventHelper.toType(po.getTarget());

		if (GiftStockEventHelper.manual(cc.getTarget(), type)) {
			// 入库
			Pe.raise("越权访问，不允许修改");
		}

		GiftStockEvent old = po.copy();
		old.setQuantity(old.getQuantity() * -1);

		po.setQuantity(cc.getQuantity());
		po.setGift(cc.getGift());
		po.setOutNo(cc.getOutNo());
		po.setComments(cc.getComments());

		String event = "新增材料消耗";
		if (cc.getTarget() == GiftStockEvent.TARGET_BUY) {
			event = "新增材料入库";
		}

		loggerDao.doLog(event, String.valueOf(cc.getId()));

		return Mix.create(old, po);

	}

	private static final String[] ALIAS_MAPPING_FULL = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.target", "target", "m.unit", "giftUnit", "uu.id", "id" };

	@Override
	public MapBean unique(Long id) {

		GiftStockEventCondition condition = new GiftStockEventCondition();
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
	public List<GiftStockEvent> load(Long[] id) {
		QueryJedi qj = new QueryJedi("select u from GiftStockEvent u where ");
		qj.eqOr("id", WxlSugar.asObjects(id));
		return find(qj.hql(), qj.parameters());
	}
}
