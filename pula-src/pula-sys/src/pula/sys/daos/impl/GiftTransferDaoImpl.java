package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import pula.sys.conditions.GiftTransferCondition;
import pula.sys.daos.GiftTransferDao;
import pula.sys.domains.GiftTransfer;

@Repository
public class GiftTransferDaoImpl extends
		HibernateGenericDao<GiftTransfer, Long> implements GiftTransferDao {

	@Override
	public GiftTransfer save(GiftTransfer rt) {

		rt.setCreatedTime(Calendar.getInstance());
		rt.setStatus(GiftTransfer.STATUS_INPUT);
		_save(rt);

		return rt;
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id",
			"createdTime", "submitTime", "status", "sentQuantity" };

	private static final String[] SINGLE_MAPPING_RECEIVE = new String[] { "id",
			"createdTime", "submitTime", "status", "sentQuantity", "sentTime" };

	private static final String[] ALIAS_MAPPING = new String[] { "b.name",
			"branchName", "b.id", "branchId", "m.name", "giftName", "m.no",
			"giftNo", "m.unit", "giftUnit", "tb.name", "toBranchName", "tb.id",
			"toBranchId", "tb.no", "toBranchNo" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"createdTime", "submitTime", "status", "comments", "sentTime",
			"receivedTime", "sentQuantity", "arriveQuantity", "outNo" };

	private static final String[] ALIAS_MAPPING_FULL = new String[] { "b.no",
			"branchNo", "b.name", "branchName", "b.id", "branchId", "m.name",
			"giftName", "m.no", "giftNo", "c.name", "creatorName", "s.name",
			"senterName", "r.name", "receiverName", "m.unit", "giftUnit",
			"tb.name", "toBranchName", "tb.id", "toBranchId", "tb.no",
			"toBranchNo" };

	@Override
	public PaginationSupport<MapBean> search(GiftTransferCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		if (condition.getTarget() == GiftTransferCondition.TARGET_RECEIVE) {
			proList = HibernateTool.injectSingle(proList,
					SINGLE_MAPPING_RECEIVE, "uu");
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

	private DetachedCriteria makeDetachedCriteria(
			GiftTransferCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.toBranch", "tb", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.gift", "m", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.eqIfNotZero(dc, "tb.id", condition.getToBranchId());
		HibernateTool.eqIfNotEmpty(dc, "m.no", condition.getGiftNo());
		HibernateTool.eqIfNotZero(dc, "uu.status", condition.getStatus());

		HibernateTool.eqIfNotZero(dc, "uu.id", condition.getId());

		String dateField = "auditTime";
		if (condition.getTarget() == GiftTransferCondition.TARGET_MY) {
			dateField = "createdTime";
		} else if (condition.getTarget() == GiftTransferCondition.TARGET_SENT) {
			dateField = "auditTime";
		} else if (condition.getTarget() == GiftTransferCondition.TARGET_RECEIVE) {
			dateField = "sentTime";
		}

		HibernateTool.betweenIfNotNull(dc, dateField, condition.getBeginDate(),
				condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public GiftTransfer update(GiftTransfer rt) {

		GiftTransfer po = this.findById(rt.getId());

		if (po.getStatus() != GiftTransfer.STATUS_INPUT) {
			Pe.raise("指定的申请不处于输入状态，不可修改");
		}

		if (po.getBranch().getId() != rt.getBranch().getId()) {
			Pe.raise("越权访问");
		}

		po.setBranch(rt.getBranch());
		po.setComments(rt.getComments());
		po.setGift(rt.getGift());
		po.setSentQuantity(rt.getSentQuantity());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		po.setToBranch(rt.getToBranch());
		_update(po);
		return po;

	}

	@Override
	public MapList loadByKeywords(String n, String t, String prefix) {
		String hql = "select no as no , (name +' '+spec) as name FROM GiftTransfer "
				+ "WHERE (no LIKE ? or name LIKE ? or spec like ? ) AND removed=?";

		String v = "%" + StringUtils.defaultIfEmpty(n, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, v, false);

		qj.eqIfNotEmpty("giftTransferType.no", t);

		if (!StringUtils.isEmpty(prefix)) {
			qj.append(" and no like ? ", prefix + "%");
		}

		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public MapList list(long branchId) {
		String sql = "select u.id as id,u.name as name ,u.no as no from"
				+ " GiftTransfer u where u.removed=? and u.branch.id=? order by u.no";
		return mapList(sql, false, branchId);
	}

	@Override
	public MapBean unique(Long id) {

		GiftTransferCondition condition = new GiftTransferCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);

		dc.createAlias("uu.creator", "c", DetachedCriteria.LEFT_JOIN);
		dc.createAlias("uu.senter", "s", DetachedCriteria.LEFT_JOIN);

		dc.createAlias("uu.receiver", "r", DetachedCriteria.LEFT_JOIN);
		// dc.createAlias("uu.auditor", "a", DetachedCriteria.LEFT_JOIN);

		// dc.createAlias("a.office", "o", DetachedCriteria.LEFT_JOIN);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING_FULL);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public void submit(Long[] id, long branchId) {

		// 提交
		QueryJedi qj = new QueryJedi(
				"update GiftTransfer set submitTime=?,status=? where status=? and branch.id=? and ( ",
				Calendar.getInstance(), GiftTransfer.STATUS_SUBMIT,
				GiftTransfer.STATUS_INPUT, branchId);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		updateBatch(qj.hql(), qj.parameters());

		for (Long i : id) {
			loggerDao.doLog("提交材料申请", String.valueOf(i));
		}

	}

	@Override
	public void precheckForRemove(Long[] id, long branchId) {
		QueryJedi qj = new QueryJedi(
				"select id from GiftTransfer where removed=? and status=? and branch.id=? and ( ",
				false, GiftTransfer.STATUS_INPUT, branchId);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		List<Long> ids = find(qj.hql(), qj.parameters());
		if (ids.size() == id.length) {
			return;
		}

		Pe.raise("已提交的单据不能删除");

	}

	@Override
	public void precheckForRejectOrApply(Long[] id) {
		QueryJedi qj = new QueryJedi(
				"select id from GiftTransfer where removed=? and status=? and ( ",
				false, GiftTransfer.STATUS_SUBMIT);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		List<Long> ids = find(qj.hql(), qj.parameters());
		if (ids.size() == id.length) {
			return;
		}

		Pe.raise("包含状态不处于提交的申请");

	}

	@Override
	public void reject(Long[] id, String actorId, String comments) {
		// 提交
		// QueryJedi qj = new QueryJedi(
		// "update GiftTransfer set auditTime=?,status=?,auditor.id=?,auditComments=? where status=? and branch.id=? and ( ",
		// Calendar.getInstance(), GiftTransfer.STATUS_REJECT, actorId,
		// comments, GiftTransfer.STATUS_SUBMIT);
		// qj.eqOr("id", WxlSugar.asObjects(id));
		// qj.append(")");
		//
		// updateBatch(qj.hql(), qj.parameters());
		//
		// for (Long i : id) {
		// loggerDao.doLog("拒绝材料申请", String.valueOf(i));
		// }

	}

	@Override
	public void apply(Long[] id, String actorId, String comments, Integer qty) {
		// QueryJedi qj = new QueryJedi(
		// "update GiftTransfer set auditTime=?,status=?,auditor.id=?,auditComments=?",
		// Calendar.getInstance(), GiftTransfer.STATUS_ACCEPT, actorId,
		// comments);
		//
		// if (qty == null) {
		// qj.append(",sentQuantity=quantity");
		// } else {
		// qj.append(",sentQuantity=?", qty);
		// }
		//
		// qj.append(" where status=? and ( ",
		//
		// GiftTransfer.STATUS_SUBMIT);
		// qj.eqOr("id", WxlSugar.asObjects(id));
		// qj.append(")");
		//
		// updateBatch(qj.hql(), qj.parameters());
		//
		// for (Long i : id) {
		// loggerDao.doLog("受理材料申请", String.valueOf(i));
		// }

	}

	@Override
	public List<GiftTransfer> precheckForSent(Long[] id) {
		QueryJedi qj = new QueryJedi(
				"from GiftTransfer where removed=? and status=? and ( ", false,
				GiftTransfer.STATUS_SUBMIT);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		List<GiftTransfer> ids = find(qj.hql(), qj.parameters());
		if (ids.size() == id.length) {
			return ids;
		}

		return Pe.raise("包含状态不处于已审的申请");

	}

	@Override
	public void send(Long[] id, String actorId, String no) {
		QueryJedi qj = new QueryJedi(
				"update GiftTransfer set sentTime=?,status=?,senter.id=?,outNo=?",
				Calendar.getInstance(), GiftTransfer.STATUS_SENT, actorId, no);

		qj.append(" where status=? and ( ",

		GiftTransfer.STATUS_SUBMIT);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		updateBatch(qj.hql(), qj.parameters());

		for (Long i : id) {
			loggerDao.doLog("发出材料", String.valueOf(i));
		}

	}

	@Override
	public List<GiftTransfer> precheckForReceive(Long[] id, long branchId) {

		QueryJedi qj = new QueryJedi(
				"from GiftTransfer where removed=? and status=? and toBranch.id=? and ( ",
				false, GiftTransfer.STATUS_SENT, branchId);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		List<GiftTransfer> ids = find(qj.hql(), qj.parameters());
		if (ids.size() == id.length) {
			return ids;
		}

		return Pe.raise("申请并非出于发出状态");
	}

	@Override
	public void receive(Long[] id, String actorId, Integer qty, long branchId) {
		// 提交
		QueryJedi qj = new QueryJedi(
				"update GiftTransfer set receivedTime=?,status=?,receiver.id=?",
				Calendar.getInstance(), GiftTransfer.STATUS_DELIVERIED, actorId);

		if (qty == null) {
			qj.append(",arriveQuantity=sentQuantity");
		} else {
			qj.append(",arriveQuantity=?", qty);
		}

		qj.append(" where status=? and toBranch.id=? and ( ",
				GiftTransfer.STATUS_SENT, branchId);
		qj.eqOr("id", WxlSugar.asObjects(id));
		qj.append(")");

		updateBatch(qj.hql(), qj.parameters());

		for (Long i : id) {
			loggerDao.doLog("礼品签收", String.valueOf(i));
		}
	}
}
