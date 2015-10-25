package pula.sys.daos;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.AuditionCondition;
import pula.sys.domains.Audition;

public interface AuditionDao extends BaseDao<Audition, Long> {

	List<Audition> loadByBranch(String branchNo);

	void update(Audition item, String actorId);

	void remove(Collection<Long> values);

	Map<Long, Long> loadBranchAuditionIds(String actorId);

	PaginationSupport<Audition> search(AuditionCondition condition, int pageIndex);

}
