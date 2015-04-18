package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.GiftCondition;
import pula.sys.domains.Gift;

public interface GiftDao extends BaseDao<Gift, Long> {

	PaginationSupport<Gift> search(GiftCondition condition,
			int pageIndex);

	Gift save(Gift cc);

	Gift update(Gift cc);

	List<Gift> loadByKeywords(String no);

	Gift fetchRef(String name, String spec);

	List<Gift> loadSuggest(String q, int i);

	Long getIdByNo(String giftNo);

	MapBean getMetaData(Long key);

	MapBean mapBeanByNo(String no);

	void saveOrUpdate(Gift gift);

	MapBean unique(Long id);

	PaginationSupport<MapBean> searchMapBean(GiftCondition condition,
			int pageIndex);

	MapBean meta4exchange(String giftNo);

	// Gift saveStandard(PartForm af);

}
