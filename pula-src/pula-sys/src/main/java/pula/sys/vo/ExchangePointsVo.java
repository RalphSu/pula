package pula.sys.vo;

import java.util.Map;

import puerta.support.utils.WxlSugar;

public class ExchangePointsVo {

	private String shopId, shopName, shopNo;
	private Map<Integer, Long> counter;

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Map<Integer, Long> getCounter() {
		return counter;
	}

	public void setCounter(Map<Integer, Long> counter) {
		this.counter = counter;
	}

	public long getCounter(int type) {
		if (counter == null) {
			counter = WxlSugar.newHashMap();
		}

		if (counter.containsKey(type)) {
			return counter.get(type);
		}
		return 0;
	}

	public void putCounter(int mt, long pts) {
		if (counter == null) {
			counter = WxlSugar.newHashMap();
		}

		counter.put(mt, pts);

	}

	public void sumCounter(int mt, long pts) {
		if (counter == null) {
			counter = WxlSugar.newHashMap();
		}

		pts = getCounter(mt) + pts;
		counter.put(mt, pts);

	}

}
