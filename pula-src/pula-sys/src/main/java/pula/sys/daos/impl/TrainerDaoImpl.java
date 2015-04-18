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
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.TrainerCondition;
import pula.sys.daos.TrainerDao;
import pula.sys.domains.Trainer;

@Repository
public class TrainerDaoImpl extends HibernateGenericDao<Trainer, Long>
		implements TrainerDao {

	@Override
	public Trainer save(Trainer rt) {

		if (super.existsNo(rt.getNo())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}
		rt.setCreatedTime(Calendar.getInstance());

		_save(rt);

		return rt;
	}

	@Override
	public PaginationSupport<Trainer> search(TrainerCondition condition,
			int pageNo) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageNo),
				Order.asc("no"));
	}

	private DetachedCriteria makeDetachedCriteria(TrainerCondition condition) {
		DetachedCriteria dc = super.makeDetachedCriteria(condition);

		return dc;
	}

	@Override
	public Trainer update(Trainer rt) {
		if (super.existsNo(rt.getNo(), rt.getId())) {
			Pe.raise("编号已经存在:" + rt.getNo());
		}

		Trainer po = this.findById(rt.getId());

		po.setName(rt.getName());
		po.setNo(rt.getNo());
		_update(po);
		return po;
	}

	@Override
	public List<Trainer> loadByKeywords(String no) {
		String hql = "FROM Trainer WHERE (no LIKE ? or name LIKE ? ) AND removed=? and enabled=? ORDER BY no";
		String v = "%" + StringUtils.defaultIfEmpty(no, "") + "%";
		return findLimitByQuery(hql, 40, v, v, false, true);
	}

	// private boolean isSys(Long n) {
	// String hql = "select id from Trainer where id=? and sys=?";
	// return this.exists(hql, n, true);
	// }

	@Override
	public List<Trainer> loadByNo() {
		String hql = "select u from Trainer u where u.removed = ?  order by u.no ";
		return find(hql, false);
	}

}
