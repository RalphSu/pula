package puerta.support.db;

import java.util.List;
import java.util.Map;

public class PDbData {

	private List<String> labels;
	private List<Map<String, Object>> rows;

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

}
