/**
 * Created on 2009-8-25
 * WXL 2009
 * $Id$
 */
package pula.sys.helpers;

import java.util.Map;
import java.util.TreeMap;

import puerta.support.utils.MD5;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.domains.Teacher;

/**
 * 
 * @author tiyi
 * 
 */
public class TeacherHelper {

	public static int[] status = new int[] { Teacher.ON_DUTY,
			Teacher.LEAVE_DUTY };

	public static String[] status_names = new String[] { "在职", "离职" };

	public static int[] types = new int[] { Teacher.ATTACHMENT_ICON,
			Teacher.ATTACHMENT_OTHER };

	public static String[] types_name = new String[] { "身份照", "其他" };

	public static SelectOptionList getFileTypeList(int n) {
		return SelectOption.getList(n, types, types_name);
	}

	public static String getFileTypeName(int n) {
		return SelectOption.getName(n, types, types_name);
	}

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, status, status_names);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, status, status_names);
	}

	public static int[] levels = new int[] { Teacher.LEVEL_A, Teacher.LEVLE_B,
			Teacher.LEVEL_C, Teacher.LEVEL_D };

	public static String[] levels_name = new String[] { "A", "B", "C", "D" };

	public static SelectOptionList getLevelList(int n) {
		return SelectOption.getList(n, levels, levels_name);
	}

	public static String getLevelName(int n) {
		return SelectOption.getName(n, levels, levels_name);
	}

	public static String makePassword(String password) {
		return MD5.GetMD5String("teacher@" + password);
	}

	public static String buildRefId(long id) {
		return "T:" + id;
	}

	public static String buildComments(Teacher ef) {
		return new StringBuilder().append("教师:").append(ef.getNo()).append(" ")
				.append(ef.getName()).toString();
	}

	public static MapList collect(MapList teacherList,
			Map<String, MapBean> orders, Map<String, MapBean> chargebacks,
			Map<String, MapBean> records, Map<String, MapBean> courses) {
		Map<String, MapBean> ml = new TreeMap<String, MapBean>();

		Map<String, MapBean> old = teacherList.toMap("no", true);

		ml.putAll(old);

		put(orders, ml);
		put(chargebacks, ml);
		put(records, ml);
		put(courses, ml);
		return new MapList(ml.values());
	}

	private static void put(Map<String, MapBean> chargebacks,
			Map<String, MapBean> ml) {
		for (Map.Entry<String, MapBean> e : chargebacks.entrySet()) {
			if (ml.containsKey(e.getKey())) {
				continue;
			}
			MapBean mb = e.getValue();
			ml.put(e.getKey(),
					MapBean.map("no", mb.string("teacherNo"))
							.add("name", mb.string("teacherName"))
							.add("teacherId", mb.asLong("teacherId")));
		}
	}

	public static Map<String, MapBean> merge(Map<String, MapBean> records,
			Map<String, MapBean> orders, Map<String, MapBean> chargebacks,
			Map<String, MapBean> courses) {

		Map<String, MapBean> ml = new TreeMap<String, MapBean>();

		for (Map.Entry<String, MapBean> e : records.entrySet()) {
			if (ml.containsKey(e.getKey())) {
				continue;
			}
			MapBean mb = e.getValue();
			ml.put(e.getKey(), mb);
		}

		for (Map.Entry<String, MapBean> e : orders.entrySet()) {
			MapBean old = null;
			MapBean mb = e.getValue();
			if (ml.containsKey(e.getKey())) {
				old = ml.get(e.getKey());
			} else {
				old = MapBean.map("no", mb.string("teacherNo"))
						.add("name", mb.string("teacherName"))
						.add("teacherId", mb.asLong("teacherId"));
				ml.put(e.getKey(), old);
			}

			old.add("orders", mb.asLong("totalOrders"));
		}
		for (Map.Entry<String, MapBean> e : chargebacks.entrySet()) {
			MapBean old = null;
			MapBean mb = e.getValue();
			if (ml.containsKey(e.getKey())) {
				old = ml.get(e.getKey());
			} else {
				old = MapBean.map("no", mb.string("teacherNo"))
						.add("name", mb.string("teacherName"))
						.add("teacherId", mb.asLong("teacherId"));
				ml.put(e.getKey(), old);
			}
			old.add("chargebacks", mb.asLong("totalBackOrders"));
		}

		for (Map.Entry<String, MapBean> e : courses.entrySet()) {
			MapBean old = null;
			MapBean mb = e.getValue();
			if (ml.containsKey(e.getKey())) {
				old = ml.get(e.getKey());
			} else {
				old = MapBean.map("no", mb.string("teacherNo"))
						.add("name", mb.string("teacherName"))
						.add("teacherId", mb.asLong("teacherId"));
				ml.put(e.getKey(), old);
			}
			old.add("courseCount", mb.asLong("courseCount"));
		}

		return ml;
	}
}
