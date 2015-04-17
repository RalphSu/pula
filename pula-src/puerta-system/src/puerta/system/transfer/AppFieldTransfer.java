/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: ModuleTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;

/**
 * 
 * @author tiyi
 * 
 */
public class AppFieldTransfer implements JDOMTransfer<AppField> {

	private static final String ELEMENT_NAME = "appField";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(AppField m) {

		Element moduleElement = new Element(ELEMENT_NAME);
		moduleElement.setAttribute("no", m.getNo());
		moduleElement.setAttribute("name", m.getName());
		moduleElement.setAttribute("indexNo", String.valueOf(m.getIndexNo()));
		moduleElement.setAttribute("comments",
				StringUtils.trimToEmpty(m.getComments()));

		moduleElement
				.setAttribute("path", StringUtils.trimToEmpty(m.getPath()));
		// moduleElement.setAttribute("accountPurviewPo",
		// m.getAccountPurviewPo());
		return moduleElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public AppField getObject(Element element) {
		AppField m = new AppField();
		m.setComments(element.getAttributeValue("comments"));
		m.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		m.setNo(element.getAttributeValue("no"));
		m.setName(element.getAttributeValue("name"));
		// m.setType(NumberUtils.toInt(element.getAttributeValue("type")));
		m.setPath(element.getAttributeValue("path"));
		// m.setAccountPurviewPo(element.getAttributeValue("accountPurviewPo", m
		// .getAccountPurviewPo()));

		return m;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
