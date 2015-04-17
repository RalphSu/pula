package puerta.support.db;

public class DbPostaPatternFactory {
	public static DbPostaPattern create(String tableName) {
		return DbPostaPattern.create(tableName);
	}

	public static DbPostaPattern createNoAutoGenerate(String string) {
		return DbPostaPattern.createNoAutoGenerate(string);
	}

	public static DbPostaPattern createWithFilter(String string, String string2) {
		return DbPostaPattern.createWithFilter(string, string2);
	}

	public static DbPostaPattern create(String tableName, String orderBy) {
		DbPostaPattern d = new DbPostaPattern();
		d.setTableName(tableName);
		d.setOrderBy(orderBy);
		d.setMode(DbPostaPattern.ORDER_BY);
		return d;
	}

	public static DbPostaPattern createSQL(String tableName, String fromSQL) {
		DbPostaPattern d = new DbPostaPattern();
		d.setTableName(tableName);
		d.setFromSQL(fromSQL);
		d.setMode(DbPostaPattern.FROM_SQL);
		return d;
	}
}
