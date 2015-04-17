package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseTaskResultStudentCondition;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultStudent;
import pula.sys.domains.Student;

public interface CourseTaskResultStudentDao extends
		BaseDao<CourseTaskResultStudent, Long> {

	CourseTaskResultStudent save(CourseTaskResultStudent s);

	MapList mapList(Long id);

	boolean exists(Long courseTaskResultId, long studentId);

	MapBean meta(Long courseTaskResultStudentId);

	MapBean meta4work(Long courseTaskResultStudentId);

	PaginationSupport<MapBean> search(
			CourseTaskResultStudentCondition condition, int pageIndex);

	MapList list4hits(Long actorId, String type_no);

	MapBean allowPlayGame(Long actorId, long courseId);

	void gamePlayed(long courseTaskResultStudentId);

	List<MapBean> save(List<Student> results, CourseTaskResult object1, Boolean update);
}
