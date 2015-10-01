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
import pula.sys.conditions.TimeCourseOrderUsageCondition;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.TimeCourseOrderUsage;

/**
 * @author Liangfei
 *
 */
@Repository
public class TimeCourseOrderUsageDaoImpl extends HibernateGenericDao<TimeCourseOrderUsage, Long> implements
        TimeCourseUsageDao {

    @Override
    public PaginationSupport<TimeCourseOrderUsage> search(TimeCourseOrderUsageCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeSearchCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.desc("updateTime"));
    }

    public DetachedCriteria makeSearchCriteria(TimeCourseOrderUsageCondition condition) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
        if (!StringUtils.isEmpty(condition.getStudentNo())) {
            dc.add(Restrictions.like("studentNo", condition.getStudentNo()));
        }

        if (!StringUtils.isEmpty(condition.getCourseNo())) {
            dc.add(Restrictions.like("courseNo", condition.getCourseNo()));
        }

        if (domainPo) {
            dc.add(Restrictions.eq("removed", false));
        }

        return dc;
    }

    @Override
    public TimeCourseOrderUsage save(TimeCourseOrderUsage cc) {
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        _save(cc);
        return cc;
    }

    @Override
    public TimeCourseOrderUsage update(TimeCourseOrderUsage cc) {
        if (super.existsNo(cc.getNo(), cc.getId())) {
            Pe.raise("订单编号已经存在:" + cc.getNo());
        }

        TimeCourseOrderUsage existed = this.findById(cc.getId());
        if (existed == null) {
            Pe.raise("订单不存在！");
        }

        if (!existed.getOrderNo().equals(cc.getOrderNo())) {
            Pe.raise(String.format("订单号不能修改: 原订单号：%s，新订单号：%s", existed.getOrderNo(), cc.getOrderNo()));
        }

        TimeCourseOrderUsage n = existed;
        n.setId(cc.getId());
        n.setUpdateTime(new Date());
        n.setCourseNo(cc.getCourseNo());
        n.setStudentNo(cc.getStudentNo());
        n.setUsedCost(cc.getUsedCount());
        n.setUsedCount(cc.getUsedCount());
//        n.setNo(cc.getNo());
        n.setUpdator(cc.getUpdator());
        n.setComments(cc.getComments());
        n.setUsedGongfangCount(cc.getUsedGongfangCount());
        n.setUsedHuodongCount(cc.getUsedHuodongCount());
        n.setUsedSpecialCourseCount(cc.getUsedSpecialCourseCount());
        _update(n);
        return n;
    }
}
