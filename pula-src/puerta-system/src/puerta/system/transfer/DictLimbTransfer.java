/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: DictTransfer.java,v 1.2 2007/01/09 12:44:21 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.DictLimb;

/**
 * 
 * @author tiyi
 * 
 */
public class DictLimbTransfer implements JDOMTransfer<DictLimb> {

	private static final String ELEMENT_NAME = "dictLimb";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(DictLimb m) {

		Element dictElement = new Element(ELEMENT_NAME);
		dictElement.setAttribute("no", m.getNo());
		dictElement.setAttribute("name", m.getName());
		dictElement.setAttribute("parentNo",
				StringUtils.defaultString(m.getParentNo()));
		dictElement.setAttribute("indexNo", String.valueOf(m.getIndexNo()));
		dictElement.setAttribute("level", String.valueOf(m.getLevel()));
		dictElement.setAttribute("treePath",
				StringUtils.defaultString(m.getTreePath()));
		return dictElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public DictLimb getObject(Element element) {
		DictLimb d = new DictLimb();
		d.setName(element.getAttributeValue("name"));
		d.setNo(element.getAttributeValue("no"));
		d.setParentNo((element.getAttributeValue("parentNo")));
		d.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo")));
		return d;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}

}
