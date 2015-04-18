package pula.sys.daos.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.Mix;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StudentPointsCondition;
import pula.sys.daos.StudentPointsDao;
import pula.sys.domains.StudentPoints;
import pula.sys.vo.ExchangePointsVo;
import pula.sys.vo.PointsRowVo;

@Repository
public class StudentPointsDaoImpl extends
		HibernateGenericDao<StudentPoints, String> implements StudentPointsDao {

	@Override
	public StudentPoints save(StudentPoints p) {
		p.setCreatedTime(Calendar.getInstance());
		getHibernateTemplate().save(p);
		return p;
	}

	@Override
	public int getSummary(Long ownerId, int flag) {
		String hql = "select SUM(points) FROM StudentPoints where owner.id=? ";

		QueryJedi qp = new QueryJedi(hql, ownerId);

		if (flag > 0) {
			qp.append(" and points>0");
		} else if (flag < 0) {
			qp.append(" and points<0");
		}

		Long n = findSingle(qp.hql(), qp.parameters());
		if (n == null) {
			return 0;
		}
		return n.intValue();

	}

	@Override
	public PaginationSupport<StudentPoints> search(
			StudentPointsCondition condition, int p) {
		DetachedCriteria dc = makeDetachedCriteria(condition);

		PaginationSupport<StudentPoints> of = super.findPageByCriteria(dc,
				new PageInfo(p, 40), Order.desc("createdTime"));

		return of;
	}

	private DetachedCriteria makeDetachedCriteria(
			StudentPointsCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(StudentPoints.class,
				"uu");
		dc.createAlias("owner", "owner");
		// dc.createAlias("owner.shop", "s");

		if (!StringUtils.isEmpty(condition.getLoginId())) {
			dc.add(Restrictions.eq("owner.no",
					StringUtils.trim(condition.getLoginId())));
		}
		HibernateTool.eqIfNotZero(dc, "fromType", condition.getType());

		HibernateTool.eqIfNotZero(dc, "owner.id", condition.getStudentId());
		// HibernateTool.eqIfNotEmpty(dc, "s.agent.id", condition.getAgentId());
		// HibernateTool.eqIfNotEmpty(dc, "s.id", condition.getShopId());

		if (HibernateTool.eqIfNotEmpty(dc, "agent.no", condition.getAgentNo())) {
			dc.createAlias("s.agent", "agent");
		}
		HibernateTool.eqIfNotEmpty(dc, "s.id", condition.getShopId());
		HibernateTool.eqIfNotEmpty(dc, "s.no", condition.getShopNo());
		HibernateTool.likeIfNotEmpty(dc, "comments", condition.getKeywords());

		// dc.add(Restrictions.eq("s.removed", false));
		HibernateTool.betweenIfNotNull(dc, "createdTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		return dc;
	}

	@Override
	public boolean hasPoints(String targetId, String ownerId) {
		String hql = "select id from StudentPoints where targetId=? AND owner.id=?";
		return findSingle(hql, targetId, ownerId) != null;
	}

	@Override
	public List<StudentPoints> getPoints(long actorId) {
		String hql = "select u FROM StudentPoints u where u.owner.id=? order by createdTime desc";
		return findLimitByQuery(hql, 20, actorId);
	}

	@Override
	public int getTotalPoints(long id) {
		String hql = "select SUM(points) FROM StudentPoints where owner.id=? and points>0";
		Long n = findSingle(hql, id);
		if (n == null) {
			return 0;
		}
		return n.intValue();
	}

	@Override
	public Map<String, Object> summary(StudentPointsCondition c) {
		DetachedCriteria criteria = makeDetachedCriteria(c);

		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.sum("points").as("points"));

		criteria.setProjection(pl);
		criteria.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		return super.uniqueResult(criteria);
	}

	@Override
	public Mix<List<ExchangePointsVo>, ExchangePointsVo> list(
			StudentPointsCondition c) {
		c.setType(StudentPoints.FROM_EXCHANGE);
		DetachedCriteria criteria = makeDetachedCriteria(c);

		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.count("points").as("points"));
		pl.add(Projections.groupProperty("minorType").as("minorType"));
		pl.add(Projections.groupProperty("s.no").as("no"));
		pl.add(Projections.max("s.name").as("sName"));
		pl.add(Projections.max("s.no").as("sNo"));

		criteria.setProjection(pl);
		criteria.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		criteria.addOrder(Order.asc("s.no"));
		List<Map<String, Object>> result = findByCriteria(criteria);

		Map<String, ExchangePointsVo> r = WxlSugar.newLinkedHashMap();
		ExchangePointsVo sum = new ExchangePointsVo();
		for (Map<String, Object> m : result) {
			String no = (String) m.get("sNo");
			String name = (String) m.get("sName");
			// String id = (String) m.get("id");
			int mt = (Integer) m.get("minorType");
			long pts = (Long) m.get("points");

			ExchangePointsVo v = null;

			if (r.containsKey(no)) {
				v = r.get(no);
			} else {
				v = new ExchangePointsVo();

				// v.setShopId(id);
				v.setShopNo(no);
				v.setShopName(name);
				r.put(no, v);
			}
			v.putCounter(mt, pts);
			sum.sumCounter(mt, pts);
		}

		List<ExchangePointsVo> list = new ArrayList<ExchangePointsVo>(
				r.values());
		return Mix.create(list, sum);

	}

	@Override
	public PointsRowVo getPointsRow(long id) {
		PointsRowVo p = new PointsRowVo();
		p.setCurrent(this.getSummary(id, 0));
		p.setAllGot(this.getSummary(id, 1));
		p.setAllUsed(this.getSummary(id, -1));
		return p;
	}

	@Override
	public StudentPoints findByRefId(String buildRefId) {
		String sql = "select u from StudentPoints u where u.refId=?";
		return findSingle(sql, buildRefId);
	}

	private static final String[] SINGLE_MAPPING = new String[] { "id",
			"createdTime", "points", "comments", "fromType" };

	//
	// private static final String[] ALIAS_MAPPING = new String[] { "b.name",
	// "branchName", "b.id", "branchId" };

	@Override
	public PaginationSupport<MapBean> search4front(Long actorId, int p) {

		StudentPointsCondition condition = new StudentPointsCondition();
		condition.setStudentId(actorId);
		// DetachedCriteria dc = makeDetachedCriteria(condition);
		//
		// PaginationSupport<MapBean> of = super.findPageByCriteria(dc,
		// new PageInfo(p, 40), Order.desc("createdTime"));

		DetachedCriteria dc = makeDetachedCriteria(condition);
		ProjectionList proList = Projections.projectionList();// 设置投影集合

		proList = HibernateTool.injectSingle(proList, SINGLE_MAPPING, "uu");
		// proList = HibernateTool.injectAlias(proList, ALIAS_MAPPING);
		dc.setProjection(proList);
		dc.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		PaginationSupport<Map<String, Object>> es = super.findPageByCriteria(
				dc, new PageInfo(p,5), Order.desc("createdTime"));
		return MapList.createPage(es);

	}
}
