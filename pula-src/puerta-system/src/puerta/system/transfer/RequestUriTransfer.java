/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: MappedActionTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.BooleanUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.RequestUri;

/**
 * 
 * @author tiyi
 * 
 */
public class RequestUriTransfer implements JDOMTransfer<RequestUri> {

	public static final String ELEMENT_NAME = "requestUri";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(RequestUri m) {
		Element mappedActionElement = new Element(ELEMENT_NAME);
		mappedActionElement.setAttribute("uri", m.getUri());
		mappedActionElement.setAttribute("purviewCheck",
				BooleanUtils.toString(m.isPurviewCheck(), "true", "false"));
		mappedActionElement.setAttribute("onlineCheck",
				BooleanUtils.toString(m.isOnlineCheck(), "true", "false"));

		return mappedActionElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public RequestUri getObject(Element element) {
		RequestUri ma = new RequestUri();
		ma.setUri(element.getAttributeValue("uri"));
		ma.setPurviewCheck(BooleanUtils.toBoolean(element
				.getAttributeValue("purviewCheck")));
		ma.setOnlineCheck(BooleanUtils.toBoolean(element
				.getAttributeValue("onlineCheck")));

		return ma;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.groundwork.platform.setting.JDOMTransfer#getElementName()
	 */
	public String getElementName() {
		return ELEMENT_NAME;
	}

}
