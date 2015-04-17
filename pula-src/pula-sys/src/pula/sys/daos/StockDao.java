package pula.sys.daos;

import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.StockCondition;
import pula.sys.domains.Stock;
import pula.sys.domains.StockLog;

public interface StockDao extends BaseDao<Stock, Long> {

	void update(StockLog... stockLog);

	MapList listBySpaceId(long id);

	Map<String, Integer> mapByBranchId(long id);

	void restore(StockLog stockLog);

	PaginationSupport<Stock> search(StockCondition condition, int pageIndex);

	MapList export(StockCondition condition);

	// MapBean getAlert(Long m);

	// MapList listByProductVersion(long id);

	// PaginationSupport<MapBean> searchAvailable(StockCondition condition,
	// int pageIndex);

	PaginationSupport<MapBean> searchMapBean(StockCondition condition,
			int pageIndex);


}
