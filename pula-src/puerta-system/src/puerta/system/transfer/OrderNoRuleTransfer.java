/**
 * Created on 2007-1-1 09:47:36
 *
 * DiagCN.COM 2004-2006
 * $Id: OrderNoRuleTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.transfer;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Element;

import puerta.system.intfs.JDOMTransfer;
import puerta.system.po.OrderNoRule;

/**
 * 
 * @author tiyi
 * 
 */
public class OrderNoRuleTransfer implements JDOMTransfer<OrderNoRule> {

	private static final String ELEMENT_NAME = "orderNoRule";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getElement(java.lang
	 * .Object)
	 */
	public Element getElement(OrderNoRule r) {

		Element ordernoruleElement = new Element(ELEMENT_NAME);
		ordernoruleElement.setAttribute("no", r.getNo());
		ordernoruleElement.setAttribute("name", r.getName());
		ordernoruleElement.setAttribute("dateFormat", StringUtils.trimToEmpty(r
				.getDateFormat()));
		ordernoruleElement.setAttribute("prefix", StringUtils.trimToEmpty(r
				.getPrefix()));
		ordernoruleElement.setAttribute("suffix", StringUtils.trimToEmpty(r
				.getSuffix()));

		ordernoruleElement.setAttribute("noLength", String.valueOf(r
				.getNoLength()));
		ordernoruleElement.setAttribute("reCountByDay", BooleanUtils.toString(r
				.isReCountByDay(), "true", "false"));
		ordernoruleElement.setAttribute("cacheRule", String.valueOf(r
				.getCacheRule()));
		return ordernoruleElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.setting.JDOMTransfer#getObject(org.jdom.Element
	 * )
	 */
	public OrderNoRule getObject(Element element) {
		OrderNoRule r = new OrderNoRule();
		r.setDateFormat(element.getAttributeValue("dateFormat"));
		r.setNo(element.getAttributeValue("no"));
		r.setName(element.getAttributeValue("name"));
		r.setPrefix(element.getAttributeValue("prefix"));
		r.setSuffix(element.getAttributeValue("suffix"));
		r.setNoLength(NumberUtils.toInt(element.getAttributeValue("noLength")));
		r.setReCountByDay(BooleanUtils.toBoolean(element
				.getAttributeValue("reCountByDay")));
		r.setCacheRule(NumberUtils
				.toInt(element.getAttributeValue("cacheRule")));
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
