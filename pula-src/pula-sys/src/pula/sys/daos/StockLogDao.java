package pula.sys.daos;

import java.util.Collection;
import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StockLogCondition;
import pula.sys.domains.MaterialRequire;
import pula.sys.domains.StockEvent;
import pula.sys.domains.StockLog;

public interface StockLogDao extends BaseDao<StockLog, Long> {

	StockLog log(StockLog note);

	PaginationSupport<MapBean> search(StockLogCondition condition, int pageIndex);

	MapList export(StockLogCondition condition);

	MapBean unique(Long id);

	StockLog save(StockEvent se);

	Mix<StockLog, StockLog> save(Mix<StockEvent, StockEvent> mix1);

	void log(List<StockLog> logs);

	List<StockLog> saveSend(List<MaterialRequire> mrs, long branchId);

	List<StockLog> saveReceive(Collection<MaterialRequire> values);

}
