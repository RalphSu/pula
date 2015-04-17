/**
 * Created on 2007-1-19 02:51:50
 *
 * DiagCN.COM 2004-2006
 * $Id: InsiderPurview.java,v 1.1 2007/01/19 07:02:12 tiyi Exp $
 */
package puerta.system.po;

/**
 * 
 * @author tiyi
 * 
 */
public class ActorPurview {

	private String id;

	private String actorId;

	private Purview purview;

	private Integer dataFrom;

	public Integer getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(Integer dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Purview getPurview() {
		return purview;
	}

	public void setPurview(Purview purview) {
		this.purview = purview;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	/**
	 * @param pid
	 * @param aid
	 * @return
	 */
	public static ActorPurview create(String pid, String aid) {
		ActorPurview ap = new ActorPurview();
		ap.actorId = aid;
		ap.purview = new Purview();
		ap.purview.setId(pid);
		return ap;
	}

}
