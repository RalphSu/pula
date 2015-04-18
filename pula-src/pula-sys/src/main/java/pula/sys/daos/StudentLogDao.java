package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.StudentLogCondition;
import pula.sys.domains.StudentLog;

public interface StudentLogDao extends BaseDao<StudentLog, Long> {

	PaginationSupport<MapBean> search(StudentLogCondition condition,
			int pageIndex);

	void save(Long asLong, Long asLong2, String ip, String string);

}
