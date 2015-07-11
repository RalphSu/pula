/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.TimeCourseOrderUsageCondition;
import pula.sys.domains.TimeCourseOrderUsage;

/**
 * @author Liangfei
 *
 */
public interface TimeCourseUsageDao extends BaseDao<TimeCourseOrderUsage, Long> {

    PaginationSupport<TimeCourseOrderUsage> search(TimeCourseOrderUsageCondition condition, int pageIndex);

    TimeCourseOrderUsage save(TimeCourseOrderUsage cc);

    TimeCourseOrderUsage update(TimeCourseOrderUsage cc);

}
