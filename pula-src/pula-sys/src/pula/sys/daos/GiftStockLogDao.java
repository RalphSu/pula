package pula.sys.daos;

import java.util.Collection;
import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.GiftStockLogCondition;
import pula.sys.domains.GiftStockEvent;
import pula.sys.domains.GiftStockLog;
import pula.sys.domains.GiftTransfer;

public interface GiftStockLogDao extends BaseDao<GiftStockLog, Long> {

	GiftStockLog log(GiftStockLog note);

	PaginationSupport<MapBean> search(GiftStockLogCondition condition,
			int pageIndex);

	MapList export(GiftStockLogCondition condition);

	MapBean unique(Long id);

	GiftStockLog save(GiftStockEvent se);

	Mix<GiftStockLog, GiftStockLog> save(
			Mix<GiftStockEvent, GiftStockEvent> mix1);

	void log(List<GiftStockLog> logs);

	List<GiftStockLog> saveSend(List<GiftTransfer> mrs, long branchId);

	List<GiftStockLog> saveReceive(Collection<GiftTransfer> values,
			long branch_id);

	void save(GiftStockLog gsl);

}
