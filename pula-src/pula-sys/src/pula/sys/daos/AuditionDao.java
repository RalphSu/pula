package pula.sys.daos;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.AuditionCondition;
import pula.sys.domains.Audition;

public interface AuditionDao extends BaseDao<Audition, Long> {

	List<Audition> loadMy(String actorId);

	void update(Audition item, String actorId);

	void remove(Collection<Long> values);

	Map<Long, Long> loadMyIds(String actorId);

	PaginationSupport<Audition> search(AuditionCondition condition, int pageIndex);

}
