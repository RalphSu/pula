package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.AppFieldCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.AppField;
import puerta.system.vo.AppFieldData;

public interface AppFieldDao extends BaseDao<AppField, String>,
		IWxlPluginSetting<AppField> {

	AppField save(AppField create);

	Map<String, AppFieldData> loadPathMap();

	List<AppField> loadByIndexNo();

	AppField update(AppField vo);

	PaginationSupport<AppField> search(AppFieldCondition condition, int pageIndex);
}
