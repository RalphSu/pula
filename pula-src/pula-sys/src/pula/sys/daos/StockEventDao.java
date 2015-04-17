package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import pula.sys.conditions.StockEventCondition;
import pula.sys.domains.StockEvent;

public interface StockEventDao extends BaseDao<StockEvent, Long> {

	PaginationSupport<MapBean> search(StockEventCondition condition,
			int pageIndex);

	StockEvent save(StockEvent cc);

	Mix<StockEvent, StockEvent> update(StockEvent cc);

	MapBean unique(Long id);

	List<StockEvent> load(Long[] id);

}
