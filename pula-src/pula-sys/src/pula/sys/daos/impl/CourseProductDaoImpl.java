package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseProductCondition;
import pula.sys.daos.CourseProductDao;
import pula.sys.domains.CourseProduct;

@Repository
public class CourseProductDaoImpl extends
		HibernateGenericDao<CourseProduct, Long> implements CourseProductDao {

	@Override
	public CourseProduct save(CourseProduct rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		rt.setCreatedTime(Calendar.getInstance());
		rt.setEnabled(true);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<CourseProduct> search(
			CourseProductCondition condition, int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(
			CourseProductCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);
		dc.createAlias("uu.branch", "b", DetachedCriteria.LEFT_JOIN);
		// HibernateTool.eqIfNotEmpty(dc, "cc.id", condition.getCategoryId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", condition.getKeywords());
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		return dc;
	}

	@Override
	public CourseProduct update(CourseProduct rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		CourseProduct po = this.findById(rt.getId());
		po.setBeginTime(rt.getBeginTime());
		po.setComments(rt.getComments());
		po.setCourseCount(rt.getCourseCount());
		po.setCreator(rt.getCreator());
		po.setEndTime(rt.getEndTime());
		po.setGiftCount(rt.getGiftCount());

		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setPrice(rt.getPrice());
		po.setUpdater(rt.getUpdater());
		po.setBranch(rt.getBranch());
		_update(po);
		return po;

	}

	@Override
	public MapList loadByKeywords(String n, String t, String prefix) {
		String hql = "select no as no , (name +' '+spec) as name FROM CourseProduct "
				+ "WHERE (no LIKE ? or name LIKE ? or spec like ? ) AND removed=?";

		String v = "%" + StringUtils.defaultIfEmpty(n, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, v, false);

		qj.eqIfNotEmpty("courseType.no", t);

		if (!StringUtils.isEmpty(prefix)) {
			qj.append(" and no like ? ", prefix + "%");
		}

		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public List<CourseProduct> listByBranch(long branchId) {
		String sql = "select u from CourseProduct u where u.enabled=? and u.removed=? and u.branch.id=? and"
				+ " ( ( u.beginTime<=? and u.endTime is null ) or"
				+ " ( u.beginTime<=? and u.endTime>=? ) or"
				+ "  ( u.beginTime is null and u.endTime>=?  )) order by no";
		Calendar cal = Calendar.getInstance();
		return find(sql, true, false, branchId, cal, cal, cal, cal);
	}

	@Override
	public MapBean meta4order(long courseProductId, long branch_id) {
		String sql = "select u.price as price,u.beginTime as beginTime,u.endTime as endTime"
				+ ",courseCount as courseCount from CourseProduct u where u.removed=? and u.branch.id=? and u.enabled=? and u.id=? ";
		return mapBean(sql, false, branch_id, true, courseProductId);
	}

}
