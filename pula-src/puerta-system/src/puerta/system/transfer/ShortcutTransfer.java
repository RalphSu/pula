/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: ShortcutTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.Purview;
import puerta.system.po.Shortcut;

/**
 * 
 * @author tiyi
 * 
 */
public class ShortcutTransfer implements JDOMTransfer<Shortcut> {

	private static final String ELEMENT_NAME = "shortcut";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(Shortcut r) {
		Element shortcutElement = new Element(ELEMENT_NAME);
		shortcutElement.setAttribute("no", r.getNo());
		shortcutElement.setAttribute("name", r.getName());
		if (r.getPurview() != null)
			shortcutElement.setAttribute("purviewNo", r.getPurview().getNo());
		shortcutElement.setAttribute("indexNo", String.valueOf(r.getIndexNo()));
		shortcutElement.setAttribute("url", r.getUrl());
		return shortcutElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public Shortcut getObject(Element element) {
		Shortcut r = new Shortcut();
		r.setNo(element.getAttributeValue("no"));
		r.setName(element.getAttributeValue("name"));
		r.setUrl(element.getAttributeValue("url"));
		r.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		r.setPurview(new Purview());
		r.getPurview().setNo(element.getAttributeValue("purviewNo"));
		return r;
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
