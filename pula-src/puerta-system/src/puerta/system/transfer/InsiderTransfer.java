/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: PurviewTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.StringUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Insider;

/**
 * 
 * @author tiyi
 * 
 */
public class InsiderTransfer implements JDOMTransfer<Insider> {

	private static final String ELEMENT_NAME = "insider";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(Insider p) {

		Element adminElement = new Element(ELEMENT_NAME);
		adminElement.setAttribute("loginId", p.getLoginId());
		adminElement.setAttribute("name", p.getName());
		adminElement.setAttribute("comments",
				StringUtils.defaultString(p.getComments()));

		return adminElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public Insider getObject(Element element) {
		Insider p = new Insider();
		p.setLoginId(element.getAttributeValue("loginId"));
		p.setName(element.getAttributeValue("name"));
		p.setComments(element.getAttributeValue("comments"));

		// p.sett
		return p;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
