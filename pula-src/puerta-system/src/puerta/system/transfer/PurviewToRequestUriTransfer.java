/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: PurviewToMappedActionTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Purview;
import puerta.system.po.PurviewToRequestUri;
import puerta.system.po.RequestUri;

/**
 * 
 * @author tiyi
 * 
 */
public class PurviewToRequestUriTransfer implements JDOMTransfer<PurviewToRequestUri> {

	private static final String ELEMENT_NAME = "purviewToUri";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(PurviewToRequestUri r) {

		if (r.getPurview() == null)
			return null;
		Element rpElement = new Element(ELEMENT_NAME);
		rpElement.setAttribute("purviewNo", r.getPurview().getNo());
		rpElement.setAttribute("uri", r.getRequestUri().getUri());

		return rpElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public PurviewToRequestUri getObject(Element element) {
		PurviewToRequestUri r = new PurviewToRequestUri();
		r.setRequestUri(new RequestUri());
		r.setPurview(new Purview());

		r.getPurview().setNo(element.getAttributeValue("purviewNo"));
		r.getRequestUri().setUri(element.getAttributeValue("uri"));

		return r;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}

}
