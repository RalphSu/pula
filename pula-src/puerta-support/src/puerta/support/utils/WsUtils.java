/**
 * Created on 2009-4-24
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * @author tiyi
 * 
 */
public class WsUtils {

	public static XMLGregorianCalendar getXml(Calendar cal) {
		if (cal == null)
			return null;
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(
					(GregorianCalendar) cal);
		} catch (DatatypeConfigurationException e) {

			e.printStackTrace();

			return null;
		}
	}

	public static XMLGregorianCalendar getXml(String time) {
		if (time == null)
			return null;
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(time);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Calendar getCalendar(XMLGregorianCalendar xcc) {
		if (xcc == null) {
			return null;
		}
		return xcc.toGregorianCalendar();
	}
}
