/**
 * Created on 2008-3-28 08:04:05
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.dao;

import java.util.Calendar;

/**
 * 
 * @author tiyi
 * 
 */
public interface IWxlLogger {

	// void init(SessionService sessionMgr);

	void setActorId(String actorId);

	void setEvent(String event);

	void setExtendInfo(String extendInfo);

	void setEventTime(Calendar cal);

	void setIp(String ip);

	// void setLog(String event,String extendInfo,String actorId,)

}
