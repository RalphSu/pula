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
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.SalesmanCondition;
import pula.sys.daos.SalesmanDao;
import pula.sys.domains.Salesman;

@Repository
public class SalesmanDaoImpl extends HibernateGenericDao<Salesman, Long>
		implements SalesmanDao {

	@Override
	public Salesman save(Salesman rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		rt.setCreatedTime(Calendar.getInstance());

		rt.setEnabled(true);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Salesman> search(SalesmanCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(SalesmanCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		dc.createAlias("uu.branch", "b");

		HibernateTool.eqIfNotZero(dc, "b.id", condition.getBranchId());
		HibernateTool.likeIfNotEmpty(dc, "name", "no", condition.getKeywords());
		HibernateTool.eqIfNotZero(dc, "uu.gender", condition.getGender());
		HibernateTool.eqIfHas(dc, "uu.enabled", condition.getEnabledStatus());
		return dc;
	}

	@Override
	public Salesman update(Salesman rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Salesman po = this.findById(rt.getId());
		po.setId(rt.getId());
		po.setBranch(rt.getBranch());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setUpdater(rt.getUpdater());

		po.setUpdatedTime(Calendar.getInstance());
		po.setMobile(rt.getMobile());
		po.setPhone(rt.getPhone());
		po.setComments(rt.getComments());
		po.setGender(rt.getGender());
		po.setGiftPoints(rt.getGiftPoints());

		_update(po);
		return po;

	}

	// @Override
	// public MapList loadByKeywords(String n, String t, String prefix) {
	// String hql = "select no as no , (name +' '+spec) as name FROM Salesman "
	// + "WHERE (no LIKE ? or name LIKE ?  ) AND removed=?";
	//
	// String v = "%" + StringUtils.defaultIfEmpty(n, "") + "%";
	// QueryJedi qj = new QueryJedi(hql, v, v, false);
	//
	// qj.eqIfNotEmpty("salesmanType.no", t);
	//
	// if (!StringUtils.isEmpty(prefix)) {
	// qj.append(" and no like ? ", prefix + "%");
	// }
	//
	// qj.append(" ORDER BY no");
	//
	// return mapListLimit(qj.hql(), 40, qj.parameters());
	// }

	@Override
	public MapBean meta4order(String slaveSalesmanNo, long branch_id) {
		String sql = "select id as id,giftPoints as giftPoints from Salesman where no=? and branch.id=? and removed=? ";
		return mapBean(sql, slaveSalesmanNo, branch_id, false);
	}

	@Override
	public MapList loadByKeywords(String no, long branchId) {
		String hql = "select no as no , name as name FROM Salesman "
				+ "WHERE (no LIKE ? or name LIKE ?  ) AND removed=?";

		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		QueryJedi qj = new QueryJedi(hql, v, v, false);

		qj.eqIfNotZero("branch.id", branchId);
		qj.append(" ORDER BY no");

		return mapListLimit(qj.hql(), 40, qj.parameters());
	}

}
