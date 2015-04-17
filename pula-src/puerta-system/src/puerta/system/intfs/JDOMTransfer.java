/**
 * Created on 2007-1-1 08:48:11
 *
 * DiagCN.COM 2004-2006
 * $Id: JDOMTransfer.java,v 1.1 2007/01/03 13:47:14 tiyi Exp $
 */
package puerta.system.intfs;

import org.jdom.Element;

/**
 * 
 * @author tiyi
 * 
 */
public interface JDOMTransfer<T> {

	Element getElement(T obj);

	T getObject(Element element);

	String getElementName();
}
