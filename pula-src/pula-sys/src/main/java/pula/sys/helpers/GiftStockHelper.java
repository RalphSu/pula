package pula.sys.helpers;

import java.util.Map;

import puerta.support.utils.WxlSugar;
import pula.sys.domains.GiftStock;
import pula.sys.domains.GiftStockLog;

public class GiftStockHelper {

	public static Map<String, GiftStock> merge(GiftStockLog... logs) {
		Map<String, GiftStock> map = WxlSugar.newLinkedHashMap();

		for (GiftStockLog item : logs) {

			String key = item.getGift().getId() + ":"
					+ item.getBranch().getId();

			if (map.containsKey(key)) {
				GiftStock stock = map.get(key);
				// 累积
				stock.setQuantity(stock.getQuantity() + item.getQuantity());

			} else {

				GiftStock stock = new GiftStock();
				stock.setGift(item.getGift());
				stock.setQuantity(item.getQuantity() * item.getType());
				stock.setBranch(item.getBranch());

				map.put(key, stock);
			}

		}

		return map;

	}
	// public static Map<String, GiftStock> merge(GiftStockIn stockIn,
	// GiftStockIn out) {
	// Map<String, GiftStock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (stockIn != null) {
	// for (GiftStockInItem item : stockIn.getItems()) {
	// GiftStock stock = new GiftStock();
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
	// for (GiftStockInItem item : out.getItems()) {
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// if (map.containsKey(key)) {
	// GiftStock stock = map.get(key);
	// // 反向，所以扣除
	// stock.setQuantity(stock.getQuantity() - item.getQuantity());
	// } else {
	//
	// GiftStock stock = new GiftStock();
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
	// public static Map<String, GiftStock> merge(GiftStockOut stockOut,
	// GiftStockOut out) {
	// Map<String, GiftStock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (stockOut != null) {
	// for (GiftStockOutItem item : stockOut.getItems()) {
	// GiftStock stock = new GiftStock();
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
	// for (GiftStockOutItem item : out.getItems()) {
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getSpace().getId();
	// if (map.containsKey(key)) {
	// GiftStock stock = map.get(key);
	// // 反向，所以扣除
	// stock.setQuantity(stock.getQuantity() - item.getQuantity());
	// } else {
	//
	// GiftStock stock = new GiftStock();
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
	// public static Map<String, GiftStock> merge(TransferOrder transOrder,
	// TransferOrder out) {
	// Map<String, GiftStock> map = WxlSugar.newLinkedHashMap();
	// // 正向
	// if (transOrder != null) {
	// for (TransferOrderItem item : transOrder.getItems()) {
	// GiftStock stock = new GiftStock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(item.getQuantity() * -1);
	// stock.setSpace(item.getFromSpace());
	// stock.setWarehouse(transOrder.getFromWarehouse());
	//
	// String key = item.getMaterial().getId() + ":"
	// + item.getFromSpace().getId();
	// map.put(key, stock);
	//
	// GiftStock stockIn = new GiftStock();
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
	// GiftStock stock = findOrPut(map, key, item);
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
	// private static GiftStock findOrPut(Map<String, GiftStock> map, String
	// key,
	// TransferOrderItem item) {
	// GiftStock stock = null;
	// if (map.containsKey(key)) {
	// stock = map.get(key);
	//
	// } else {
	//
	// stock = new GiftStock();
	// stock.setMaterial(item.getMaterial());
	// stock.setQuantity(0); // 然后面自己加
	// map.put(key, stock);
	// }
	// return stock;
	// }
}
