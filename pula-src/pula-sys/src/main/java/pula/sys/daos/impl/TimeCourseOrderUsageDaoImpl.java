/**
 * 
 */
package pula.sys.daos.impl;

import org.springframework.stereotype.Repository;

import puerta.support.PaginationSupport;
import puerta.system.base.HibernateGenericDao;
import pula.sys.conditions.TimeCourseOrderUsageCondition;
import pula.sys.daos.TimeCourseUsageDao;
import pula.sys.domains.TimeCourseOrderUsage;

/**
 * @author Liangfei
 *
 */
@Repository
public class TimeCourseOrderUsageDaoImpl extends HibernateGenericDao<TimeCourseOrderUsage, Long> implements TimeCourseUsageDao {

    @Override
    public PaginationSupport<TimeCourseOrderUsage> search(TimeCourseOrderUsageCondition condition, int pageIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeCourseOrderUsage save(TimeCourseOrderUsage cc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TimeCourseOrderUsage update(TimeCourseOrderUsage cc) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
