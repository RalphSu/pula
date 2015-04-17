package puerta.system.vo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import puerta.support.utils.WxlSugar;

public class MapBean extends HashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1262385473857890347L;
	public static final MapBean EMPTY = new MapBean();

	public String string(String string) {
		if (this.containsKey(string)) {
			return StringUtils.defaultString((String) this.get(string));
		} else {
			return StringUtils.EMPTY;
		}
	}

	public static MapBean map() {
		MapBean mb = new MapBean();
		return mb;
	}

	public static MapBean map(String k, Object v) {
		MapBean mb = new MapBean();
		return mb.add(k, v);
	}

	public MapBean add(String string, Object object) {
		this.put(string, object);
		return this;
	}

	public MapBean map(String k) {
		return (MapBean) this.get(k);
	}

	public Long asLong(String string) {
		if (this.containsKey(string)) {
			return (Long) this.get(string);
		}
		return 0L;
	}

	public Byte asByte(String string) {

		if (this.containsKey(string)) {
			return (Byte) this.get(string);
		}
		return 0;
	}

	public Boolean yes(String string) {
		if (this.containsKey(string)) {
			Object obj = this.get(string);

			if (obj instanceof Integer) {
				return ((Integer) obj > 0);
			} else {
				return (Boolean) obj;
			}
		}
		return false;
	}

	public Integer integer(String string) {

		if (this.containsKey(string)) {
			return (Integer) this.get(string);
		}
		return 0;
	}

	public Timestamp time(String string) {
		if (this.containsKey(string)) {
			return (Timestamp) this.get(string);
		}

		return new java.sql.Timestamp(0);
	}

	public static List<MapBean> list() {
		return WxlSugar.newArrayList();
	}

	public static MapBean find(List<MapBean> list, String key, Object obj) {
		for (MapBean mb : list) {
			if (mb.containsKey(key)) {
				Object v = mb.get(key);
				if (v == obj || obj.equals(v)) {
					return mb;
				}
			}
		}
		return null;
	}

	public boolean empty(String string) {
		return StringUtils.isEmpty(this.string(string));
	}

	public double asDouble(String string) {
		if (this.containsKey(string)) {
			return (Double) this.get(string);
		}
		return 0d;
	}

	public Calendar calendar(String string) {
		if (this.containsKey(string)) {
			return (Calendar) this.get(string);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <E> E object(String key) {
		return (E) this.get(key);
	}

	public int asInteger(String string) {
		if (this.containsKey(string)) {
			return (Integer) this.get(string);
		}
		return 0;
	}

	public boolean asBoolean(String string) {
		if (this.containsKey(string)) {
			Boolean b = (Boolean) this.get(string);
			if (b == null) {
				return false;
			}
			return b;
		}
		return false;
	}

	public static MapBean map(Map<String, Object> m) {
		MapBean mb = new MapBean();
		mb.putAll(m);
		return mb;
	}

	public MapBean asMapBean(String key) {
		return (MapBean) this.get(key);
	}

	public boolean isZeroOrNull(String... keys) {

		for (String k : keys) {
			boolean zero = false;
			boolean empty = false;

			if (containsKey(k)) {
				Object obj = this.get(k);
				if (obj != null) {
					zero = obj.equals(0) || obj.equals(0L);
				} else {
					empty = true;
				}
			} else {
				empty = true;
			}

			// 非空，马上跳出
			if (!(empty || zero)) {
				return false;
			}

		}

		return true;

	}
}
