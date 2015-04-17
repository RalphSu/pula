/**
 * Created on 2008-12-18 12:21:49
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.dao;

import puerta.support.dao.IWxlActor;
import puerta.support.dao.LoggablePo;
import puerta.support.mls.Mls;

/**
 * 
 * @author tiyi
 * 
 */
public interface LoggerDao {

	/**
	 * @param string
	 * @param bean
	 */
	void doLog(String string, String bean);

	/**
	 * @param string
	 */
	void doLog(String string);

	/**
	 * @param string
	 * @param insider
	 */
	void doLog(String event, IWxlActor user);

	/**
	 * @param event
	 * @param extendInfo
	 * @param user
	 */
	void doLog(String event, String extendInfo, IWxlActor user);

	void doLog(String event, LoggablePo data);

	Mls getMls();

}
