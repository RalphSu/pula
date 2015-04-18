/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: PurviewTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package pula.sys.transfers;

import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import pula.sys.domains.SysUser;

/**
 * 
 * @author tiyi
 * 
 */
public class SysUserTransfer implements JDOMTransfer<SysUser> {

	private static final String ELEMENT_NAME = "sysUser";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(SysUser p) {

		Element adminElement = new Element(ELEMENT_NAME);
		adminElement.setAttribute("loginId", p.getLoginId());
		adminElement.setAttribute("name", p.getName());

		// adminElement.setAttribute("roleNo", p.getRole().getNo());

		return adminElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public SysUser getObject(Element element) {
		SysUser p = new SysUser();

		p.setLoginId(element.getAttributeValue("loginId"));
		p.setName(element.getAttributeValue("name"));
		// p.setRole(new SysRole());
		// p.getRole().setNo(element.getAttributeValue("roleNo"));

		// p.sett
		return p;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
