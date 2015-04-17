package pula.web.vo;

import puerta.system.vo.MapBean;

public class PointsMix {

	private PageInfo<MapBean> data;
	private long totalPoints;

	public PageInfo<MapBean> getData() {
		return data;
	}

	public void setData(PageInfo<MapBean> data) {
		this.data = data;
	}

	public long getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(long totalPoints) {
		this.totalPoints = totalPoints;
	}

}
