/*
 * Created on 2005-7-8
 *$Id: PurviewToMappedAction.java,v 1.1 2006/12/05 09:33:37 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.po;

/**
 * @author tiyi 2005-7-8 23:28:45
 */
public class PurviewToRequestUri {

	private String id;
	private Purview purview;
	private RequestUri requestUri;

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	public Purview getPurview() {
		return purview;
	}

	public void setPurview(Purview purview) {
		this.purview = purview;
	}

	public RequestUri getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(RequestUri requestUri) {
		this.requestUri = requestUri;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static PurviewToRequestUri create(String s, RequestUri u) {
		PurviewToRequestUri u1 = new PurviewToRequestUri();
		u1.setPurview(Purview.createByNo(s));
		u1.setRequestUri(u);
		return u1 ;
	}

}
