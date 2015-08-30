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
import pula.sys.conditions.TimeCourseOrderCondition;
import pula.sys.daos.TimeCourseOrderDao;
import pula.sys.domains.TimeCourseOrder;

/**
 * @author Liangfei
 *
 */
@Repository
public class TimeCourseOrderDaoImpl extends HibernateGenericDao<TimeCourseOrder, Long> implements TimeCourseOrderDao {

    @Override
    public PaginationSupport<TimeCourseOrder> search(TimeCourseOrderCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeSearchCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.desc("updateTime"));
    }

    public DetachedCriteria makeSearchCriteria(TimeCourseOrderCondition condition) {
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
    public TimeCourseOrder save(TimeCourseOrder cc) {
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        cc.setBuyType(1); // 只支持buytype=1，次课
        _save(cc);
        return cc;
    }

    @Override
    public TimeCourseOrder update(TimeCourseOrder cc) {
        if (super.existsNo(cc.getNo(), cc.getId())) {
            Pe.raise("订单编号已经存在:" + cc.getNo());
        }

        TimeCourseOrder existed = this.findById(cc.getId());
        if (existed == null) {
            Pe.raise("订单不存在！");
        }

        TimeCourseOrder n = existed;
        n.setId(cc.getId());
        n.setUpdateTime(new Date());
        n.setCourseNo(cc.getCourseNo());
        n.setStudentNo(cc.getStudentNo());
        n.setPaied(cc.getPaied());
        n.setPaiedCount(cc.getPaiedCount());
        n.setUsedCost(cc.getUsedCost());
        n.setUsedCount(cc.getUsedCount());
        n.setBuyType(cc.getBuyType());
        n.setOrderStatus(cc.getOrderStatus());
        n.setNo(cc.getNo());
        n.setUpdator(cc.getUpdator());
        n.setComments(cc.getComments());

        n.setGongfangCount(cc.getGongfangCount());
        n.setUsedGongFangCount(cc.getUsedGongFangCount());
        n.setHuodongCount(cc.getHuodongCount());
        n.setUsedHuodongCount(cc.getUsedHuodongCount());

        _update(n);
        return n;
    }
}
