package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import pula.sys.conditions.GiftStockEventCondition;
import pula.sys.domains.GiftStockEvent;

public interface GiftStockEventDao extends BaseDao<GiftStockEvent, Long> {

	PaginationSupport<MapBean> search(GiftStockEventCondition condition,
			int pageIndex);

	GiftStockEvent save(GiftStockEvent cc);

	Mix<GiftStockEvent, GiftStockEvent> update(GiftStockEvent cc);

	MapBean unique(Long id);

	List<GiftStockEvent> load(Long[] id);

}
