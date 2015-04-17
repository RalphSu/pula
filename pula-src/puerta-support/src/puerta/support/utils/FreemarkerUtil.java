package puerta.support.utils;

import java.io.Serializable;
import java.util.Map;

public class FreemarkerUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7266218519301476059L;

	@SuppressWarnings("unchecked")
	public static <E extends Serializable> Map<String, Object> hash(
			Map<E, ? extends Object> m) {

		if (m == null)
			return null;

		Map<String, Object> r = WxlSugar.newHashMap();

		for (Map.Entry<E, ?> e : m.entrySet()) {
			Object v = e.getValue();
			if (v instanceof Map) {
				v = hash((Map<E, Object>) v);
			}
			r.put(e.getKey().toString(), v);
		}

		return r;
	}

	@SuppressWarnings("unchecked")
	public static <E extends Serializable> Map<String, Object> linkedHash(
			Map<E, ? extends Object> m) {

		if (m == null)
			return null;

		Map<String, Object> r = WxlSugar.newLinkedHashMap();

		for (Map.Entry<E, ?> e : m.entrySet()) {
			Object v = e.getValue();
			if (v instanceof Map) {
				v = linkedHash((Map<E, Object>) v);
			}
			r.put(e.getKey().toString(), v);
		}

		return r;
	}
}
