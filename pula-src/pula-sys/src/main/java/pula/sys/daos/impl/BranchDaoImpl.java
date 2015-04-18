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
import puerta.system.vo.MapList;
import pula.sys.conditions.BranchCondition;
import pula.sys.daos.BranchDao;
import pula.sys.domains.Branch;

@Repository
public class BranchDaoImpl extends HibernateGenericDao<Branch, Long> implements
		BranchDao {

	@Override
	public Branch save(Branch rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		// 总部只有一个
		rt.setHeadquarter(false);
		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Branch> search(BranchCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(BranchCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		return dc;
	}

	@Override
	public Branch update(Branch rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		Branch po = this.findById(rt.getId());
		po.setAddress(rt.getAddress());
		po.setComments(rt.getComments());
		po.setCreator(rt.getCreator());
		po.setEmail(rt.getEmail());
		po.setFax(rt.getFax());

		// po.setHeadquarter( rt.isHeadquarter() );
		po.setId(rt.getId());
		po.setLinkman(rt.getLinkman());
		po.setMobile(rt.getMobile());
		po.setName(rt.getName());
		po.setNo(rt.getNo());
		po.setPhone(rt.getPhone());
		po.setShowInWeb(rt.isShowInWeb());
		po.setUpdater(rt.getUpdater());
		po.setPrefix(rt.getPrefix());
		_update(po);
		return po;
	}

	@Override
	public List<Branch> loadByKeywords(String no) {
		String hql = "FROM Branch WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true);
	}

	@Override
	public boolean isHeadQuarter(Long i) {
		String sql = "select id from Branch where headquarter=? and id=?";
		return exists(sql, true, i);
	}

	@Override
	public MapList loadMeta() {
		String sql = "select u.id as id ,u.no as no ,u.name as name from Branch u where u.removed=? and u.enabled=? order by u.no";
		return mapList(sql, false, true);
	}

	@Override
	public MapList loadMetaWithoutHeadquarter() {
		String sql = "select u.id as id ,u.no as no ,u.name as name from Branch u where u.removed=? and u.enabled=? and u.headquarter=? order by u.no";
		return mapList(sql, false, true, false);
	}

	@Override
	public String getPrefix(long idLong) {
		String sql = "select u.prefix from Branch u where u.id=?";
		return findSingle(sql, idLong);
	}

}
