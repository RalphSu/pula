package puerta.support.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcDaoImpl {
	@Resource
	protected JdbcTemplate jdbcTemplate;
	private static final Logger log = Logger.getLogger(JdbcDaoImpl.class);

	public int insert(String tableName, Map<String, ?> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append("(");
		StringBuilder values = new StringBuilder(" VALUES(");

		int size = paramMap.size();
		String[] keys = paramMap.keySet().toArray(new String[size]);
		for (int i = 0; i < size; i++) {
			String k = keys[i];
			sb.append(k);
			values.append("?");
			if (i < size - 1) {
				values.append(",");
				sb.append(",");
			}
		}
		sb.append(values);
		String sql = sb.toString();
		log.debug("SQL:" + sql);

		return jdbcTemplate.update(sql, paramMap);
	}

	public int updateAuto(String tableName, Map<String, ?> paramMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET ");

		int size = paramMap.size();
		String[] keys = paramMap.keySet().toArray(new String[size]);
		for (int i = 0; i < size; i++) {
			String k = keys[i];
			sb.append(k).append("=?");

			if (i < size - 1) {
				sb.append(",");
			}
		}
		String sql = sb.toString();
		log.debug("SQL:" + sql);
		return jdbcTemplate.update(sql, paramMap);
	}

}
