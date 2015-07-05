/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.CourseCondition;
import pula.sys.domains.TimeCourse;

/**
 * @author Liangfei
 *
 */
public interface TimeCourseDao extends BaseDao<TimeCourse, Long> {


    PaginationSupport<TimeCourse> search(CourseCondition condition,
            int pageIndex);

    TimeCourse save(TimeCourse cc);

    TimeCourse update(TimeCourse cc);

//    MapList loadByKeywords(String no, String t, String prefix);

    Long getIdByNo(String mno);

//    MapList list(String categoryId);
//
//    MapList mapList4web(String type_no);


}
