package pula.sys.daos;

import java.util.Collection;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.TeacherPerformanceCondition;
import pula.sys.domains.TeacherPerformance;

public interface TeacherPerformanceDao extends
		BaseDao<TeacherPerformance, Long> {

	Map<String, MapBean> map(int year, int java_month, long branchId);

	Map<Long, Long> getExists(int year, int java_month, long branchId);

	void update(TeacherPerformance teacherPerformance, String actorId,
			boolean update);

	void remove(Collection<Long> values);

	PaginationSupport<MapBean> search(TeacherPerformanceCondition condition,
			int pageIndex);

}
