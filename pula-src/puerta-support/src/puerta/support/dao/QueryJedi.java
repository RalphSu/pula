package puerta.support.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import puerta.support.Pe;
import puerta.support.utils.DateExTool;
import puerta.support.utils.WxlSugar;

import com.mchange.v1.util.ArrayUtils;

public class QueryJedi implements Cloneable {

	public QueryJedi() {
		this.values = WxlSugar.newArrayList();
		this.conditionSb = new StringBuilder();
	}

	public QueryJedi(String hql, Object... objs) {
		this();
		this.append(hql, objs);

	}

	private List<Object> values;
	private StringBuilder conditionSb;

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	// public QueryParameter append(String str, Object obj) {
	// this.conditionSb.append(str);
	// values.add(obj);
	// return this;
	// }

	public QueryJedi append(String str, Object... obj) {

		if (str != null) {

			int c = StringUtils.countMatches(str, "?");

			if (c != obj.length) {
				Pe.raise("QueryParameter:参数对应有误：" + str
						+ ArrayUtils.toString(obj));
			}

			this.conditionSb.append(str).append(" ");
		}
		for (Object ob : obj) {
			values.add(ob);
		}
		return this;
	}

	public String condition() {
		return conditionSb.toString();
	}

	@Deprecated
	public String toHql() {
		return conditionSb.toString();
	}

	public String toHql(String groupBy) {
		return conditionSb.append(groupBy).toString();
	}

	public String hql() {
		return conditionSb.toString();
	}

	public Object[] parameters() {
		return this.values.toArray();
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void ifNotEmpty(String exp, String v) {
		if (StringUtils.isEmpty(v)) {
			return;
		}

		append(exp, v);

	}

	public void ifNotZero(String exp, long v) {
		if (v == 0) {
			return;
		}
		append(exp, v);

	}

	public QueryJedi eqOr(String field, Object... objects) {

		if (objects == null || objects.length == 0) {
			return this;
		}

		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < objects.length; i++) {

			if (i == 0) {

			} else {
				sb.append(" or ");
			}
			sb.append(field).append("=?");
		}
		sb.append(")");

		append(sb.toString(), objects);
		return this;

	}

	public void eqAnd(String field, Object... objects) {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0; i < objects.length; i++) {

			if (i == 0) {

			} else {
				sb.append(" and ");
			}
			sb.append(field).append("=?");
		}
		sb.append(")");

		append(sb.toString(), objects);

	}

	public void betweenDays(String string, String beginDate, String endDate) {
		Calendar bd = DateExTool.getDate(beginDate);
		Calendar ed = DateExTool.getDate(endDate);

		if (bd != null) {
			this.append(" and " + string + ">?", DateExTool.yesterday(bd));
		}

		if (bd != null) {
			this.append(" and " + string + "<?", DateExTool.yesterday(ed));
		}
	}

	public boolean eqIfNotEmpty(String field, String v) {
		if (!StringUtils.isEmpty(v)) {
			this.append(" and " + field + "=?", v);
			return true;
		}
		return false;

	}

	public QueryJedi clear() {
		this.conditionSb.delete(0, this.conditionSb.length());
		this.values.clear();
		return this;
	}

	public QueryJedi select(String tblAlias, String... fields) {
		if (fields == null || fields.length == 0) {
			return this;
		}
		boolean need = true;
		if (this.conditionSb.lastIndexOf(",") == this.conditionSb.length() - 1) {
			need = false;
		}
		if (this.conditionSb.toString().trim().equals("select")) {
			need = false;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String s : fields) {
			if (first && need || !first) {
				sb.append(",");

			}
			sb.append(tblAlias).append(".").append(s).append(" AS ").append(s);
			first = false;
		}
		this.conditionSb.append(sb);
		return this;

	}

	public QueryJedi selectAs(String... fields) {
		if (fields == null || fields.length == 0) {
			return this;
		}
		boolean need = true;
		if (this.conditionSb.lastIndexOf(",") == this.conditionSb.length() - 1) {
			need = false;
		}
		if (this.conditionSb.toString().trim().equals("select")) {
			need = false;
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (int i = 0; i < fields.length; i += 2) {
			if (first && need || !first) {
				sb.append(",");
			}
			sb.append(fields[i]).append(" AS ").append(fields[i + 1]);
			first = false;
		}
		this.conditionSb.append(sb);
		return this;
	}

	public boolean eqIfNotZero(String field, long v) {
		if (v == 0)
			return false;

		this.conditionSb.append(" and ").append(field).append("=?");
		this.values.add(v);
		return true;

	}

	public boolean likeIfNotEmpty(String... objs) {
		if (objs == null || objs.length < 2) {
			return false;
		}
		String k = objs[objs.length - 1];

		if (StringUtils.isEmpty(k)) {
			return false;
		}

		String v = "%" + k + "%";

		conditionSb.append(" and ( ");
		boolean first = true;
		int idx = 0;
		for (String field : objs) {
			if (idx == objs.length - 1) {
				break;
			} else {
				idx++;
			}

			if (!first) {
				conditionSb.append(" OR ");
			}
			conditionSb.append(field).append(" LIKE ? ");
			this.values.add(v);
			first = false;
		}
		conditionSb.append(" ) ");

		return true;

	}
}
