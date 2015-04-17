/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: PurviewTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package pula.sys.transfers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import pula.sys.domains.SysCategory;

/**
 * 
 * @author tiyi
 * 
 */
public class SysCategoryTransfer implements JDOMTransfer<SysCategory> {

	private static final String ELEMENT_NAME = "sysCategory";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(SysCategory p) {

		Element adminElement = new Element(ELEMENT_NAME);
		adminElement.setAttribute("no", p.getNo());
		adminElement.setAttribute("name", p.getName());
		adminElement.setAttribute("indexNo", String.valueOf(p.getIndexNo()));
		if (p.getParent() != null)
			adminElement.setAttribute("parentNo", p.getParent().getNo());
		adminElement.setAttribute("treePath",
				StringUtils.defaultString(p.getTreePath()));
		return adminElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public SysCategory getObject(Element element) {
		SysCategory p = new SysCategory();

		p.setNo(StringUtils.left(element.getAttributeValue("no"), 40));
		p.setName(element.getAttributeValue("name"));
		String pno = element.getAttributeValue("parentNo");
		p.setIndexNo(NumberUtils.toInt(element.getAttributeValue("indexNo"), 0));
		p.setEnabled(true);
		p.setParent(SysCategory.createByNo(pno));

		// p.sett
		return p;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
