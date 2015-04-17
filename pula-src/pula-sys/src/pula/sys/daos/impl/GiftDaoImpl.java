package pula.sys.daos.impl;

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
import pula.sys.conditions.GiftCondition;
import pula.sys.daos.GiftDao;
import pula.sys.domains.Gift;

@Repository
public class GiftDaoImpl extends HibernateGenericDao<Gift, Long> implements
		GiftDao {

	@Override
	public Gift save(Gift rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Gift> search(GiftCondition condition, int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(GiftCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.category", "c", DetachedCriteria.LEFT_JOIN);

		HibernateTool.eqIfNotEmpty(dc, "c.id", condition.getCategoryId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", "raw", "unit", "pinyin",
				"brand", condition.getKeywords());
		HibernateTool.eqIfNotZero(dc, "id", condition.getId());
		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());
		return dc;
	}

	@Override
	public Gift update(Gift rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Gift po = this.findById(rt.getId());
		po.setComments(rt.getComments());
		po.setId(rt.getId());
		po.setCategory(rt.getCategory());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setRaw(rt.getRaw());
		po.setBrand(rt.getBrand());
		po.setSuperficialArea(rt.getSuperficialArea());
		po.setWeight(rt.getWeight());
		po.setPinyin(rt.getPinyin());
		po.setUnit(rt.getUnit());
		po.setPoints(rt.getPoints());
		po.setBeginTime(rt.getBeginTime());
		po.setEndTime(rt.getEndTime());

		_update(po);
		return po;

	}

	@Override
	public List<Gift> loadByKeywords(String no) {
		String hql = "FROM Gift WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true);
	}

	@Override
	public Gift fetchRef(String name, String spec) {

		String hql = "Select u.id from Gift u where u.name=? and u.spec=? and u.removed=? and u.enabled=?";
		return Gift.create((Long) findSingle(hql, name, spec, false, true));
	}

	@Override
	public List<Gift> loadSuggest(String q, int i) {
		String sql = "select u from Gift u where (u.name LIKE ? or u.pinyin LIKE ? or u.no LIKE ? ) and u.removed=? and u.enabled=? ORDER BY u.no ";
		String qs = "%" + q + "%";
		return findLimitByQuery(sql, i, qs, qs, qs, false, true);
	}

	// @Override
	// public Gift fetch(PartImport pi) {
	// String sql = "select u from Gift u where u.no=? and u.removed=?";
	// Gift m = findSingle(sql, pi.getGiftNo(), false);
	// if (m != null) {
	// return m;
	// }
	//
	// m = pi.toGift();
	//
	// getHibernateTemplate().save(m);
	// return m;
	// }

	@Override
	public MapBean getMetaData(Long key) {
		String sql = "select u.id as id ,u.no as giftNo,u.name as giftName,u.graphNo as graphNo,"
				+ "u	.spec as spec from Gift u where u.id=?";

		return mapBean(sql, key);

	}

	@Override
	public MapBean mapBeanByNo(String no) {

		String sql = "select u.id as id ,u.no as giftNo,u.name as giftName,u.brand as brand,"
				+ "u.unit as unitl from Gift u where u.no=? and removed=?";

		return mapBean(sql, no, false);
	}

	@Override
	public void saveOrUpdate(Gift gift) {
		Long id = getIdByNo(gift.getNo());

		if (id == null) {
			getHibernateTemplate().save(gift);
		} else {
			gift.setId(id);
			getHibernateTemplate().update(gift);
		}

	}

	@Override
	public MapBean unique(Long id) {
		GiftCondition condition = new GiftCondition();
		condition.setId(id);

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool
				.injectSingle(proList, SINGLE_MAPPING_FULL, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		Map<String, Object> map = super.uniqueResult(dc);

		return MapBean.map(map);
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id", "no",
			"name", "pinyin", "raw", "brand", "unit", "points", "beginTime",
			"endTime", "enabled" };

	private static final String[] ALIAS_MAPPING = new String[] { "c.name",
			"categoryName", "c.id", "categoryId" };

	// private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
	// "no", "name", "birthday", "status", "gender", "hjAddress" };

	private static final String[] SINGLE_MAPPING_FULL = new String[] { "id",
			"no", "name", "pinyin", "raw", "brand", "unit", "comments",
			"points", "beginTime", "endTime" };

	@Override
	public PaginationSupport<MapBean> searchMapBean(GiftCondition condition,
			int pageIndex) {
		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(pageIndex), Order.asc("uu.no"));
		return MapList.createPage(es);
	}

	@Override
	public MapBean meta4exchange(String giftNo) {
		String sql = "select u.id as id ,u.no as no ,u.name as name ,u.points as points,u.unit as unit from"
				+ " Gift u where u.removed=? and u.no=? and u.enabled=?";
		return mapBean(sql, false, giftNo, true);

	}

	// @Override
	// public Gift saveStandard(PartForm af) {
	// Gift m = new Gift();
	// m.setName(af.getName());
	// m.setNo(String.valueOf(Calendar.getInstance().getTimeInMillis())
	// + RandomTool.getRandomString(5));
	// m.setRaw(af.getRaw());
	// m.setSpec(af.getSpec());
	//
	// return this.save(m);
	// }

}
