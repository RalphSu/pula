/**
 * Created on 2008-3-28 08:03:39
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.keeper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import puerta.support.dao.IWxlLogger;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class LoggerClassKeeper {

	private Map<String, Class<? extends IWxlLogger>> clsMap = new HashMap<String, Class<? extends IWxlLogger>>();

	public void addClass(String no, Class<? extends IWxlLogger> clz) {
		clsMap.put(no, clz);
	}

	/**
	 * @param ap
	 * @return
	 */
	public Class<? extends IWxlLogger> getClass(String ap) {
		return clsMap.get(ap);
	}

}
