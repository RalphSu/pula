/*
 * Created on 2005-7-8
 *$Id: MappedAction.java,v 1.2 2006/12/20 13:38:00 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.po;

/**
 * action 基础 记录系统的所有Action 如果有方法的权限限制，那么细分到方法
 * 
 * @author tiyi
 */
public class RequestUri {

	private String id;
	private String uri;
	// 权限关联数量
	private int assignedCount;

	private boolean onlineCheck, purviewCheck;

	public boolean isOnlineCheck() {
		return onlineCheck;
	}

	public void setOnlineCheck(boolean onlineCheck) {
		this.onlineCheck = onlineCheck;
	}

	public boolean isPurviewCheck() {
		return purviewCheck;
	}

	public void setPurviewCheck(boolean purviewCheck) {
		this.purviewCheck = purviewCheck;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public int getAssignedCount() {
		return assignedCount;
	}

	public void setAssignedCount(int assignedCount) {
		this.assignedCount = assignedCount;
	}

	public static RequestUri create(String id2) {
		RequestUri i = new RequestUri();
		i.id = id2;
		return i;
	}
}
