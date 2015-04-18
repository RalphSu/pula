package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapList;
import pula.sys.conditions.ActivityCondition;
import pula.sys.daos.ActivityDao;
import pula.sys.domains.Activity;
import pula.sys.domains.ActivityBranch;
import pula.sys.domains.Branch;

@Repository
public class ActivityDaoImpl extends HibernateGenericDao<Activity, Long>
		implements ActivityDao {

	@Override
	public Activity save(Activity rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		rt.setCreatedTime(Calendar.getInstance());
		rt.setEnabled(true);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Activity> search(ActivityCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(ActivityCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		if (condition.getBranchId() != 0) {
			DetachedCriteria subDc = DetachedCriteria.forClass(
					ActivityBranch.class, "ab");
			subDc.add(Restrictions.eq("ab.branch.id", condition.getBranchId()));
			subDc.add(Restrictions.eqProperty("ab.activity.id", "uu.id"));
			subDc.setProjection(Projections.id());

			dc.add(Subqueries.exists(subDc));
		}
		
		HibernateTool.eqIfHas(dc, "uu.enabled", condition.getEnabledStatus());

		return dc;
	}

	@Override
	public Activity update(Activity rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Activity po = this.findById(rt.getId());

		po.setBeginDate(rt.getBeginDate());
		po.setComments(rt.getComments());
		po.setEndDate(rt.getEndDate());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setPartner(rt.getPartner());
		po.setUpdatedTime(Calendar.getInstance());
		po.setUpdater(rt.getUpdater());
		_update(po);
		return po;

	}

	@Override
	public MapList loadMeta() {
		String sql = "select u.id as id ,u.no as no ,u.name as name from Activity u where u.removed=? and u.enabled=? order by u.no";
		return mapList(sql, false, true);
	}

	@Override
	public List<Activity> loadByKeywords(String no, long branchId) {
		String hql = "select u FROM Activity u WHERE (u.no LIKE ? or u.name LIKE ? ) "
				+ "AND u.removed=? and u.enabled=? and exists(select ab.id from ActivityBranch ab where ab.branch.id=? and ab.activity.id=u.id)"
				+ " ORDER BY u.no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true, branchId);
	}

	@Override
	public void saveBranch(long id, Long[] branchId, boolean clearFirst) {
		if (clearFirst) {
			String sql = "delete from ActivityBranch where activity.id=?";
			delete(sql, id);

		}
		if (branchId == null)
			return;
		// save
		Activity a = Activity.create(id);
		for (Long bid : branchId) {
			ActivityBranch ab = new ActivityBranch();
			ab.setActivity(a);
			ab.setBranch(Branch.create(bid));
			getHibernateTemplate().save(ab);
		}
	}

}
