package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.TrainLogCondition;
import pula.sys.domains.TrainLog;

public interface TrainLogDao extends BaseDao<TrainLog, Long> {

	TrainLog save(TrainLog cc);

	PaginationSupport<MapBean> search(TrainLogCondition condition, int pageIndex);

	TrainLog update(TrainLog cc);

	void checkAllowEdit(long id, long branchId);

	void checkAllowView(long id, long branchId);

	void checkAllowRemove(Long[] id, long idLong);

}
