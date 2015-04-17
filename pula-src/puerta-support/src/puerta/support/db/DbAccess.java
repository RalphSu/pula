/**
 * Created on 2009-4-8
 * WXL 2009
 * $Id$
 */
package puerta.support.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import puerta.support.BasementException;
import puerta.support.PaginationSupport;

/**
 * 
 * @author tiyi
 * 
 */
public class DbAccess {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DbAccess.class);

	public void testConnection(String driver, String url, String userName,
			String password) {
		Connection conn = null;
		try {
			// printJdbcInfo();
			conn = getConnection(new ConnectionInfo(driver, url, userName,
					password));

		} catch (Exception e) {
			throw new BasementException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new BasementException(e);
				}
			}
		}
	}

	public PaginationSupport<Object[]> execute(ConnectionInfo ci, String sql,
			int beginIndex, int pageSize) {
		Connection connection = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;

		try {
			connection = getConnection(ci);

			int count = getCount(connection, sql);

			if (beginIndex >= count) {
				if (count >= pageSize) {
					beginIndex = count - pageSize;
				} else {
					beginIndex = 0;
				}
			}

			pstmt2 = connection.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			// pstmt2.setMaxRows(pageSize);
			// pstmt2.setFetchSize(pageSize);
			rs = pstmt2.executeQuery();

			rs.absolute(beginIndex);
			int cc = rs.getMetaData().getColumnCount();
			List<Object[]> results = new ArrayList<Object[]>();
			int i = 0;
			while (rs.next() && i < pageSize) {
				Object[] objs = new Object[cc];
				for (int n = 1; n <= cc; n++) {
					objs[n - 1] = rs.getObject(n);
				}
				results.add(objs);
				i++;
			}

			PaginationSupport<Object[]> ps = new PaginationSupport<Object[]>(
					results, count, pageSize, beginIndex);
			ps.setPageSize(pageSize);

			return ps;

		} catch (Exception ex) {
			throw new BasementException(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {

				throw new BasementException(e);
			}
		}
	}

	/**
	 * @param connection
	 * @param sql
	 * @return
	 */
	public int getCount(Connection connection, String sql) throws Exception {
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String countQueryString = "SELECT count(*) "
				+ SqlHelper.removeSelect(SqlHelper.removeOrders(sql));

		logger.debug("count=" + countQueryString);
		try {
			pstmt2 = connection.prepareStatement(countQueryString);
			rs = pstmt2.executeQuery();
			rs.next();

			int n = rs.getInt(1);
			return n;

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt2 != null) {
					pstmt2.close();
				}

			} catch (SQLException e) {

				throw new BasementException(e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilvar.pew.system.jdbc.JdbcMgr#getConnection(com.nilvar.pew.system
	 * .jdbc.ConnectionInfo)
	 */
	public Connection getConnection(ConnectionInfo ci) throws Exception {
		Class.forName(ci.getDriver());
		return DriverManager.getConnection(ci.getUrl(), ci.getUserName(),
				ci.getPassword());
	}

}
