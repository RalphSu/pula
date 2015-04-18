package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.GiftTransferCondition;
import pula.sys.domains.GiftTransfer;

public interface GiftTransferDao extends BaseDao<GiftTransfer, Long> {

	PaginationSupport<MapBean> search(GiftTransferCondition condition,
			int pageIndex);

	GiftTransfer save(GiftTransfer cc);

	GiftTransfer update(GiftTransfer cc);

	MapList loadByKeywords(String no, String t, String prefix);

	Long getIdByNo(String mno);

	MapList list(long branchId);

	MapBean unique(Long id);

	void precheckForRemove(Long[] id, long branchId);

	void submit(Long[] id, long branchId);

	void precheckForRejectOrApply(Long[] id);

	void reject(Long[] id, String string, String comments);

	void apply(Long[] id, String comments, String string, Integer qty);

	List<GiftTransfer> precheckForSent(Long[] id);

	void send(Long[] id, String actorId, String no);

	List<GiftTransfer> precheckForReceive(Long[] id, long branchId);

	void receive(Long[] id, String actorId, Integer qty, long branchId);

}
