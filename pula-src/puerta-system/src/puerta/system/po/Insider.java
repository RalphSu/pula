/**
 * Created on 2007-2-5 04:04:53
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.po;

import puerta.support.dao.IWxlActor;
import puerta.support.dao.IWxlLogger;

/**
 * 
 * @author tiyi 内部人,总控者
 */
public class Insider implements IWxlActor {

	private String id;

	private String name;

	private String loginId;

	private String password;

	private String comments;

	private boolean enabled, removed;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.IWxlActor#getActorId()
	 */
	public String getActorId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.logger.IWxlActor#getNewLogger()
	 */
	public IWxlLogger getNewLogger() {
		return new InsiderLog();
	}
}
