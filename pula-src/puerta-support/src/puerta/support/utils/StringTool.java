/*
 * Created on 2004-11-6
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author tiyi 2004-11-6
 */
public class StringTool {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StringTool.class);

	public static int parseInt(String value) {
		if (StringUtils.isEmpty(value)) {
			return 0;
		}

		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static double parseDouble(String value) {
		if (StringUtils.isEmpty(value)) {
			return 0;
		}

		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static String substring(String org, int beginIndex, int endIndex) {
		int offset = 0;
		StringBuffer sbf = new StringBuffer();
		for (int i = 0; i < org.length(); i++) {
			char c = org.charAt(i);
			if (offset > endIndex)
				break;
			if (offset >= beginIndex)
				sbf.append(c);
			offset += c < 256 ? 1 : 2;
			// System.out.println(c+" "+offset);
		}
		return new String(sbf);
	}

	public static String fillZero(String str, int len) {
		String tmp = str;
		StringBuffer sb = new StringBuffer();
		for (int i = str.length(); i < len; i++) {
			sb.append("0");
		}

		sb.append(tmp);

		return sb.toString();
	}

	public static String fillChar(char c, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(String.valueOf(c));
		}

		return sb.toString();
	}

	public static String getBean(Object obj) {
		String no = null, name = null;
		try {
			no = StringUtils
					.trimToEmpty(BeanUtils.getSimpleProperty(obj, "no"));

		} catch (Exception e) {
			no = "";
			// e.printStackTrace();
		}

		try {

			name = BeanUtils.getSimpleProperty(obj, "name");
			return no + " (" + name + ")";
		} catch (Exception e) {

			// e.printStackTrace();

		}

		return no;

	}

	public static String getUser(Object obj) {
		try {
			String no = BeanUtils.getSimpleProperty(obj, "loginId");
			String name = BeanUtils.getSimpleProperty(obj, "name");
			return no + " (" + name + ")";
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "";
	}

	public static String getBeanShortName(Object obj) {
		try {
			String no = BeanUtils.getSimpleProperty(obj, "no");
			String name = BeanUtils.getSimpleProperty(obj, "shortName");
			return no + " (" + name + ")";
		} catch (Exception e) {

			e.printStackTrace();
		}

		return "";

	}

	public static boolean[] getSelectedFromString(String str) {
		StringTokenizer token = new StringTokenizer(str, ",");
		String selected = null;
		List<Boolean> l = new ArrayList<Boolean>();
		while (token.hasMoreElements()) {
			selected = (String) token.nextElement();
			if (StringUtils.isEmpty(selected))
				continue;
			boolean b = BooleanUtils.toBoolean(selected);

			Boolean bl = new Boolean(b);
			l.add(bl);
		}

		boolean[] res = new boolean[l.size()];

		for (int i = 0; i < res.length; i++) {
			res[i] = ((Boolean) l.get(i)).booleanValue();
		}
		return res;
	}

	public static String fillZero(Object ind, int i) {
		return fillZero(String.valueOf(ind), i);
	}

	public static String getMgrName(String target) {
		return target.substring(0, 1).toLowerCase() + target.substring(1);
		// return null;
	}

	public static String[][] getGridArray(String src) {
		StringTokenizer stringToken = new StringTokenizer(src, ";");
		// int count = stringToken.countTokens();

		int i = 0;
		List<String> l = new ArrayList<String>();
		while (stringToken.hasMoreTokens()) {
			String str = stringToken.nextToken();
			if (StringUtils.isEmpty(str)) {
				continue;
			}

			l.add(str);
			i++;

			// res[i++] = getArrayRow(str);
		}

		System.out.println("get grid i=" + i);

		String[][] res = new String[i][];
		i = 0;
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			res[i++] = getArrayRow(iter.next());
		}

		return res;

	}

	private static String[] getArrayRow(String str) {
		StringTokenizer stringToken = new StringTokenizer(str, ",");
		int count = stringToken.countTokens();
		String[] res = new String[count];
		int i = 0;
		while (stringToken.hasMoreTokens()) {
			String str1 = stringToken.nextToken();
			res[i++] = decodeGridCell(str1);
		}

		return res;
	}

	public static String decodeGridCell(String str1) {
		String s = StringUtils.replace(str1, "#[SEMI]", ";");
		s = StringUtils.replace(s, "#[COMMA]", ",");

		return s.trim();
	}

	/**
	 * @param b
	 * @return
	 */
	public static String getString(boolean b) {

		return BooleanUtils.toString(b, "true", "false");
	}

	/**
	 * @param backURL
	 * @param string
	 * @param domainNo
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String setURLParam(String backURL, String name, String value)
			throws UnsupportedEncodingException {

		logger.debug("name=" + name + ";value=" + value);
		String mark = "?" + name + "=";
		int i = StringUtils.indexOf(backURL, mark);
		if (i == StringUtils.INDEX_NOT_FOUND) {
			mark = "&" + name + "=";
			i = StringUtils.indexOf(backURL, mark);
		}

		if (i == StringUtils.INDEX_NOT_FOUND) {
			if (StringUtils.indexOf(backURL, "?") == StringUtils.INDEX_NOT_FOUND) {
				backURL += "?";
			} else {
				backURL += "&";
			}
			backURL += name + "=" + URLEncoder.encode(value, "UTF-8");
		} else {
			// replace exists
			int beginIndex = i;
			i = StringUtils.indexOf(backURL, "&", i + mark.length());
			String v = mark + URLEncoder.encode(value, "UTF-8");
			if (i == StringUtils.INDEX_NOT_FOUND) {
				mark = StringUtils.substring(backURL, beginIndex);
				backURL = StringUtils.replace(backURL, mark, v);
			} else {
				// find .
				backURL = StringUtils.left(backURL, beginIndex)
						+ StringUtils.right(backURL, backURL.length() - i) + v;
			}
		}

		return backURL;
	}

	public static void main(String[] args) {
		// String QJstr = "abcdefghijklmnopqrstuvwxyz";
		//
		// // String result1 = Q2B(QJstr2);
		// String result2 = ToSBC(QJstr);
		//
		// // System.out.println(result1);
		// System.out.println("\n" + result2);

		StringNumberBreak snbreak = StringTool.getLastNumber("C001");
		System.out.println(snbreak);
		System.out.println(snbreak.getNextNo());
	}

	/**
	 * @param sb
	 * @param string
	 * @param i
	 */
	public static void fillChar(StringBuilder sb, String string, int i) {
		for (int n = 0; n < i; n++) {
			sb.append(string);
		}

	}

	/**
	 * @param sb
	 * @param string
	 * @param i
	 */
	public static void fillChar(StringBuffer sb, String string, int i) {
		for (int n = 0; n < i; n++) {
			sb.append(string);
		}

	}

	/**
	 * @param str
	 * @return
	 */
	public static String toCsv(String str) {
		String tmp = str;
		if (StringUtils.contains(str, '"')) {
			tmp = StringUtils.replace(str, "\"", "\"\"");
		}
		if (StringUtils.contains(tmp, ',')) {
			return "\"" + tmp + "\"";
		}
		return tmp;
	}

	/**
	 * @param no
	 * @param strings
	 * @return
	 */
	public static boolean contains(String str, String[] array) {
		if (array == null)
			return false;
		for (String item : array) {
			if (StringUtils.equals(str, item)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param v
	 * @param i
	 * @return
	 */
	public static String fromDouble(double v, int i) {

		String mark = "0.";
		while (i > 0) {
			i--;
			mark += "#";
		}

		DecimalFormat df = new DecimalFormat(mark);
		return (df.format(v));

	}

	/**
	 * @param f
	 * @return
	 */
	public static String toMoney(double f) {
		DecimalFormat df = new DecimalFormat("0.00");

		return df.format(f);

	}

	public static String ToDBC(String input) {// 全角-->半角
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);

	}

	public static String ToSBC(String input) {// 半角-->全角
		// 半角转全角：
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127)
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	public static String encodeURL(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}

	/**
	 * @param no
	 * @return
	 */
	public static StringNumberBreak getLastNumber(String no) {
		String ret = "";
		int breakIndex = -1;
		for (int i = no.length() - 1; i >= 0; i--) {
			char chr = no.charAt(i);
			if (chr >= 48 && chr <= 57) {
				ret = chr + ret;
			} else {
				breakIndex = i;
				break;
			}
		}
		StringNumberBreak sb = new StringNumberBreak(ret, no.substring(0,
				breakIndex + 1));

		return sb;
	}

	/**
	 * @param title
	 * @return
	 */
	public static boolean isEmpty(String[] title) {
		if (title == null || title.length == 0)
			return true;
		return false;
	}

	public static boolean isMobilePhone(String str) {
		if (str == null)
			return false;
		return str.matches("^(13|15|18)\\d{9}$");
	}

	public static String fixPath(String cp, String goThere) {
		if (StringUtils.equals("/", cp)) {
			return goThere;
		}
		return cp + goThere;
	}

	public static boolean endsWith(String no, String endFix) {
		return StringUtils.indexOf(no, endFix) == (no.length() - endFix
				.length());
	}

	public static String trimDefault(String materialNo) {
		return StringUtils.trim(StringUtils.defaultString(materialNo));
	}
}
