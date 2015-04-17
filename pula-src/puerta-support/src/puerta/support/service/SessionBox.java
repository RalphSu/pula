package puerta.support.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SessionBox implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8215749371029013169L;

	// flags for commit
	private long modified, lastModified;
	private boolean expired;

	public SessionBox() {
		modified = 0;
		modified();
	}

	public boolean committable() {

		if (lastModified > modified) {
			return true;
		}
		return false;
	}

	public void commited() {
		this.modified = this.lastModified;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
		this.lastModified = System.currentTimeMillis();
	}

	// session props
	String id, loginId, name, purviewActorId, menu;
	Map<String, Object> props = new HashMap<String, Object>();

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public String getPurviewActorId() {
		return purviewActorId;
	}

	public void setPurviewActorId(String purviewActorId) {
		this.purviewActorId = purviewActorId;
	}

	public String getId() {
		return id;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
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

	public Map<String, Object> getProps() {
		return props;
	}

	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void modified() {
		this.lastModified = System.currentTimeMillis();
	}

	public static SessionBox create(String id2) {
		SessionBox sb = new SessionBox();
		sb.setId(id2);
		return sb;
	}

	public Object getProps(String key) {
		return this.props.get(key);
	}

	@Override
	public String toString() {
		return loginId + " : " + name;
	}
}
