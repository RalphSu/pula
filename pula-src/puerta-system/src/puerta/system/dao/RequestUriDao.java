package puerta.system.dao;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.RequestUriCondition;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.RequestUri;

public interface RequestUriDao extends BaseDao<RequestUri, String>,
		IWxlPluginSetting<RequestUri> {

	RequestUri get(RequestUri url);

	PaginationSupport<RequestUri> search(RequestUriCondition condition, int p);

	void doImport(List<RequestUri> uris);

	RequestUri save(RequestUri uri);

	List<RequestUri> getAll(String prefix);

	RequestUri get(String uri);

	void doOnlineCheck(String[] id, boolean onlineCheck);

	void doPurviewCheck(String[] id, boolean purviewCheck);

	RequestUri update(RequestUri uri);

	List<RequestUri> getPurviewCheck(String path);

	void doImportLocal(List<RequestUri> uris);

}
