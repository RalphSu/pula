package puerta.support.db;

import java.util.Arrays;
import java.util.List;

import puerta.support.utils.WxlSugar;

public class DbPostaPattern {

	public static final int DIRECT = 1;
	public static final int ORDER_BY = 2;
	public static final int FROM_SQL = 3;
	public static final int NO_AUTO_GENERATE = 4;

	private String tableName;
	private String orderBy;
	private String fromSQL;
	private List<String> filters = WxlSugar.newArrayList();

	private int mode = DIRECT;
	private boolean fkCheck = true;

	public static DbPostaPattern create(String tableName) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;

		return d;
	}

	public static DbPostaPattern create(String tableName, String orderBy) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;
		d.orderBy = orderBy;
		d.mode = ORDER_BY;
		return d;
	}

	public static DbPostaPattern createAsTable(String tableName,
			String fromTable) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;
		d.fromSQL = "select * from " + fromTable;
		d.mode = FROM_SQL;
		return d;
	}

	public static DbPostaPattern createSQL(String tableName, String sql) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;
		d.fromSQL = sql;
		d.mode = FROM_SQL;
		return d;
	}

	public static DbPostaPattern createWithFilter(String tableName,
			String... filter) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;
		d.filters.addAll(Arrays.asList(filter));
		return d;
	}

	public static DbPostaPattern createNoAutoGenerate(String tableName) {
		DbPostaPattern d = new DbPostaPattern();
		d.tableName = tableName;
		d.mode = NO_AUTO_GENERATE;
		return d;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getFromSQL() {
		return fromSQL;
	}

	public void setFromSQL(String fromSQL) {
		this.fromSQL = fromSQL;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public List<String> getFilters() {
		return filters;
	}

	public void setFilters(List<String> filters) {
		this.filters = filters;
	}

	public DbPostaPattern notFkCheck() {
		fkCheck = false;
		return this;
	}

	public boolean isFkCheck() {
		return fkCheck;
	}

	public void setFkCheck(boolean fkCheck) {
		this.fkCheck = fkCheck;
	}
}
