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
import pula.sys.conditions.StockEventCondition;
import pula.sys.daos.StockEventDao;
import pula.sys.domains.StockEvent;
import pula.sys.domains.StockLog;
import pula.sys.helpers.StockEventHelper;
import pula.sys.services.SessionUserService;

@Repository
public class StockEventDaoImpl extends HibernateGenericDao<StockEvent, Long>
		implements StockEventDao {

	@Resource
	SessionUserService sessionUserService;

	private static final String[] ALIAS_MAPPING_SEARCH = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.target", "target", "m.unit", "materialUnit", "uu.id", "id" };

	@Override
	public PaginationSupport<MapBean> search(StockEventCondition condition,
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

	private DetachedCriteria makeDetachedCriteria(StockEventCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(StockEvent.class, "uu");

		dc.createAlias("uu.material", "m", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "uu.no", condition.getNo());
		HibernateTool.likeIfNotEmpty(dc, "uu.outNo", condition.getOutNo());

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());

		HibernateTool.betweenIfNotNull(dc, "uu.eventTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		HibernateTool.eqIfNotZero(dc, "uu.target", condition.getTarget());

		if (!HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId())) {
			if (condition.getType() == StockLog.IN) {
				dc.add(Restrictions.gt("target", 0));
			} else {
				dc.add(Restrictions.lt("target", 0));
			}
		}

		HibernateTool.eq(dc, "removed", false);

		return dc;
	}

	@Override
	public StockEvent save(StockEvent cc) {

		// 保存，就两种情况，一种是入库，一个是消费

		cc.setEventTime(Calendar.getInstance());

		_save(cc);

		return cc;

	}

	@Override
	public Mix<StockEvent, StockEvent> update(StockEvent cc) {

		StockEvent po = this.findById(cc.getId());

		// 拿到类型
		int type = StockEventHelper.toType(po.getTarget());

		if (StockEventHelper.manual(cc.getTarget(), type)) {
			// 入库
			Pe.raise("越权访问，不允许修改");
		}

		StockEvent old = po.copy();
		old.setQuantity(old.getQuantity() * -1);

		po.setQuantity(cc.getQuantity());
		po.setMaterial(cc.getMaterial());
		po.setOutNo(cc.getOutNo());
		po.setComments(cc.getComments());

		String event = "新增材料消耗";
		if (cc.getTarget() == StockEvent.TARGET_BUY) {
			event = "新增材料入库";
		}

		loggerDao.doLog(event, String.valueOf(cc.getId()));

		return Mix.create(old, po);

	}

	private static final String[] ALIAS_MAPPING_FULL = new String[] { "m.no",
			"no", "m.name", "name", "uu.quantity", "quantity", "b.name",
			"branchName", "uu.outNo", "outNo", "uu.eventTime", "eventTime",
			"uu.target", "target", "m.unit", "materialUnit", "uu.id", "id" };

	@Override
	public MapBean unique(Long id) {

		StockEventCondition condition = new StockEventCondition();
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
	public List<StockEvent> load(Long[] id) {
		QueryJedi qj = new QueryJedi("select u from StockEvent u where ");
		qj.eqOr("id", WxlSugar.asObjects(id));
		return find(qj.hql(), qj.parameters());
	}
}
