/**
 * Created on 2008-12-19 11:23:18
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import puerta.support.service.Environment;
import puerta.support.service.SessionBox;

/**
 * 
 * @author tiyi
 * 
 */
@Service
public class SessionService {
	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(SessionService.class);

	private ThreadLocal<SessionBox> sessionBox;
	private ThreadLocal<Environment> environment;

	public SessionService() {
		// System.out.println("Service ");
	}

	public SessionBox get() {
		// UUIDHexGenerator g = new UUIDHexGenerator();
		if (this.sessionBox == null) {
			return null;
		}
		SessionBox sb = this.sessionBox.get();
		// if (sb != null && !sb.isExpired()) {
		// return sb;
		// }
		return sb;
	}

	public void set(SessionBox attribute) {
		if (sessionBox == null) {
			sessionBox = new ThreadLocal<SessionBox>();
		}
		sessionBox.set(attribute);

	}

	public void set(Environment create) {
		if (environment == null) {
			environment = new ThreadLocal<Environment>();
		}
		environment.set(create);

	}

	public Environment env() {
		if (environment == null) {
			environment = new ThreadLocal<Environment>();
		}
		return environment.get();
	}

	public String getActorId() {
		SessionBox sb = this.get();
		if (sb == null) {
			return null;
		}
		return sb.getId();
	}

	public boolean has() {
		SessionBox sb = get();
		if (sb == null) {
			return false;
		}
		return true;
	}

	public void abandon() {
		SessionBox sb = get();
		if (sb == null) {
			return;
		} else {
			sb.setExpired(true);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getProp(String key) {
		SessionBox sb = get();
		if (sb != null) {
			return (T) sb.getProps().get(key);
		}
		return null;
	}
}
