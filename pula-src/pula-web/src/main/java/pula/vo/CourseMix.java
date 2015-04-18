package pula.vo;

import puerta.system.vo.MapList;

public class CourseMix {
	private MapList data;
	private MapList hits; // 上过课的内容

	public MapList getData() {
		return data;
	}

	public void setData(MapList data) {
		this.data = data;
	}

	public MapList getHits() {
		return hits;
	}

	public void setHits(MapList hits) {
		this.hits = hits;
	}

}
