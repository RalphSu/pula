/*
 * Created on 2005-3-15
 *$Id: RandomTool.java,v 1.1 2006/12/05 09:33:37 tiyi Exp $
 * Electric Potential Studio (2003-2005)
 * (c) Kingting.com 
 */
package puerta.support.utils;

import org.apache.commons.lang.math.RandomUtils;

/**
 * @author ibm 2005-3-15 10:42:31
 */
public class RandomTool {

	public static String getRandomString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int n = new Double(Math.random() * 26 + 65).intValue();
			sb.append((char) n);
		}

		return sb.toString();
	}

	public static String getRandomInt(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int intRnd = RandomUtils.nextInt(10);
			sb.append(intRnd);
		}
		return sb.toString();
	}
}
