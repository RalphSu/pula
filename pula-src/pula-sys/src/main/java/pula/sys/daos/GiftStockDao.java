package pula.sys.daos;

import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.GiftStockCondition;
import pula.sys.domains.GiftStock;
import pula.sys.domains.GiftStockLog;

public interface GiftStockDao extends BaseDao<GiftStock, Long> {

	void update(GiftStockLog... stockLog);

	MapList listBySpaceId(long id);

	Map<String, Integer> mapByBranchId(long id);

	void restore(GiftStockLog stockLog);

	PaginationSupport<GiftStock> search(GiftStockCondition condition,
			int pageIndex);

	MapList export(GiftStockCondition condition);

	// MapBean getAlert(Long m);

	// MapList listByProductVersion(long id);

	// PaginationSupport<MapBean> searchAvailable(GiftStockCondition condition,
	// int pageIndex);

	PaginationSupport<MapBean> searchMapBean(GiftStockCondition condition,
			int pageIndex);

	boolean isEnough(Long asLong, int qty, long idLong);

}
