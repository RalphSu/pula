package pula.sys.daos;

import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.domains.CourseTaskResultWork;

public interface CourseTaskResultWorkDao extends
		BaseDao<CourseTaskResultWork, Long> {

	CourseTaskResultWork save(CourseTaskResultWork w);

	Map<String, MapBean> map(Long id);

	void updateKey(String randomString, Long rwid);

	Long getId(Long resultId, Long studentId);

	MapBean meta4upload(Long resultId);

	MapBean meta4view(Long id);

	MapList mapList(long resultId);

	PaginationSupport<MapBean> list4score(Long actorId, int pageIndex);

	MapBean meta4score(long courseTaskResultWorkId, Long actorId);

	MapBean getFile(long courseTaskResultWorkId, Long actorId);

	void makeScore(long id, int s1, int s2, int s3, int s4, int s5,
			boolean updateScoreTime);

	MapBean stat4radar(Long actorId);

}
