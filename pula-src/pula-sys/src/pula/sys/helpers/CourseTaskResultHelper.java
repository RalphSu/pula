/**
 * Created on 2009-8-25
 * WXL 2009
 * $Id$
 */
package pula.sys.helpers;

import org.apache.commons.lang.StringUtils;

import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.domains.CourseTaskResult;

/**
 * 
 * @author tiyi
 * 
 */
public class CourseTaskResultHelper {

	public static int[] submitTypes = new int[] { CourseTaskResult.ST_CLIENT,
			CourseTaskResult.ST_MANUAL };

	public static String[] submitTypesName = new String[] { "客户端", "手动" };

	public static SelectOptionList getSubmitTypes(int n) {
		return SelectOption.getList(n, submitTypes, submitTypesName);
	}

	public static String getSubmitTypeName(int n) {
		return SelectOption.getName(n, submitTypes, submitTypesName);
	}

	public static MapList splitToTeacher(MapBean mb) {
		MapList ml = new MapList();
		MapBean tmb = extract(mb, "master");
		if (tmb != null) {
			ml.add(tmb);
		}

		tmb = extract(mb, "assistant1");
		if (tmb != null) {
			ml.add(tmb);
		}
		tmb = extract(mb, "assistant2");
		if (tmb != null) {
			ml.add(tmb);
		}

		return ml;
	}

	private static MapBean extract(MapBean mb, String string) {
		String tno = mb.string(string + "No");
		if (StringUtils.isEmpty(tno)) {
			return null;
		}
		String tname = mb.string(string + "Name");
		Long tid = mb.asLong(string + "Id");

		return MapBean.map("teacherId", tid).add("teacherNo", tno)
				.add("teacherName", tname)
				.add("courseCount", mb.asLong("courseCount"));
	}

}
