/**
 * 
 */
package pula.sys.daos.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.NoticeCondition;
import pula.sys.daos.NoticeDao;
import pula.sys.domains.Notice;

/**
 * @author Liangfei
 *
 */
@Repository
public class NoticeDaoImpl extends HibernateGenericDao<Notice, Long> implements NoticeDao {

    @Override
    public PaginationSupport<Notice> search(NoticeCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeSearchCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.asc("updateTime"));
    }

    public DetachedCriteria makeSearchCriteria(NoticeCondition condition) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
        if (!StringUtils.isEmpty(condition.getKeywords())) {
            dc.add(Restrictions.like("title", condition.getKeywords().trim(), MatchMode.ANYWHERE));
        }

        if (domainPo) {
            dc.add(Restrictions.eq("removed", false));
        }

        return dc;
    }

    @Override
    public Notice save(Notice cc) {
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        _save(cc);
        return cc;
    }

    @Override
    public Notice update(Notice cc) {
        if (super.existsNo(cc.getNo(), cc.getId())) {
            Pe.raise("编号已经存在:" + cc.getNo());
        }
        
        Notice existed = this.findById(cc.getId());
        if (existed == null) {
            Pe.raise("通知不存在！");
        }

        Notice n = existed;
        n.setId(cc.getId());
        n.setUpdateTime(new Date());
        n.setContent(cc.getContent());
        n.setTitle(cc.getTitle());
        n.setFormattedTitle(cc.getFormattedTitle());
        n.setImgPath(cc.getImgPath());
        n.setSuffix(cc.getSuffix());
        n.setNo(cc.getNo());
        n.setComment(cc.getComment());
        n.setNoticeCount(cc.getNoticeCount());
        n.setNoticeCourseNo(cc.getNoticeCourseNo());
        n.setNoticePrice(cc.getNoticePrice());
        
        _update(n);;
        return n;
    }

}
