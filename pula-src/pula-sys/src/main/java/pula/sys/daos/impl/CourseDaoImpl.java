package pula.sys.daos.impl;

import java.util.Calendar;

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
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseCondition;
import pula.sys.daos.CourseDao;
import pula.sys.domains.Course;

@Repository
public class CourseDaoImpl extends HibernateGenericDao<Course, Long> implements
		CourseDao {

	@Override
	public Course save(Course rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		rt.setCreatedTime(Calendar.getInstance());
		rt.setEnabled(true);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Course> search(CourseCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(CourseCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);
		dc.createAlias("uu.category", "cc");
		HibernateTool.eqIfNotEmpty(dc, "cc.id", condition.getCategoryId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", condition.getKeywords());
		HibernateTool.eqIfHas(dc, "enabled", condition.getEnabledStatus());
		return dc;
	}

	@Override
	public Course update(Course rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Course po = this.findById(rt.getId());
		po.setComments(rt.getComments());

		po.setCategory(rt.getCategory());
		po.setSubCategory(rt.getSubCategory());
		po.setExpiredTime(rt.getExpiredTime());
		po.setIndexNo(rt.getIndexNo());
		po.setName(rt.getName());
		po.setPublishTime(rt.getPublishTime());
		po.setShowInWeb(rt.isShowInWeb());
		po.setUpdater(rt.getUpdater());
		po.setUpdatedTime(Calendar.getInstance());
		po.setKey(rt.getKey());
		po.setMinutes(rt.getMinutes());

		_update(po);
		return po;

	}

	@Override
	public MapList loadByKeywords(String n, String t, String prefix) {
		String hql = "select no as no , (name +' '+spec) as name FROM Course "
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
	public MapList list(String categoryId) {
		String sql = "select u.id as id,u.name as name ,u.no as no from Course u where u.removed=? and u.category.id=? order by u.no";
		return mapList(sql, false, categoryId);
	}

	@Override
	public MapList mapList4web(String type_no) {
		String sql = "select u.id as id,u.name as name ,u.no as no,u.comments as comments"
				+ " from Course u where u.removed=? and u.showInWeb=? and u.category.no=? order by u.no";
		return mapList(sql, false, true, type_no);
	}

}
