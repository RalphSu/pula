/**
 * Created on 2010-1-24
 * WXL 2009
 * $Id$
 */
package puerta.system.vo;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author tiyi
 * 
 */
public class PurviewHolder {
	private Map<String, String> purviews = null;
	private String role = "";

	public Map<String, String> getPurviews() {
		return purviews;
	}

	public void setPurviews(Map<String, String> purviews) {
		this.purviews = purviews;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @param ps
	 */
	public PurviewHolder(Map<String, String> ps) {
		this.purviews = ps;
	}

	public boolean got(String key) {
		return purviews.containsKey(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> e : purviews.entrySet()) {
			sb.append(e.getKey()).append("=").append(e.getValue()).append("\n");
		}
		return sb.toString();
	}
}
