package pula.sys.daos;

import java.util.Calendar;

import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.domains.CourseTaskPlan;

public interface CourseTaskPlanDao extends BaseDao<CourseTaskPlan, Long> {

	void checkAllowEdit(long id, long branch_id);

	CourseTaskPlan save(CourseTaskPlan plan);

	CourseTaskPlan update(CourseTaskPlan plan);

	MapBean unique(Long id);

	MapList mapList(int year, int month, long branchId);

	MapList planList(Calendar sDate, long branchId);

	MapList listByTeacher(int year, int month, Long actorId);

}
