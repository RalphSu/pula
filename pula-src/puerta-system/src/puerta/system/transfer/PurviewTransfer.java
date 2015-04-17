/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: PurviewTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.Module;
import puerta.system.po.Purview;

/**
 * 
 * @author tiyi
 * 
 */
public class PurviewTransfer implements JDOMTransfer<Purview> {

	private static final String ELEMENT_NAME = "purview";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(Purview p) {

		Element purviewElement = new Element("purview");
		purviewElement.setAttribute("no", p.getNo());
		purviewElement.setAttribute("name", p.getName());
		purviewElement.setAttribute("indexNo", String.valueOf(p.getIndexNo()));
		purviewElement.setAttribute("menuItem", BooleanUtils
				.toStringTrueFalse(p.isMenuItem()));

		purviewElement.setAttribute("defaultURL", StringUtils.trimToEmpty(p
				.getDefaultURL()));

		if (p.getParentPurview() != null) {
			purviewElement.setAttribute("parentPurview", StringUtils
					.trimToEmpty(p.getParentPurview().getNo()));
		}

		purviewElement.setAttribute("module", StringUtils.trimToEmpty(p
				.getModule().getNo()));
		purviewElement.setAttribute("appFieldNo", String.valueOf(p
				.getAppField().getNo()));
		purviewElement.setAttribute("treePath", p.getTreePath());
		return purviewElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public Purview getObject(Element element) {
		Purview p = new Purview();
		p.setNo(element.getAttributeValue("no"));
		p.setDefaultURL(element.getAttributeValue("defaultURL"));
		p.setName(element.getAttributeValue("name"));
		p.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		p.setMenuItem(BooleanUtils.toBoolean(element
				.getAttributeValue("menuItem")));
		String v = element.getAttributeValue("parentPurview");
		if (v == null) {

		} else {
			p.setParentPurview(new Purview());
			p.getParentPurview().setNo(v);
		}

		p.setModule(new Module());
		p.getModule().setNo(element.getAttributeValue("module"));

		p.setAppField(new AppField());
		p.getAppField().setNo(
				element.getAttributeValue("appFieldNo"));

		// p.sett
		return p;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
