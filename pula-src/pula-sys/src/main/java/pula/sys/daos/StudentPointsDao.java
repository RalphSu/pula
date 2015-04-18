package pula.sys.daos;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import pula.sys.conditions.StudentPointsCondition;
import pula.sys.domains.StudentPoints;
import pula.sys.vo.ExchangePointsVo;
import pula.sys.vo.PointsRowVo;

public interface StudentPointsDao extends BaseDao<StudentPoints, String> {

	public StudentPoints save(StudentPoints p);

	public int getSummary(Long ownerId, int flag);

	PaginationSupport<StudentPoints> search(StudentPointsCondition condition,
			int pageNo);

	public boolean hasPoints(String targetId, String ownerId);

	public List<StudentPoints> getPoints(long id);

	public int getTotalPoints(long id);

	public Map<String, Object> summary(StudentPointsCondition c);

	public Mix<List<ExchangePointsVo>, ExchangePointsVo> list(
			StudentPointsCondition condition);

	public PointsRowVo getPointsRow(long id);

	public StudentPoints findByRefId(String buildRefId);

	public PaginationSupport<MapBean> search4front(Long actorId, int pageIndex);
}
