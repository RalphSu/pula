package pula.sys.daos;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.TeacherCondition;
import pula.sys.domains.Teacher;

public interface TeacherDao extends BaseDao<Teacher, Long> {

	PaginationSupport<Teacher> search(TeacherCondition condition, int pageIndex);

	Teacher save(Teacher cc);

	Teacher update(Teacher cc, boolean updatePassword);

	PaginationSupport<MapBean> searchMapBean(TeacherCondition condition,
			int pageIndex);

	MapBean unique(Long id);

	Teacher findRefByAttendanceNo(String no);

	MapBean meta(Long id);

	MapList listMetaEnabledIn(long idLong);

	MapList loadByKeywords(String no, long branchId);

	Long getIdByNoWithBranchId(String teacherNo, long branch_id);

	Map<Long, String> allIn(List<Long> teacherIds, long branchId);

	MapBean meta4plan(String teacherNo, long branch_id);

	void checkAllowView(Long id, long idLong);

	MapBean meta4login(String loginId, String password);

	Long getBranchId(Long actorId);

	Long updatePassword(Long actorId, String oldPassword, String newPassword);

	// Employee getRefCheckIfExistsFile(String employeeNo, long id);

	// List<Teacher> loadTimeout();

}
