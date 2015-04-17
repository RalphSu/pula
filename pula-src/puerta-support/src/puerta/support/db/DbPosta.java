package puerta.support.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import puerta.support.utils.WxlSugar;

public class DbPosta {

	private static final Logger logger = Logger.getLogger(DbPosta.class);

	private Connection src, dest;

	public static DbPosta create(Connection src, Connection dest) {
		DbPosta dbs = new DbPosta();
		dbs.src = src;
		dbs.dest = dest;
		return dbs;
	}

	public void borre(DbPostaPattern... ts) throws SQLException {
		for (DbPostaPattern t : ts) {
			if (!t.isFkCheck()) {
				_execute(dest, "SET FOREIGN_KEY_CHECKS=0",
						Collections.emptyList());
			}
			_execute(dest, "delete from " + t.getTableName(),
					Collections.emptyList());
			if (!t.isFkCheck()) {
				_execute(dest, "SET FOREIGN_KEY_CHECKS=1",
						Collections.emptyList());
			}
		}
	}

	private List<DbMetaData> _getColumns(Connection conn, String sql,
			List<String> filters) throws SQLException {
		Statement stmt = null;
		List<DbMetaData> list = WxlSugar.newArrayList();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int cc = md.getColumnCount();
			for (int i = 1; i <= cc; i++) {
				// System.out.println(md.getColumnName(i));
				String cn = md.getColumnName(i);
				int maxl = md.getColumnDisplaySize(i);

				if (filters != null && filters.indexOf(cn) >= 0) {

				} else {
					list.add(DbMetaData.create(cn, maxl));
				}

			}

			rs.close();

		} finally {
			if (stmt != null) {
				stmt.close();
			}
			stmt = null;

		}
		return list;
	}

	private void _generateInsert(Connection oriConn, Connection destConn,
			String dataSQL, String t, List<String> filters, boolean noAuto)
			throws SQLException {
		StringBuilder sb = new StringBuilder();
		if (noAuto) {
			sb.append("set IDENTITY_INSERT " + t + " ON ");
		}
		sb.append("insert into ").append(t).append(" (");
		Statement stmt = oriConn.createStatement();
		ResultSet rs = stmt.executeQuery(dataSQL);
		// ResultSetMetaData md = rs.getMetaData();
		// build fields from dest
		List<DbMetaData> dest_columns = _getColumns(destConn, "select * from "
				+ t, filters);
		// List<String> src_columns = _getColumns(oriConn, dataSQL);
		int cc = dest_columns.size();

		for (int i = 0; i < cc; i++) {
			// System.out.println(md.getColumnName(i));
			sb.append(dest_columns.get(i).getName());
			if (cc == i + 1) {
			} else {
				sb.append(",");
			}
		}
		sb.append(") values(");
		for (int i = 1; i <= cc; i++) {
			sb.append("?");
			if (cc == i) {
			} else {
				sb.append(",");
			}
		}
		sb.append(")");
		if (noAuto) {
			sb.append(" set IDENTITY_INSERT " + t + " OFF ");
		}
		// insert into
		String sql = sb.toString();
		logger.debug("sql=" + sql);
		int c = 0;
		while (rs.next()) {
			c++;
			List<Object> values = _extract(rs, cc, dest_columns);
			try {
				_execute(destConn, sql, values);
			} catch (SQLException ex) {
				for (int i = 0; i < values.size(); i++) {
					DbMetaData d = dest_columns.get(i);
					int ml = d.getMaxLength();
					int len = getLength(values.get(i));
					logger.debug(d.getName() + "=" + values.get(i) + " maxlen="
							+ ml + " now=" + len);

					if (ml < len) {
						logger.error("GOT!!!!!!!!!!!!!!! ");
					}

				}

				throw ex;
			}

		}
		logger.debug("insert data:" + c + " rows");

	}

	private int getLength(Object object) {
		if (object instanceof String) {
			return ((String) object).length();
		}
		return 0;
	}

	private List<Object> _extract(ResultSet rs, int columnCount,
			List<DbMetaData> columns) throws SQLException {
		List<Object> list = new ArrayList<Object>(columnCount);
		if (columns.size() > 0) {
			for (DbMetaData s : columns) {
				try {
					rs.findColumn(s.getName());
					Object obj = rs.getObject(s.getName());
					list.add(obj);
				} catch (SQLException ex) {
					list.add(null);
				}

			}
		} else {
			for (int i = 1; i <= columnCount; i++) {
				try {
					Object obj = rs.getObject(i);
					list.add(obj);
				} catch (SQLException ex) {
					list.add(null);
				}
			}
		}
		return list;
	}

	public void transferirWithSQL(String dataSQL, String t, List<String> list,
			boolean noAuto) throws SQLException {

		_generateInsert(src, dest, dataSQL, t, list, noAuto);

	}

	public void transferir(String t, List<String> list) throws SQLException {
		String sql = "select * from " + t;
		_generateInsert(src, dest, sql, t, list, false);
	}

	// do it
	private void _execute(Connection connection, String string,
			List<Object> values) throws SQLException {
		PreparedStatement pstmt2 = null;
		// logger.debug("execute >>" + string);
		try {
			logger.error("start exec=" + string);
			pstmt2 = connection.prepareStatement(string);
			for (int i = 0; i < values.size(); i++) {
				pstmt2.setObject(i + 1, values.get(i));
			}
			pstmt2.execute();
		} catch (SQLException ex) {
			logger.error("@@when exec=" + string);
			for (Object obj : values) {
				logger.error("data=" + obj);
			}
			throw ex;

		} finally {
			if (pstmt2 != null)
				pstmt2.close();

			pstmt2 = null;
		}

	}

	public void come(boolean clearFirst, DbPostaPattern... series)
			throws SQLException {

		if (clearFirst) {

			// clear first
			DbPostaPattern[] arrCopy = Arrays.copyOf(series, series.length);
			ArrayUtils.reverse(arrCopy);
			borre(arrCopy);
		}

		// now read and process;
		for (DbPostaPattern d : series) {
			comeuno(d);
		}

	}

	private void comeuno(DbPostaPattern d) throws SQLException {

		if (!d.isFkCheck()) {
			_execute(dest, "SET FOREIGN_KEY_CHECKS=0", Collections.emptyList());
		}

		switch (d.getMode()) {
		case DbPostaPattern.DIRECT:
			transferir(d.getTableName(), d.getFilters());
			break;
		case DbPostaPattern.ORDER_BY:
			transferirWithSQL("select * from " + d.getTableName()
					+ " order by " + d.getOrderBy(), d.getTableName(),
					d.getFilters(), false);
			break;
		case DbPostaPattern.FROM_SQL:
			transferirWithSQL(d.getFromSQL(), d.getTableName(), d.getFilters(),
					false);
			break;
		case DbPostaPattern.NO_AUTO_GENERATE:
			transferirWithSQL("select * from " + d.getTableName(),
					d.getTableName(), null, true);
		}
		if (!d.isFkCheck()) {
			_execute(dest, "SET FOREIGN_KEY_CHECKS=1", Collections.emptyList());
		}
	}

	public void liberte() {
		try {
			src.close();
			dest.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void turn(String string) throws SQLException {
		_execute(this.dest, string, Collections.emptyList());

	}

	public static DbPosta create(ConnectionInfo create, ConnectionInfo create2)
			throws Exception {
		DbAccess dba = new DbAccess();
		Connection oriConn = dba.getConnection(create);
		Connection revConn = dba.getConnection(create2);
		return create(oriConn, revConn);
	}

	public void executeDest(String sql, Object... params) throws SQLException {
		_execute(this.dest, sql, Arrays.asList(params));

	}

	public <T> List<T> executeQueryFrom(String sql, Object... params)
			throws SQLException {
		return _executeQuery(this.src, sql, Arrays.asList(params));
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> _executeQuery(Connection connection, String sql,
			List<Object> params) throws SQLException {
		PreparedStatement pstmt2 = null;
		logger.debug("executeQuery " + sql);
		List<Object> results = WxlSugar.newArrayList();
		try {
			pstmt2 = connection.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++) {
				pstmt2.setObject(i + 1, params.get(i));
			}
			ResultSet rs = pstmt2.executeQuery();
			while (rs.next()) {
				List<Object> values = _extract(rs, rs.getMetaData()
						.getColumnCount(), Collections.EMPTY_LIST);
				if (values.size() > 1) {
					results.add(values.toArray());
				} else if (values.size() > 0) {
					results.add(values.get(0));
				}
			}
		} finally {
			if (pstmt2 != null)
				pstmt2.close();

			pstmt2 = null;
		}

		return (List<T>) results;

	}

	public void mapResult(DbPostaParam src, DbPostaParam dest)
			throws SQLException {
		List<Object> values = null;
		if (src.getConnection() == DbPostaParam.DEST) {
			values = _executeQuery(this.dest, src.getSql(), src.getParams());
		} else {
			values = _executeQuery(this.src, src.getSql(), src.getParams());
		}

		logger.debug("get " + values.size() + " rows");
		// dest
		// put

		logger.debug("execute:" + dest.getSql());
		for (Object obj : values) {
			List<Object> pp;
			if (obj instanceof Object[]) {
				pp = Arrays.asList((Object[]) obj);
			} else {
				pp = WxlSugar.newArrayList();
				pp.add(obj);
			}
			if (dest.getConnection() == DbPostaParam.DEST) {
				this._execute(this.dest, dest.getSql(), pp);
			} else {
				this._execute(this.src, dest.getSql(), pp);

			}
		}

	}

	public DbDataRows executeSrc(String sql, Object... values)
			throws SQLException {
		return _executeQuery2(this.src, sql, Arrays.asList(values));
	}

	private DbDataRows _executeQuery2(Connection connection, String sql,
			List<Object> params) throws SQLException {
		PreparedStatement pstmt2 = null;
		logger.debug("executeQuery " + sql);
		DbDataRows results = new DbDataRows();
		try {
			pstmt2 = connection.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++) {
				pstmt2.setObject(i + 1, params.get(i));
			}
			ResultSet rs = pstmt2.executeQuery();
			while (rs.next()) {
				DbDataRow r = _extract(rs);
				results.add(r);

			}
		} finally {
			if (pstmt2 != null)
				pstmt2.close();

			pstmt2 = null;
		}

		return results;
	}

	private DbDataRow _extract(ResultSet rs) throws SQLException {
		int c = rs.getMetaData().getColumnCount();
		DbDataRow d = new DbDataRow();
		for (int n = 1; n <= c; n++) {
			String cname = rs.getMetaData().getColumnName(n);
			d.put(cname, rs.getObject(n));
		}
		return d;

	}

	public void insertDest(String table, DbDataRows values) throws SQLException {
		if (values.size() == 0)
			return;
		DbDataRow r = values.get(0);

		StringBuffer sb = new StringBuffer("INSERT INTO ").append(table)
				.append("(");

		StringBuffer sbV = new StringBuffer();
		for (String k : r.keySet()) {
			sb.append(k).append(",");
			sbV.append("? ").append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") VALUES(").append(sbV);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");

		String sql = sb.toString();
		for (DbDataRow rr : values) {
			executeDest(sql, new ArrayList<Object>(rr.values()).toArray());
		}

	}

	public void insertDest(String table, DbDataRows values,
			FieldCallback callback) throws SQLException {
		if (values.size() == 0)
			return;
		DbDataRow r = values.get(0);

		StringBuffer sb = new StringBuffer("INSERT INTO ").append(table)
				.append("(");

		StringBuffer sbV = new StringBuffer();
		for (String k : r.keySet()) {
			sb.append(k).append(",");
			sbV.append("? ").append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(") VALUES(").append(sbV);
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");

		String sql = sb.toString();
		for (DbDataRow rr : values) {
			if (callback != null) {
				List<Object> objs = new ArrayList<Object>();
				for (String k : rr.keySet()) {
					Object v = rr.get(k);
					objs.add(callback.trans(k, v));
				}
				executeDest(sql, objs.toArray());
			} else {
				executeDest(sql, new ArrayList<Object>(rr.values()).toArray());
			}
		}

	}

	public void begin() {
		try {
			this.dest.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void commit() {
		try {
			this.dest.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
