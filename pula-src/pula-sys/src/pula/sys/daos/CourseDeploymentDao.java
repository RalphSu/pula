package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseDeploymentCondition;
import pula.sys.domains.CourseDeployment;

public interface CourseDeploymentDao extends BaseDao<CourseDeployment, Long> {

	void save(Long[] objId, Long classroomId, String actorId);

	PaginationSupport<MapBean> searchMapBean(
			CourseDeploymentCondition condition, int pageIndex);

	boolean hasCourse(long classroomId, long courseId);

	MapList list(long classroomId, String categoryId);

	MapList listByBranch(long idLong, String categoryId);

	MapList mapList(Long classroomId);

	// MapBean unique(Long id);

	// Employee getRefCheckIfExistsFile(String employeeNo, long id);

	// List<Teacher> loadTimeout();

}
