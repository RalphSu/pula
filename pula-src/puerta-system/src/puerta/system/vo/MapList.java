package puerta.system.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.utils.WxlSugar;

public class MapList extends ArrayList<MapBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1423215444939255982L;

	// public static final MapList EMPTY = new MapList();

	public MapList(Collection<MapBean> values) {
		this.addAll(values);
	}

	public MapList() {
	}

	public static MapList create(List<Map<String, Object>> list) {
		MapList ml = new MapList();
		for (Map<String, Object> m : list) {
			MapBean mb = new MapBean();
			mb.putAll(m);
			ml.add(mb);
		}
		return ml;
	}

	public static PaginationSupport<MapBean> createPage(
			PaginationSupport<Map<String, Object>> es) {
		return new PaginationSupport<MapBean>(create(es.getItems()),
				es.getTotalCount(), es.getPageSize(), es.getStartIndex());
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> toList(List<MapBean> items, String string) {
		List<T> results = WxlSugar.newArrayList();

		for (MapBean mb : items) {
			results.add((T) mb.get(string));
		}
		return results;

	}

	@SuppressWarnings("unchecked")
	public static <T> Map<T, MapBean> toMap(List<MapBean> items, String string) {
		Map<T, MapBean> results = WxlSugar.newHashMap();

		for (MapBean mb : items) {
			results.put((T) mb.get(string), mb);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<T, MapList> toMapList(MapList items, String string) {
		Map<T, MapList> results = WxlSugar.newHashMap();

		for (MapBean mb : items) {
			T key = (T) mb.get(string);
			MapList l = null;
			if (results.containsKey(key)) {
				l = results.get(key);
			} else {
				l = new MapList();
				results.put(key, l);
			}
			l.add(mb);
		}
		return results;
	}

	public static void sort(MapList promoPriceList, String sortBy) {
		Collections.sort(promoPriceList, MapBeanComparator.create(sortBy));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> toList(String string, boolean trimNull) {
		List<T> results = WxlSugar.newArrayList();

		for (MapBean mb : this) {
			T val = (T) mb.get(string);
			if (val == null && trimNull) {
				continue;
			}
			results.add(val);
		}
		return results;
	}

	public <T> Map<T, MapBean> toMap(String string, boolean skipEmptyKey) {
		Map<T, MapBean> results = WxlSugar.newLinkedHashMap();

		for (MapBean mb : this) {
			@SuppressWarnings("unchecked")
			T k = (T) mb.get(string);
			if (k == null && skipEmptyKey) {
				continue;
			}
			results.put(k, mb);
		}
		return results;
	}

	public static <T> void merge(Map<T, MapBean>... a) {

		if (a == null || a.length <= 1) {
			return;
		}

		for (Map.Entry<T, MapBean> e : a[0].entrySet()) {

			for (int i = 1; i < a.length; i++) {
				Map<T, MapBean> b = a[i];
				if (!b.containsKey(e.getKey())) {
					continue;
				}

				e.getValue().putAll(b.get(e.getKey()));
			}

		}

	}

	public static <K> MapList collectMapList(Map<K, MapList> mapWithMapList) {
		MapList ml = new MapList();
		for (MapList e : mapWithMapList.values()) {
			ml.addAll(e);
		}
		return ml;
	}

	public static <K> MapList collect(Map<K, MapBean> mapWithMapBean) {
		MapList ml = new MapList();

		ml.addAll(mapWithMapBean.values());

		return ml;
	}

	public static void each(List<MapBean> items, YuiResultMapper<MapBean> mapper) {

		for (MapBean mb : items) {
			mapper.toMap(mb);
		}
	}

	public MapList put(MapBean f) {
		this.add(f);
		return this;
	}

	public <T> MapList eat(Map<T, MapBean> table, String key_name) {

		for (MapBean mb : this) {
			@SuppressWarnings("unchecked")
			T key = (T) mb.object(key_name);

			if (table.containsKey(key)) {
				mb.putAll(table.get(key));
			}
		}

		return this;

	}
}

class MapBeanComparator implements Comparator<MapBean> {

	private String field;

	public static MapBeanComparator create(String field) {
		MapBeanComparator c = new MapBeanComparator();
		c.field = field;
		return c;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public int compare(MapBean arg0, MapBean arg1) {
		Object obj1 = arg0.get(field);
		Object obj2 = arg1.get(field);

		if (obj1 == null && obj2 == null) {
			return 0;
		} else if (obj1 == null && obj2 != null) {
			return -1;
		} else if (obj1 != null && obj2 == null) {
			return -1;
		} else {

			Comparable c = (Comparable) obj1;
			Comparable c2 = (Comparable) obj2;

			return c.compareTo(c2);
		}

	}
}
