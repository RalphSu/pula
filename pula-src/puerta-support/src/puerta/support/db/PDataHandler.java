package puerta.support.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.ResultSetHandler;

import puerta.support.utils.WxlSugar;

public class PDataHandler implements ResultSetHandler<PDbData> {

	private PMapListHandler mapListHander = new PMapListHandler();

	@Override
	public PDbData handle(ResultSet rs) throws SQLException {

		// 要字段的时间到了
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		List<String> columns = WxlSugar.newArrayList(cols);

		for (int i = 1; i <= cols; i++) {
			columns.add(rsmd.getColumnLabel(i));
		}

		List<Map<String, Object>> m = mapListHander.handle(rs);
		PDbData pd = new PDbData();
		pd.setRows(m);
		pd.setLabels(columns);
		return pd;
	}

}
