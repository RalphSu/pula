package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CardCondition;
import pula.sys.domains.Card;

public interface CardDao extends BaseDao<Card, Long> {

	PaginationSupport<MapBean> search(CardCondition condition, int pageIndex);

	MapList loadByKeywords(String no);

	MapBean unique(Long id);

	MapBean importCards(List<Card> afs);

	Long getId4Use(String cardNo, String buildRefId);

	void takeBy(String buildRefId, String comments, Long cardId);


}
