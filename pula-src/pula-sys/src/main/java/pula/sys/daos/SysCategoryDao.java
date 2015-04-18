package pula.sys.daos;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.conditions.SysCategoryCondition;
import pula.sys.domains.SysCategory;

public interface SysCategoryDao extends BaseDao<SysCategory, String> {
	SysCategory save(SysCategory rt);

	SysCategory update(SysCategory rt);

	PaginationSupport<SysCategory> search(SysCategoryCondition condition,
			int pageNo);

	List<SysCategory> loadByNo();

	List<SysCategory> loadByTree();

	List<SysCategory> getUnder(String information);

	List<SysCategory> get();

	Map<String, SysCategory> fetchId(String... no);

	String getName(String no);

	void registerSetting();

	List<SysCategory> getChildren(String parentNo, String orderBy);

	List<SysCategory> loadByKeywords(String id, String no);

	SysCategory fetchRefByName(String groupToName, String underNo);

	Map<String, MapBean> map(String materialType);

	List<SysCategory> getUnderById(String id);

}
