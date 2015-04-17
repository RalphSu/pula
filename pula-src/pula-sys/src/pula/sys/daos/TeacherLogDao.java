package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.TeacherLogCondition;
import pula.sys.domains.TeacherLog;

public interface TeacherLogDao extends BaseDao<TeacherLog, Long> {

	PaginationSupport<MapBean> search(TeacherLogCondition condition,
			int pageIndex);

	void save(Long teacherId, Long branchId,String ip, String string);

}
