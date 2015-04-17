package puerta.support.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;

public class BeanToMap {

	public static final BeanToMapFilter ALL = new BeanToMapFilter() {

		@Override
		public Object filter(String name, Object v) {
			return v;
		}
	};

	public static Map<String, Object> toMap(Object saForm,
			BeanToMapFilter filter) {
		Map<String, Object> m = WxlSugar.newHashMap();
		@SuppressWarnings("rawtypes")
		Map m1 = null;
		try {
			m1 = BeanUtils.describe(saForm);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		for (Object entry : m1.entrySet()) {
			@SuppressWarnings("rawtypes")
			Entry en = (Entry) entry;

			String name = en.getKey().toString();
			Object v = en.getValue();

			v = filter.filter(name, v);

			if (v == null) {
				v = "";
			}
			m.put(name, v.toString());
		}

		return m;
	}

}
