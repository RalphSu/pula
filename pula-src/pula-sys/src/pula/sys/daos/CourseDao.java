package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseCondition;
import pula.sys.domains.Course;

public interface CourseDao extends BaseDao<Course, Long> {

	PaginationSupport<Course> search(CourseCondition condition,
			int pageIndex);

	Course save(Course cc);

	Course update(Course cc);

	MapList loadByKeywords(String no, String t, String prefix);


	Long getIdByNo(String mno);

	MapList list(String categoryId);

	MapList mapList4web(String type_no);

}
