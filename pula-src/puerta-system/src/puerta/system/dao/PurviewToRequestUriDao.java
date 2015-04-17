/**
 * Created on 2009-8-7
 * WXL 2009
 * $Id$
 */
package puerta.system.dao;

import java.util.List;
import java.util.Map;

import puerta.support.dao.BaseDao;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.PurviewToRequestUri;
import puerta.system.po.RequestUri;

/**
 * 
 * @author tiyi
 * 
 */
public interface PurviewToRequestUriDao extends
		BaseDao<PurviewToRequestUri, String>,
		IWxlPluginSetting<PurviewToRequestUri> {

	List<RequestUri> getAssigned(String id);

	void save(String purviewId, String[] uriId, boolean append);

	Map<String, Integer> getAssigned();

	void clear(String purviewId);

	boolean canAccess(RequestUri ma, String id);

	void doImportLocal(List<PurviewToRequestUri> ptrs);

}
