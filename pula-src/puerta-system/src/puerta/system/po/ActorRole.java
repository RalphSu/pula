/**
 * Created on 2010-11-23
 * WXL 2009
 * $Id$
 */
package puerta.system.po;

/**
 * 
 * @author tiyi
 * 
 */
public class ActorRole {
	private String id;
	private String actorId;
	private SysRole role;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public static ActorRole create(String rid, String id2) {
		ActorRole atr = new ActorRole();
		atr.role = SysRole.create(rid);
		atr.actorId = id2;
		return atr;
	}
}
