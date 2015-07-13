/**
 * 
 */
package pula.sys.daos.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.CourseCondition;
import pula.sys.daos.TimeCourseDao;
import pula.sys.domains.TimeCourse;

/**
 * @author Liangfei
 *
 */
@Repository
public class TimeCourseDaoImpl extends HibernateGenericDao<TimeCourse, Long> implements TimeCourseDao {

    @Override
    public PaginationSupport<TimeCourse> search(CourseCondition condition, int pageIndex) {
        DetachedCriteria criteria = makeDetachedCriteria(condition);
        return super.findPageByCriteria(criteria, new PageInfo(pageIndex), Order.asc("no"));
    }

    @Override
    public TimeCourse save(TimeCourse rt) {
        if (super.existsNo(rt.getNo())) {
            Pe.raise("编号已经存在:" + rt.getNo());
        }
        rt.setCreateTime(new Date());
        rt.setEnabled(true);
        _save(rt);
        return rt;
    }

    @Override
    public TimeCourse update(TimeCourse rt) {
        if (super.existsNo(rt.getNo(), rt.getId())) {
            Pe.raise("编号已经存在:" + rt.getNo());
        }

        TimeCourse po = this.findById(rt.getId());
        po.setNo(rt.getNo());
        po.setComments(rt.getComments());
        po.setEndTime(rt.getEndTime());
        po.setName(rt.getName());
        po.setStartTime(rt.getStartTime());
        po.setEndTime(rt.getEndTime());
        po.setUpdateTime(rt.getUpdateTime());
        po.setDurationMinute(rt.getDurationMinute());
        // more:
        po.setBranchName(rt.getBranchName());
        po.setClassRoomName(rt.getClassRoomName());
        po.setCourseType(rt.getCourseType());
        po.setEnabled(rt.isEnabled());
        po.setMaxStudentNum(rt.getMaxStudentNum());
        po.setPrice(rt.getPrice());
        po.setStartHour(rt.getStartHour());
        po.setStartMinute(rt.getStartMinute());
        po.setStartWeekDay(rt.getStartWeekDay());

        _update(po);
        return po;

    }

    @Override
    public Long getIdByName(String courseName) {
        DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
        dc.add(Restrictions.eq("name", courseName.trim()));

        if (domainPo) {
            dc.add(Restrictions.eq("removed", false));
        }

        List<TimeCourse> tcs = super.findByCriteria(dc);
        if (tcs.size() == 0) {
            return 0l;
        } else if (tcs.size() > 0) {
            return -1l;
        } else {
            return tcs.get(0).getId();
        }
    }

}
