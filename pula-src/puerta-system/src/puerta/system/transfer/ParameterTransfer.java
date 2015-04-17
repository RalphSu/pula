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
import puerta.system.po.Parameter;
import puerta.system.po.ParameterFolder;

/**
 * 
 * @author tiyi
 * 
 */
public class ParameterTransfer implements JDOMTransfer<Parameter> {

	private static final String ELEMENT_NAME = "parameter";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(Parameter obj) {
		Parameter r = (Parameter) obj;
		Element rpElement = new Element(ELEMENT_NAME);
		rpElement.setAttribute("no", r.getNo());
		rpElement.setAttribute("name", r.getName());

		rpElement.setAttribute("indexNo", String.valueOf(r.getIndexNo()));
		rpElement.setAttribute("paramType", String.valueOf(r.getParamType()));
		rpElement.setAttribute("value", r.getValue());
		if (r.getFolder() != null) {
			rpElement.setAttribute("folderNo", r.getFolder().getNo());
		}
		rpElement.setAttribute("mask", StringUtils.trimToEmpty(r.getMask()));

		return rpElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public Parameter getObject(Element element) {
		Parameter v = new Parameter();
		v.setNo(element.getAttributeValue("no"));
		v.setName(element.getAttributeValue("name"));
		v.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		v.setMask(element.getAttributeValue("mask"));
		v.setValue(element.getAttributeValue("value"));
		v.setFolder(new ParameterFolder());
		v.getFolder().setNo(element.getAttributeValue("folderNo"));
		v.setParamType(NumberUtils.toInt(element.getAttributeValue("paramType")));

		return v;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
