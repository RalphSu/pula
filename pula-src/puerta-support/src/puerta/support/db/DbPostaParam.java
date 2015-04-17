package puerta.support.db;

import java.util.Collections;
import java.util.List;

public class DbPostaParam {
	public static final int FROM = 1;
	public static final int DEST = 2;
	private static final Object[] EMPTY_ARRAY = new Object[] {};

	private String sql;
	private List<Object> params;
	private int connection;

	public static DbPostaParam dest(String string) {
		DbPostaParam p = create(string);
		p.connection = DEST;
		return p;

	}

	public static DbPostaParam from(String string) {
		DbPostaParam p = create(string);
		p.connection = FROM;
		return p;

	}

	private static DbPostaParam create(String string) {
		DbPostaParam d = new DbPostaParam();
		d.params = Collections.emptyList();
		d.sql = string;
		return d;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public int getConnection() {
		return connection;
	}

	public void setConnection(int connection) {
		this.connection = connection;
	}

}
