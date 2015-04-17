/**
 * Created on 2007-6-6 09:52:18
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.keeper;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import puerta.system.flag.OrderNoMutex;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class OrderNoMutexKeeper {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(OrderNoMutexKeeper.class);

	public static Map<String, OrderNoMutex> mutexs = new HashMap<String, OrderNoMutex>();

	public synchronized void register(OrderNoMutex mutex) {
		logger.debug("register orderno mutex=" + mutex.getNo() + " instance="
				+ mutex.toString());
		mutexs.put(mutex.getNo(), mutex);
	}

	public OrderNoMutex getKey(String key) {
		return mutexs.get(key);
	}

}
