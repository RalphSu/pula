package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.MaterialRequireCondition;
import pula.sys.domains.MaterialRequire;

public interface MaterialRequireDao extends BaseDao<MaterialRequire, Long> {

	PaginationSupport<MapBean> search(MaterialRequireCondition condition,
			int pageIndex);

	MaterialRequire save(MaterialRequire cc);

	MaterialRequire update(MaterialRequire cc);

	MapList loadByKeywords(String no, String t, String prefix);

	Long getIdByNo(String mno);

	MapList list(long branchId);

	MapBean unique(Long id);

	void precheckForRemove(Long[] id, long branchId);

	void submit(Long[] id, long branchId);

	void precheckForRejectOrApply(Long[] id);

	void reject(Long[] id, String string, String comments);

	void apply(Long[] id, String comments, String string, Integer qty);

	List<MaterialRequire> precheckForSent(Long[] id);

	void send(Long[] id, String actorId, String no);

	List<MaterialRequire> precheckForReceive(Long[] id, long branchId);

	void receive(Long[] id, String actorId, Integer qty, long branchId);

}
