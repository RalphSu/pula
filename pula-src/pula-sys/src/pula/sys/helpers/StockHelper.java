package pula.sys.helpers;

import java.util.Map;

import puerta.support.utils.WxlSugar;
import pula.sys.domains.Stock;
import pula.sys.domains.StockLog;

public class StockHelper {

	public static Map<String, Stock> merge(StockLog... logs) {
		Map<String, Stock> map = WxlSugar.newLinkedHashMap();

		for (StockLog item : logs) {

			String key = item.getMaterial().getId() + ":"
					+ item.getBranch().getId();

			if (map.containsKey(key)) {
				Stock stock = map.get(key);
				// 累积
				stock.setQuantity(stock.getQuantity() + item.getQuantity());

			} else {

				Stock stock = new Stock();
				stock.setMaterial(item.getMaterial());
				stock.setQuantity(item.getQuantity() * item.getType());
				stock.setBranch(item.getBranch());

				map.put(key, stock);
			}

		}

		return map;

	}
	// public static Map<String, Stock> merge(StockIn stockIn, StockIn out) {
	// Map<String, Stock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (stockIn != null) {
	// for (StockInItem item : stockIn.getItems()) {
	// Stock stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity());
	// stock.setSpace(item.getSpace());
	// stock.setWarehouse(stockIn.getWarehouse());
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// map.put(key, stock);
	// }
	// }
	//
	// if (out != null) {
	// // 反向
	// for (StockInItem item : out.getItems()) {
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// if (map.containsKey(key)) {
	// Stock stock = map.get(key);
	// // 反向，所以扣除
	// stock.setQuantity(stock.getQuantity() - item.getQuantity());
	// } else {
	//
	// Stock stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity() * -1);
	// stock.setSpace(item.getSpace());
	// stock.setWarehouse(out.getWarehouse());
	// map.put(key, stock);
	// }
	// }
	// }
	//
	// return map;
	//
	// }
	//
	// public static Map<String, Stock> merge(StockOut stockOut, StockOut out) {
	// Map<String, Stock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (stockOut != null) {
	// for (StockOutItem item : stockOut.getItems()) {
	// Stock stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity() * -1);
	// stock.setSpace(item.getSpace());
	// stock.setWarehouse(stockOut.getWarehouse());
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// map.put(key, stock);
	// }
	// }
	//
	// if (out != null) {
	// // 反向
	// for (StockOutItem item : out.getItems()) {
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// if (map.containsKey(key)) {
	// Stock stock = map.get(key);
	// // 反向，所以扣除
	// stock.setQuantity(stock.getQuantity() - item.getQuantity());
	// } else {
	//
	// Stock stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity());
	// stock.setSpace(item.getSpace());
	// stock.setWarehouse(out.getWarehouse());
	// map.put(key, stock);
	// }
	// }
	// }
	//
	// return map;
	// }
	//
	// public static Map<String, Stock> merge(TransferOrder transOrder,
	// TransferOrder out) {
	// Map<String, Stock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (transOrder != null) {
	// for (TransferOrderItem item : transOrder.getItems()) {
	// Stock stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity() * -1);
	// stock.setSpace(item.getFromSpace());
	// stock.setWarehouse(transOrder.getFromWarehouse());
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getFromSpace().getId();
	// map.put(key, stock);
	//
	// Stock stockIn = new Stock();
	// stockIn.setMaterial(item.getMaterial());
	// stockIn.setQuantity(item.getQuantity());
	// stockIn.setSpace(item.getToSpace());
	// stockIn.setWarehouse(transOrder.getToWarehouse());
	//
	// key = item.getMaterial().getId() + ":"
	// + item.getToSpace().getId();
	//
	// map.put(key, stockIn);
	// }
	// }
	//
	// if (out != null) {
	// // 反向
	// for (TransferOrderItem item : out.getItems()) {
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getFromSpace().getId();
	// Stock stock = findOrPut(map, key, item);
	// stock.setSpace(item.getFromSpace());
	// stock.setWarehouse(out.getFromWarehouse());
	// stock.setQuantity(stock.getQuantity() + item.getQuantity());
	//
	// // 反过来
	// key = item.getMaterial().getId() + ":"
	// + item.getToSpace().getId();
	// stock = findOrPut(map, key, item);
	// stock.setSpace(item.getToSpace());
	// stock.setWarehouse(out.getToWarehouse());
	// stock.setQuantity(stock.getQuantity() - item.getQuantity());
	// }
	// }
	//
	// return map;
	// }
	//
	// private static Stock findOrPut(Map<String, Stock> map, String key,
	// TransferOrderItem item) {
	// Stock stock = null;
	// if (map.containsKey(key)) {
	// stock = map.get(key);
	//
	// } else {
	//
	// stock = new Stock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(0); // 然后面自己加
	// map.put(key, stock);
	// }
	// return stock;
	// }
}
