package pula.sys.daos.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.SysUserGroupCondition;
import pula.sys.daos.SysUserGroupDao;
import pula.sys.domains.SysUserGroup;

@Repository
public class SysUserGroupDaoImpl extends
		HibernateGenericDao<SysUserGroup, String> implements SysUserGroupDao {

	@Override
	public PaginationSupport<SysUserGroup> search(
			SysUserGroupCondition condition, int pageIndex) {

		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageIndex),
				Order.asc("no"));
	}

	@Override
	public void save(SysUserGroup g) {
		if (super.existsNo(g.getNo())) {
			Pe.raise("编号已经存在:" + g.getNo());
		}
		
		g.setEnabled(true);

		_save(g);

	}

	@Override
	public void update(SysUserGroup rt) {

		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		SysUserGroup po = this.findById(rt.getId());
		//po.setEnabled(rt.isEnabled());
		po.setId(rt.getId());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		_update(po);

	}

	@Override
	public List<SysUserGroup> loadByKeywords(String no) {

		String hql = "FROM SysUserGroup WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true);
	}

}
