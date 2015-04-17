package pula.sys.daos;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import pula.sys.conditions.CourseTaskResultCondition;
import pula.sys.domains.CourseTaskResult;
import pula.sys.domains.CourseTaskResultReportComments;

public interface CourseTaskResultDao extends BaseDao<CourseTaskResult, Long> {

	CourseTaskResult save(CourseTaskResult ctr);

	PaginationSupport<MapBean> search(CourseTaskResultCondition condition,
			int pageIndex);

	MapBean unique(Long id);

	void checkAllowView(long id, long branch_id);

	void checkAllowEdit(long id, long branch_id);

	CourseTaskResult update(CourseTaskResult cc);

	void deleteById(Long[] id);

	void checkAllowRemove(Long[] id, long idLong);

	MapBean meta(long id);

	Map<String, MapBean> stat4Teacher(int year, int java_month, long branchId);

	void incStudent(Long courseTaskResultId, int seed);

	Mix<CourseTaskResult, Boolean> store(CourseTaskResult ctr);

	void storeComments(List<CourseTaskResultReportComments> results,
			CourseTaskResult object1);

}
