package puerta.system.tests;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanWrapperImpl;

public class TestBeanUtils {

	public static void main(String[] args) {

		Map<String, String[]> map = new HashMap<String, String[]>();
		// map.put("employee.id", new String[] { "id_id" });
		// map.put("employee.company.name", new String[] { "農工商" });
		// map.put("ec", new String[] { "100" });

		map.put("employee.salary", new String[] { "7812.12" });

		// Employee employee = new Employee();
		// BeanWrapper bw = new BeanWrapperImpl_(employee);
		// bw.setAutoGrowNestedPaths(true);
		// bw.setPropertyValue("id", new String[] { "cc", "dd" });
		// bw.setPropertyValue("checkList", new String[] { "ok", "很好" });
		// bw.setPropertyValue("company.name", new String[] { "很好" });
		// bw.setPropertyValue("company.employeeCount", new String[] { "10" });
		// bw.setPropertyValue("onTrip", new String[] { "0" });
		// bw.setPropertyValue("daysOfRest", new String[] { "1", "10", "21" });
		// bw.setPropertyValue("salary", new String[] { "7812.12" });
		// Employee employee = getObject("employee", map, Employee.class);
		// System.out.println(employee);

		// int c = getObject("ec", map, Integer.class);
		// System.out.println(c);
		// System.out.println("company=" + employee.getCompany().getName());
		// System.out.println("employeeCount="
		// + employee.getCompany().getEmployeeCount());
		// System.out.println("employee.onTrip=" + employee.isOnTrip());
		// System.out.println("checkList="
		// + ArrayUtils.toString(employee.getCheckList()));
	}

	public static <T> T getObject(String paramName, Map<String, String[]> map,
			Class<T> clazz) {
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (obj == null) {
			return obj;
		}
		BeanWrapperImpl bw = new BeanWrapperImpl(obj);
		bw.setAutoGrowNestedPaths(true);
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(paramName)) {
				String target = key.substring(paramName.length());
				if (target.length() == 0) {
					// 直接返回值（还需要根据类型匹配一下）
					return obj;
				} else {
					target = target.substring(1);
					bw.setPropertyValue(target, entry.getValue());
				}
			}
		}

		return obj;
	}
}
