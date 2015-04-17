/**
 * Created on 2007-6-3 08:57:17
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.po;

/**
 * 参与者的快捷方式
 * 
 * @author tiyi
 * 
 */
public class ActorShortcut {

	public static final int TYPE_INSIDER = 1001;

	public static final int TYPE_ADMIN = 1002;

	public static final int TYPE_USER = 1003;

	private String id;

	private Shortcut shortcut;

	private String actorId;

	private int indexNo;

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Shortcut getShortcut() {
		return shortcut;
	}

	public void setShortcut(Shortcut shortcut) {
		this.shortcut = shortcut;
	}

}
