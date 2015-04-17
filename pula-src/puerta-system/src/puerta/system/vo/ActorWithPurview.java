/**
 * Created on 2008-12-18 12:14:45
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.vo;


/**
 * 
 * @author tiyi
 * 
 */
public class ActorWithPurview<T> {

	private T actor;
	private String menu;

	/**
	 * @param ad
	 * @param menu2
	 */
	public ActorWithPurview(T ad, String menu2) {
		this.actor = ad;
		this.menu = menu2;
	}

	public T getActor() {
		return actor;
	}

	public void setActor(T actor) {
		this.actor = actor;
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

}
