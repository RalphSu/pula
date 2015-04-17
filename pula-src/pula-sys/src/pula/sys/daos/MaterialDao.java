package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.MaterialCondition;
import pula.sys.domains.Material;

public interface MaterialDao extends BaseDao<Material, Long> {

	PaginationSupport<Material> search(MaterialCondition condition,
			int pageIndex);

	Material save(Material cc);

	Material update(Material cc);

	List<Material> loadByKeywords(String no);

	Material fetchRef(String name, String spec);

	List<Material> loadSuggest(String q, int i);

	Long getIdByNo(String materialNo);

	MapBean getMetaData(Long key);

	MapBean mapBeanByNo(String no);

	void saveOrUpdate(Material material);

	MapBean unique(Long id);

	PaginationSupport<MapBean> searchMapBean(MaterialCondition condition,
			int pageIndex);

	// Material saveStandard(PartForm af);

}
