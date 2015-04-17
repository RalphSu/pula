package puerta.support.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlHelper {
	/** */
	/**
	 * 方法取自SpringSide. 去除hql的select子句，未考虑union的情况
	 */
	public static String removeSelect(String hql) {
		int beginPos = hql.toLowerCase().indexOf("from ");
		return hql.substring(beginPos);
	}

	/** */
	/**
	 * 方法取自SpringSide. 去除hql的orderby子句
	 */
	public static String removeOrders(String hql) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
