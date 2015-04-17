/**
 * Created on 2010-3-12
 * WXL 2009
 * $Id$
 */
package puerta.system.vo;

import java.io.Serializable;

import puerta.support.service.Environment;

/**
 * 
 * @author tiyi
 * 
 */
public class RollingStonePiece implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5626333187271125546L;

	private String uri;
	private String namespace;

	public String getUri() {
		return uri;
	}

	public void setUri(String actionName) {
		this.uri = actionName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @param env
	 * @return
	 */
	public static RollingStonePiece create(Environment env) {
		RollingStonePiece rsp = new RollingStonePiece();
		rsp.uri = env.getUri();
		return rsp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		return sb.toString();
	}

}
