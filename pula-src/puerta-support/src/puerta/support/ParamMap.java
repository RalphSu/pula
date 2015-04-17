package puerta.support;

import java.util.HashMap;
import java.util.Map;

public class ParamMap {

	private Map<String, Object> _map = null;

	private ParamMap() {
	}

	public static ParamMap create(String key, Object obj) {
		ParamMap m = new ParamMap();
		m._map = new HashMap<String, Object>();
		m._map.put(key, obj);
		return m;
	}

	public ParamMap set(String key, Object obj) {
		if (this._map.containsKey(key)) {
			throw new RuntimeException("key is exists:" + key);
		} else {
			this._map.put(key, obj);
		}
		return this;
	}

	public Map<String, Object> map() {
		return this._map;
	}
}
