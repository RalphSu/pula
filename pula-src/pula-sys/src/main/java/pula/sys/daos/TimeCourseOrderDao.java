/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.TimeCourseOrderCondition;
import pula.sys.domains.TimeCourseOrder;


/**
 * @author Liangfei
 *
 */
public interface TimeCourseOrderDao extends BaseDao<TimeCourseOrder, Long> {

    PaginationSupport<TimeCourseOrder> search(TimeCourseOrderCondition condition, int pageIndex);

    TimeCourseOrder save(TimeCourseOrder cc);

    TimeCourseOrder update(TimeCourseOrder cc);

    
}
