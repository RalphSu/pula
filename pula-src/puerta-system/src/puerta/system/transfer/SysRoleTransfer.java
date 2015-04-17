/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: ActorRoleTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import java.util.List;

import org.jdom.Element;

import puerta.support.utils.WxlSugar;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.AppField;
import puerta.system.po.Purview;
import puerta.system.po.SysRole;

/**
 * 
 * @author tiyi
 * 
 */
public class SysRoleTransfer implements JDOMTransfer<SysRole> {

	private static final String ELEMENT_NAME = "actorRole";
	private static final String ELEMENT_NAME_PURVIEW = "purview";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(SysRole m) {
		Element ele = new Element(ELEMENT_NAME);
		ele.setAttribute("no", m.getNo());
		ele.setAttribute("name", m.getName());
		ele.setAttribute("appFieldNo", String.valueOf(m.getAppField().getNo()));

		// purviews
		for (Purview p : m.getPurviews()) {
			Element eleP = new Element(ELEMENT_NAME_PURVIEW);
			eleP.setAttribute("no", p.getNo());
			ele.addContent(eleP);
		}
		return ele;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public SysRole getObject(Element element) {
		SysRole m = new SysRole();

		m.setNo(element.getAttributeValue("no"));
		m.setName(element.getAttributeValue("name"));
		AppField as = new AppField();
		as.setNo(element.getAttributeValue("appFieldNo"));
		m.setAppField(as);

		// lists
		List<Purview> ps = WxlSugar.newArrayList();
		for (Object obj : element.getChildren()) {
			Element pe = (Element) obj;
			String pno = pe.getAttributeValue("no");
			Purview p = new Purview();
			p.setNo(pno);
			ps.add(p);
		}
		m.setPurviews(ps);

		return m;
	}

	public String getElementName() {
		return ELEMENT_NAME;
	}
}
