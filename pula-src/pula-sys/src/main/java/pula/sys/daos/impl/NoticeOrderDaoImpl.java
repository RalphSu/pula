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
import puerta.support.utils.MD5;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.NoticeOrderCondition;
import pula.sys.daos.NoticeOrderDao;
import pula.sys.domains.NoticeOrder;

/**
 * @author Liangfei
 *
 */
@Repository
public class NoticeOrderDaoImpl extends HibernateGenericDao<NoticeOrder, Long> implements NoticeOrderDao {

    @Override
    public PaginationSupport<NoticeOrder> search(NoticeOrderCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeSearchCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.desc("updateTime"));
    }

    public DetachedCriteria makeSearchCriteria(NoticeOrderCondition condition) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
        if (!StringUtils.isEmpty(condition.getStudentNo())) {
            dc.add(Restrictions.like("studentNo", condition.getStudentNo()));
        }

        if (!StringUtils.isEmpty(condition.getNoticeNo())) {
            dc.add(Restrictions.like("noticeNo", condition.getNoticeNo()));
        }

        if (domainPo) {
            dc.add(Restrictions.eq("removed", false));
        }

        return dc;
    }

    @Override
    public NoticeOrder save(NoticeOrder cc) {
        if (StringUtils.isEmpty(cc.getNo())) {
            cc.setNo(MD5.GetMD5String("@NoticeOrder:" + System.currentTimeMillis()));
        }
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        _save(cc);
        return cc;
    }

    @Override
    public NoticeOrder update(NoticeOrder cc) {
        if (super.existsNo(cc.getNo(), cc.getId())) {
            Pe.raise("订单编号已经存在:" + cc.getNo());
        }

        NoticeOrder existed = this.findById(cc.getId());
        if (existed == null) {
            Pe.raise("订单不存在！");
        }

        NoticeOrder n = existed;
        n.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(cc.getStudentNo())) {
            n.setStudentNo(cc.getStudentNo());
        }
        if (cc.getStudentId() != 0) {
            n.setStudentId(cc.getStudentId());
        }
        if (cc.getOrderPayStatus() > 0) {
            n.setOrderPayStatus(cc.getOrderPayStatus());
        }
        if (!StringUtils.isEmpty(cc.getNo())) {
            n.setNo(cc.getNo());
        }
        n.setComments(cc.getComments());
        if (cc.getNoticeId() > 0) {
            n.setNoticeId(cc.getNoticeId());
        }
        if (!StringUtils.isEmpty(cc.getNoticeName())) {
            n.setNoticeName(cc.getNoticeName());
        }
        if (!StringUtils.isEmpty(cc.getNoticeNo())) {
            n.setNoticeNo(cc.getNoticeNo());
        }
        n.setNoticePrice(cc.getNoticePrice());
        n.setCount(cc.getCount());
        //
        if (cc.getPaied() > 0) {
            n.setPaied(cc.getPaied());
        }
        if (!StringUtils.isEmpty(cc.getPrepayId())) {
            n.setPrepayId(cc.getPrepayId());
        }
        if (!StringUtils.isEmpty(cc.getAccessToken())) {
            n.setAccessToken(cc.getAccessToken());
        }
        if (!StringUtils.isEmpty(cc.getWxOrderId())) {
            n.setWxOrderId(cc.getWxOrderId());
        }
        n.setWxPayStatus(cc.getWxPayStatus());
        if (!StringUtils.isEmpty(cc.getWxOrderDetail())) {
            n.setWxOrderDetail(cc.getWxOrderDetail());
        }

        _update(n);
        return n;
    }

}
