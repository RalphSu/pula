/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: VendeeTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.ParameterFolder;
import puerta.system.po.ParameterPage;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterFolderTransfer implements JDOMTransfer<ParameterFolder> {

	private static final String ELEMENT_NAME = "parameterFolder";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(ParameterFolder r) {
		Element rpElement = new Element(ELEMENT_NAME);
		rpElement.setAttribute("no", r.getNo());
		rpElement.setAttribute("name", r.getName());

		rpElement.setAttribute("indexNo", String.valueOf(r.getIndexNo()));

		rpElement.setAttribute("pageNo", r.getPage().getNo());

		return rpElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public ParameterFolder getObject(Element element) {
		ParameterFolder v = new ParameterFolder();
		v.setNo(element.getAttributeValue("no"));
		v.setName(element.getAttributeValue("name"));
		v.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		v.setPage(new ParameterPage());
		v.getPage().setNo(element.getAttributeValue("pageNo"));

		return v;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
