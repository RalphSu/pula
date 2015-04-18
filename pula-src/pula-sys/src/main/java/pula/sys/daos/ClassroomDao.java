package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapList;
import pula.sys.conditions.ClassroomCondition;
import pula.sys.domains.Classroom;

public interface ClassroomDao extends BaseDao<Classroom, Long> {

	PaginationSupport<Classroom> search(ClassroomCondition condition,
			int pageIndex);

	Classroom save(Classroom cc);

	Classroom update(Classroom cc);

	MapList loadByKeywords(String no, String t, String prefix);

	Long getIdByNo(String mno);

	MapList list(long branchId);

	boolean isBelongsTo(long classroomId, long branch_id);

}
