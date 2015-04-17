package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.CourseClientCondition;
import pula.sys.domains.CourseClient;

public interface CourseClientDao extends BaseDao<CourseClient, Long> {

	PaginationSupport<MapBean> search(CourseClientCondition condition,
			int pageIndex);

	MapBean unique(long id);

	void apply(CourseClient courseClient);

	void update(CourseClient cc);

	void request(String code, String comments,String ip );

	MapBean hasRequest(String code);

	Long getClassroomId(String code, String activeCode);

	Object[] getClassroomIdAndBranchId(String code, String activeCode);

}
