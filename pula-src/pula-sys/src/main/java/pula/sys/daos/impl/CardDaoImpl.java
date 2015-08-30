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
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CardCondition;
import pula.sys.daos.CardDao;
import pula.sys.domains.Card;

@Repository
public class CardDaoImpl extends HibernateGenericDao<Card, Long> implements
		CardDao {

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"mac", "enabled", "status", "comments" };
	//
	// private static final String[] ALIAS_MAPPING = new String[] { "b.name",
	// "branchName", "b.id", "branchId" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "mac", "enabled", "status", "comments" };

	@Override
	public PaginationSupport<MapBean> search(CardCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		// proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("uu.no"));
		return MapList.createPage(es);
	}

	private DetachedCriteria makeDetachedCriteria(CardCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		// dc.createAlias("uu.assignments", "ass", DetachedCriteria.LEFT_JOIN,
		// Restrictions.eq("ass.current", true));

		HibernateTool.eqIfNotZero(dc, "status", condition.getStatus());
		HibernateTool.likeIfNotEmpty(dc, "mac", condition.getMac());

		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());
		return dc;
	}

	@Override
	public MapList loadByKeywords(String no) {

		String hql = "select no as no ,no as name,id as id FROM Card WHERE (no LIKE ? ) AND removed=? and enabled=? and status=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return mapListLimit(hql, 40, v, false, true, Card.STATUS_FREE);
	}

	@Override
	public MapBean unique(Long id) {
		CardCondition condition = new CardCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		// proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	@Override
	public MapBean importCards(List<Card> afs) {
		// 还得判断卡号是否重复，还有物理吗重复

		String sql = "select no from Card where (mac=? or no=?) and removed=?";
		for (Card c : afs) {
			String no = findSingle(sql, c.getMac(), c.getNo(), false);
			if (no != null) {
				Pe.raise("指定的卡号或物理号已经存在:" + no);
			}
		}

		// 保存
		for (Card c : afs) {
			c.setCreatedTime(Calendar.getInstance());
			c.setEnabled(true);
			getHibernateTemplate().save(c);
		}

		// logs
		for (Card c : afs) {
			loggerDao.doLog("新增", c);
		}

		return MapBean.map("count", afs.size());

	}

	@Override
	public Long getId4Use(String cardNo, String buildRefId) {
		String sql = "select id from Card where no=? and (refId=? or status=?) and enabled=? and removed=?";
		return findSingle(sql, cardNo, buildRefId, Card.STATUS_FREE, true,
				false);

	}

	@Override
	public void takeBy(String buildRefId, String comments, Long cardId) {
	    // 释放之前绑定的卡
	    String readSql = " update Card set status = 1, refId = '' where refId=? and status=? and enabled=? and removed=? "
	            + " and id != ? ";
	    updateBatch(readSql, buildRefId, Card.STATUS_USED, true, false, cardId);
	    
		String sql = "update Card set refId=? ,comments=?, status=? where id=?";
		updateBatch(sql, buildRefId, comments, Card.STATUS_USED, cardId);
	}
	

}
