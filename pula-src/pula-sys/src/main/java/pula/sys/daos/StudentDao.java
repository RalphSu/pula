package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StudentCondition;
import pula.sys.domains.Student;

public interface StudentDao extends BaseDao<Student, Long> {

	void updatePoints(int points, long id);

	PaginationSupport<MapBean> search(StudentCondition condition, int pageIndex, int pageSize);

	Student update(Student cc, boolean changePassword);

	MapBean unique(Long id);

	Student save(Student cc);

	MapList loadByKeywords(String no, long branchId);

	void checkOwner(Long[] id, long idLong);

	Long getIdByNoWithBranchId(String studentNo, long branch_id);

	MapBean metaByNo(String no, long branch_id);

	Long getIdByNo(String loginId);

	MapBean meta4exhange(String loginId);

	void checkAllowView(long teacherId, long idLong);

	MapBean meta(Long id);

	MapBean meta4login(String loginId, String password);

	Long getBranchId(Long actorId);

	Long updatePassword(Long actorId, String oldPassword, String newPassword);

	int getTotal(long actorId);

    Long getIdByName(String studentName);

}
