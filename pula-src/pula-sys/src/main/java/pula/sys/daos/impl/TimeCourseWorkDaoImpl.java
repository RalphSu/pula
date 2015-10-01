/**
 * 
 */
package pula.sys.daos.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.TimeCourseWorkCondition;
import pula.sys.daos.TimeCourseWorkDao;
import pula.sys.domains.TimeCourseWork;

/**
 * @author Liangfei
 *
 */
@Repository
public class TimeCourseWorkDaoImpl extends HibernateGenericDao<TimeCourseWork, Long> implements TimeCourseWorkDao {

    @Override
    public PaginationSupport<TimeCourseWork> search(TimeCourseWorkCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeSearchCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.desc("updateTime"));
    }

    public DetachedCriteria makeSearchCriteria(TimeCourseWorkCondition condition) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
        if (!StringUtils.isEmpty(condition.getStudentNo())) {
            dc.add(Restrictions.like("studentNo", condition.getStudentNo()));
        }

        if (!StringUtils.isEmpty(condition.getCourseNo())) {
            dc.add(Restrictions.like("courseNo", condition.getCourseNo()));
        }

        if (condition.getRate() >= 0) {
            dc.add(Restrictions.eq("rate", condition.getRate()));
        }

        if (domainPo) {
            dc.add(Restrictions.eq("removed", false));
        }

        return dc;
    }

    @Override
    public TimeCourseWork save(TimeCourseWork cc) {
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        _save(cc);
        return cc;
    }

    @Override
    public TimeCourseWork update(TimeCourseWork cc) {
        if (super.existsNo(cc.getNo(), cc.getId())) {
            Pe.raise("课程作业编号已经存在:" + cc.getNo());
        }

        TimeCourseWork existed = this.findById(cc.getId());
        if (existed == null) {
            Pe.raise("订单课程作业编号！");
        }

        existed.setUpdateTime(new Date());
        existed.setComments(cc.getComments());
        existed.setCourseNo(cc.getCourseNo());
        existed.setImagePath(cc.getImagePath());
        existed.setRate(cc.getRate());
        existed.setStudentNo(cc.getStudentNo());
        existed.setWorkEffectDate(cc.getWorkEffectDate());

        _update(existed);
        return existed;
    }

}
