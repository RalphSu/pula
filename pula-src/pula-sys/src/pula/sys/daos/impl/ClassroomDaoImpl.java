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
import pula.sys.conditions.ClassroomCondition;
import pula.sys.daos.ClassroomDao;
import pula.sys.domains.Classroom;

@Repository
public class ClassroomDaoImpl extends HibernateGenericDao<Classroom, Long>
		implements ClassroomDao {

	@Override
	public Classroom save(Classroom rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		rt.setCreatedTime(Calendar.getInstance());

		rt.setEnabled(true);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Classroom> search(ClassroomCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(ClassroomCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);
		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", condition.getKeywords());
		return dc;
	}

	@Override
	public Classroom update(Classroom rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Classroom po = this.findById(rt.getId());
		po.setId(rt.getId());
		po.setBranch(rt.getBranch());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setUpdater(rt.getUpdater());

		po.setUpdatedTime(Calendar.getInstance());

		_update(po);
		return po;

	}

	@Override
	public MapList loadByKeywords(String n, String t, String prefix) {
		String hql = "select no as no , (name +' '+spec) as name FROM Classroom "
				+ "WHERE (no LIKE ? or name LIKE ? or spec like ? ) AND removed=?";

		String v = "%" + StringUtils.defaultIfEmpty(n, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, v, false);

		qj.eqIfNotEmpty("classroomType.no", t);

		if (!StringUtils.isEmpty(prefix)) {
			qj.append(" and no like ? ", prefix + "%");
		}

		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

	@Override
	public MapList list(long branchId) {
		String sql = "select u.id as id,u.name as name ,u.no as no from Classroom u where u.removed=? and u.branch.id=? order by u.no";
		return mapList(sql, false, branchId);
	}

	@Override
	public boolean isBelongsTo(long classroomId, long branch_id) {
		String sql = "select u.id from Classroom u where u.removed=? and u.branch.id=? and u.id=?";
		return exists(sql, false, branch_id, classroomId);
	}

}
