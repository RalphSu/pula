/**
 * 
 */
package pula.sys.daos.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import puerta.support.PaginationSupport;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeCourseOrder save(TimeCourseOrder cc) {
        cc.setCreateTime(new Date());
        cc.setUpdateTime(new Date());
        cc.setEnabled(true);
        _save(cc);
        return cc;
    }

    @Override
    public TimeCourseOrder update(TimeCourseOrder cc) {
        // TODO Auto-generated method stub
        return null;
    }
}
