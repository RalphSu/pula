package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.dao.HibernateTool;
import puerta.support.dao.QueryJedi;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.AuditionCondition;
import pula.sys.daos.AuditionDao;
import pula.sys.domains.Audition;
import pula.sys.domains.SysUser;

@Repository
public class AuditionDaoImpl extends HibernateGenericDao<Audition, Long>
		implements AuditionDao {

	@Override
	public List<Audition> loadMy(String actorId) {
		String sql = "select u from Audition u where u.removed=? and u.closed=? and u.owner.id=?";
		return find(sql, false, false, actorId);
	}

	@Override
	public void update(Audition rt, String actorId) {
		// 更新

		Audition po = this.findById(rt.getId());

		if (po == null) {
			// save new
			po = rt;
			po.setOwner(SysUser.create(actorId));
			po.setCreatedTime(Calendar.getInstance());

			getHibernateTemplate().save(po);
		} else {

			if (!po.getOwner().getId().equals(actorId) || po.isRemoved()) {
				Pe.raise("越权访问");
			}

			// 已经关了,界面还有,所以直接跳过
			if (po.isClosed()) {
				return;
			}

			// 更新

			po.setAge(rt.getAge());
			po.setBranch(rt.getBranch());
			po.setClosed(rt.isClosed());
			po.setComments(rt.getComments());
			po.setContent(rt.getContent());
			po.setParent(rt.getParent());
			po.setPhone(rt.getPhone());
			po.setPlan1(rt.getPlan1());
			po.setPlan2(rt.getPlan2());
			po.setPlan3(rt.getPlan3());
			po.setPlan4(rt.getPlan4());
			po.setPlan5(rt.getPlan5());
			po.setResult(rt.getResult());
			po.setStudent(rt.getStudent());
			po.setUpdatedTime(rt.getUpdatedTime());
			_update(po);
		}

	}

	@Override
	public void remove(Collection<Long> values) {
		QueryJedi qj = new QueryJedi(
				"update from Audition set removed=? where ", true);
		qj.eqOr("id", values.toArray());

		delete(qj.hql(), qj.parameters());

	}

	@Override
	public Map<Long, Long> loadMyIds(String actorId) {
		String sql = "select u.id,u.id from Audition u where u.removed=? and u.closed=? and u.owner.id=?";
		return HibernateTool.asMap(find(sql, false, false, actorId));
	}

	@Override
	public PaginationSupport<Audition> search(AuditionCondition condition,
			int pageIndex) {
		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, new PageInfo(pageIndex),
				Order.desc("createdTime"));
	}

	private DetachedCriteria makeDetachedCriteria(AuditionCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(Audition.class, "uu");

		HibernateTool.likeIfNotEmpty(dc, "plan1", "plan2", "plan3", "plan4",
				"plan5", "student", "parent", "phone", "content",
				condition.getKeywords());

		HibernateTool.eqIfNotEmpty(dc, "result.id", condition.getResultId());
		HibernateTool.eqIfNotZero(dc, "branch.id", condition.getBranchId());
		HibernateTool.eqIfHas(dc, "closed", condition.getClosedStatus());
		HibernateTool.betweenIfNotNull(dc, "createdTime",
				condition.getBeginDate(), condition.getEndDate(), -1);

		if (!StringUtils.isEmpty(condition.getSalesmanNo())) {
			dc.createAlias("uu.owner", "usr", DetachedCriteria.LEFT_JOIN);
			dc.createAlias("usr.salesman", "sm", DetachedCriteria.LEFT_JOIN);

			HibernateTool.eq(dc, "sm.no", condition.getSalesmanNo());
		}

        if (!StringUtils.isEmpty(condition.getStudentNo())) {
            HibernateTool.eq(dc, "studentNo", condition.getStudentNo());
        }

        if (!StringUtils.isEmpty(condition.getStudentName())) {
            HibernateTool.eq(dc, "student", condition.getStudentName());
        }

		dc.add(Restrictions.eq("removed", false));

		return dc;
	}

}
