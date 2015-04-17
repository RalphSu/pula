/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: VendeeTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.ParameterPage;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterPageTransfer implements JDOMTransfer<ParameterPage> {

	private static final String ELEMENT_NAME = "parameterPage";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(ParameterPage r) {
		if (r.getAppField() == null) {
			return null;
		}
		Element rpElement = new Element(ELEMENT_NAME);
		rpElement.setAttribute("no", StringUtils.defaultString(r.getNo()));
		rpElement.setAttribute("name", StringUtils.defaultString(r.getName()));
		rpElement.setAttribute("indexNo", String.valueOf(r.getIndexNo()));
		rpElement.setAttribute("appFieldNo",
				String.valueOf(r.getAppField().getNo()));
		return rpElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public ParameterPage getObject(Element element) {
		ParameterPage v = new ParameterPage();
		v.setNo(element.getAttributeValue("no"));
		v.setName(element.getAttributeValue("name"));
		v.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		v.setAppField(new AppField());
		v.getAppField().setNo(element.getAttributeValue("appFieldNo"));
		return v;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
