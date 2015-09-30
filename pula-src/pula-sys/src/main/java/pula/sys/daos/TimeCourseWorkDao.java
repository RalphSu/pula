/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.TimeCourseWorkCondition;
import pula.sys.domains.TimeCourseWork;

/**
 * @author Liangfei
 *
 */
public interface TimeCourseWorkDao extends BaseDao<TimeCourseWork, Long> {

    PaginationSupport<TimeCourseWork> search(TimeCourseWorkCondition condition, int pageIndex);

    TimeCourseWork save(TimeCourseWork cc);

    TimeCourseWork update(TimeCourseWork cc);

    Long getIdByNo(String mno);

}
