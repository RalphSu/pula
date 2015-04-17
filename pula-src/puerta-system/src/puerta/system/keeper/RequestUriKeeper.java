/**
 * Created on 2009-1-6 06:04:35
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.keeper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import puerta.system.dao.RequestUriDao;
import puerta.system.intfs.IWxlPluginKeeper;
import puerta.system.po.RequestUri;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class RequestUriKeeper implements IWxlPluginKeeper {

	private Map<String, RequestUri> map = new HashMap<String, RequestUri>();

	@Resource
	private RequestUriDao requestUriDao;

	// private byte[] lock = new byte[0];

	public void reload() {
		List<RequestUri> actions = requestUriDao.loadAll();

		map.clear();

		for (RequestUri ma : actions) {

			map.put(ma.getUri(), ma);

		}
	}

	public RequestUri getRequestUri(String uri) {
		return map.get(uri);
	}
}
