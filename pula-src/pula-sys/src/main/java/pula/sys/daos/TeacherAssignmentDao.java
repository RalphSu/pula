package pula.sys.daos;

import java.util.Calendar;
import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.TeacherAssignmentCondition;
import pula.sys.domains.TeacherAssignment;

public interface TeacherAssignmentDao extends BaseDao<TeacherAssignment, Long> {

	boolean isCurrent(long id, long branchId);

	void save(long id, long branchId, String actorId);

	void checkAllowUse(List<Long> teacher_ids, long idLong, Calendar trainDate);

	PaginationSupport<MapBean> search(TeacherAssignmentCondition condition,
			int pageIndex);

}
