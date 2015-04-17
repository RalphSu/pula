/**
 * Created on 2009-12-23
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * 
 * @author tiyi
 * 
 */
public class WxlSugar {

	@SuppressWarnings("unchecked")
	public static <K, V, E> List<E> getList(K key, Map<K, V> map) {

		if (map.containsKey(key)) {
			return (List<E>) map.get(key);
		}
		return Collections.emptyList();
	}

	/**
	 * @return
	 */
	public static <E> List<E> newArrayList() {
		return new ArrayList<E>();
	}

	public static <E> List<E> newArrayList(int size) {
		return new ArrayList<E>(size);
	}

	/**
	 * @return
	 */
	public static <K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * @return
	 */
	public static <K, V> Map<K, V> newLinkedHashMap() {
		return new LinkedHashMap<K, V>();
	}

	public static <V> Map<String, V> prefix(Map<String, V> map, String string) {
		Map<String, V> m = WxlSugar.newHashMap();
		for (Entry<String, V> e : map.entrySet()) {
			m.put(string + e.getKey(), e.getValue());
		}
		return m;
	}

	public static <K, V> List<V> asList(Map<K, V> r) {
		return new ArrayList<V>(r.values());
	}

	public static <T> void removeDuplicate(List<T> arlList) {
		HashSet<T> h = new LinkedHashSet<T>(arlList);
		arlList.clear();
		arlList.addAll(h);
	}

	public static <V> Map<String, V> sortMapByKey(Map<String, V> oriMap) {

		if (oriMap == null || oriMap.isEmpty()) {
			return null;
		}
		Map<String, V> sortedMap = new TreeMap<String, V>(
				new Comparator<String>() {
					public int compare(String str, String anotherString) {

						int len1 = str.length();
						int len2 = anotherString.length();
						int n = Math.min(len1, len2);
						char v1[] = str.toCharArray();
						char v2[] = anotherString.toCharArray();
						int k = 0;
						while (k < n) {
							char c1 = v1[k];
							char c2 = v2[k];
							if (c1 != c2) {
								return c1 - c2;
							}
							k++;
						}

						return len1 - len2;

					}
				});
		sortedMap.putAll(oriMap);
		return sortedMap;
	}

	public static <T extends Object> Object[] asObjects(T[] objId) {
		// return Arrays.asList(objId).toArray();
		Object[] result = new Object[objId.length];
		System.arraycopy(objId, 0, result, 0, objId.length);
		return result;
	}

	public static <T> List<T> asList(T data) {
		if (data == null) {
			return Collections.emptyList();
		}
		List<T> ts = WxlSugar.newArrayList();
		ts.add(data);
		return ts;
	}

	
}
