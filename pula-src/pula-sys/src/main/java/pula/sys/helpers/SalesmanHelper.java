package pula.sys.helpers;

import java.util.Map;
import java.util.TreeMap;

import puerta.support.utils.WxlSugar;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;

public class SalesmanHelper {

	public static MapList collect(Map<String, MapBean> orders,
			Map<String, MapBean> chargebacks) {

		Map<String, MapBean> ml = new TreeMap<String, MapBean>();

		_collect(orders, ml);
		_collect(chargebacks, ml);

		return new MapList(ml.values());

	}

	private static void _collect(Map<String, MapBean> orders,
			Map<String, MapBean> ml) {
		for (Map.Entry<String, MapBean> e : orders.entrySet()) {

			MapBean mb = e.getValue();

			String no = e.getKey();
			String name = mb.string("salesmanName");

			if (ml.containsKey(no)) {
				continue;
			}

			ml.put(no, MapBean.map("name", name).add("no", no));

		}
	}

	public static Map<String, MapBean> merge(Map<String, MapBean> orders,
			Map<String, MapBean> chargebacks) {

		Map<String, MapBean> r = WxlSugar.newHashMap();
		_merge(orders, r);
		_merge(chargebacks, r);

		return r;

	}

	private static void _merge(Map<String, MapBean> orders,
			Map<String, MapBean> ml) {
		for (Map.Entry<String, MapBean> e : orders.entrySet()) {

			MapBean mb = e.getValue();

			String no = e.getKey();

			MapBean old = null;
			if (ml.containsKey(no)) {
				old = ml.get(no);
				//merge
				old.putAll(mb);
			} else {
				old = mb;
				ml.put(no, old);
			}
		}

	}
}
