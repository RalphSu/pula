/**
 * Created on 2009-11-4
 * WXL 2009
 * $Id$
 */
package puerta.support.dao;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import puerta.support.BasementException;
import puerta.support.utils.DateExTool;
import puerta.support.utils.DateJedi;
import puerta.support.utils.WxlSugar;

/**
 * 
 * @author tiyi
 * 
 */
public class HibernateTool {

	/**
	 * @param like
	 * @param like2
	 * @param like3
	 * @param like4
	 * @param like5
	 * @return
	 */
	public static Criterion or(Criterion... exp) {
		if (exp == null)
			return null;
		Criterion last = null;
		for (Criterion ex : exp) {
			if (ex == null)
				continue;

			if (last == null) {
				last = ex;
				continue;
			} else {
				last = Restrictions.or(ex, last);
			}
		}
		return last;
	}

	public static Criterion or(List<Criterion> exp) {
		if (exp == null || exp.size() == 0)
			return null;
		Criterion last = null;
		for (Criterion ex : exp) {
			if (ex == null)
				continue;

			if (last == null) {
				last = ex;
				continue;
			} else {
				last = Restrictions.or(ex, last);
			}
		}
		return last;
	}

	public static Criterion likeOr(String values, String... fields) {
		Criterion last = null;
		for (String f : fields) {
			Criterion exp = Restrictions.like(f, values, MatchMode.ANYWHERE);
			if (last == null) {
				last = exp;
			} else {
				last = Restrictions.or(exp, last);
			}
		}
		return last;
	}

	/**
	 * @param proList
	 * @param singleMapping
	 * @param string
	 * @return
	 */
	public static ProjectionList injectSingle(ProjectionList proList,
			String[] singleMapping, String string) {
		for (String s : singleMapping) {
			proList.add(Projections.property(string + "." + s)

			.as(s));
		}
		return proList;
	}

	/**
	 * @param proList
	 * @param singleMapping
	 * @param string
	 * @return
	 */
	public static ProjectionList injectSingleSum(ProjectionList proList,
			String[] singleMapping, String string) {
		for (String s : singleMapping) {
			proList.add(Projections.sum(string + "." + s)

			.as(s));
		}
		return proList;
	}

	/**
	 * @param proList
	 * @param aliasMapping
	 * @return
	 */
	public static ProjectionList injectAlias(ProjectionList proList,
			String[] aliasMapping) {
		for (int i = 0; i < aliasMapping.length; i += 2) {
			String s = aliasMapping[i];
			String s1 = aliasMapping[i + 1];
			proList.add(Projections.property(s).as(s1));
		}
		return proList;
	}

	/**
	 * @param occupation
	 * @param string
	 * @return
	 */
	public static Criterion likeOrManyValues(String occupation, String field) {
		String[] values = StringUtils.split(occupation, ",");
		Criterion last = null;
		for (String f : values) {
			if (StringUtils.isEmpty(f))
				continue;
			Criterion exp = Restrictions.like(field, f.trim(),
					MatchMode.ANYWHERE);
			if (last == null) {
				last = exp;
			} else {
				last = Restrictions.or(exp, last);
			}
		}
		return last;
	}

	/**
	 * @param dc
	 * @param field
	 * @param value
	 */
	public static boolean eqIfNotEmpty(DetachedCriteria dc, String field,
			String value) {
		if (!StringUtils.isEmpty(value)) {
			dc.add(Restrictions.eq(field, value));
			return true;
		}

		return false;
	}

	/**
	 * @param dc
	 * @param string
	 *            .. first - last-1 = fields , last = value
	 */
	public static boolean likeIfNotEmpty(DetachedCriteria dc, String... s) {
		if (s.length < 2) {
			return false;
		}
		String value = s[s.length - 1];
		if (!StringUtils.isEmpty(value)) {
			List<String> l = Arrays.asList(s);
			l = new ArrayList<String>(l);
			l.remove(l.size() - 1);
			dc.add(likeOr(value, l.toArray(new String[l.size()])));
			return true;
		}
		return false;

	}

	/**
	 * @param dc
	 * @param string
	 * @param value
	 */
	public static void eqIfNotZero(DetachedCriteria dc, String field, int value) {
		if (value == 0)
			return;
		dc.add(Restrictions.eq(field, value));
	}

	/**
	 * @param dc
	 * @param string
	 * @param beginDate
	 * @param endDate
	 */
	public static void betweenIfNotNull(DetachedCriteria dc, String field,
			String beginDate, String endDate, int limitDays) {
		Calendar bcal = DateExTool.getPrevDate(beginDate);
		Calendar ecal = DateExTool.getNextDate(endDate);
		long days = 0;
		if (limitDays > 0) {
			if (bcal == null || ecal == null) {
				throw new BasementException("超出天数:" + limitDays);
			} else {
				days = DateExTool.betweenDays(ecal, bcal);
			}
			if (limitDays < days) {
				throw new BasementException("超出天数:" + limitDays);
			}
		}

		if (bcal != null) {
			dc.add(Restrictions.ge(field, bcal));
		}
		if (ecal != null) {
			dc.add(Restrictions.lt(field, ecal));
		}

	}

	/**
	 * @param b
	 * @param statusExecute
	 * @param statusComplete
	 * @param i
	 * @param j
	 * @param thisMonthBegin
	 * @param thisMonthEnd
	 * @param no
	 * @param c
	 * @return
	 */
	public static Object[] mix(Object... os) {
		List<Object> params = WxlSugar.newArrayList();
		for (int i = 0; i < os.length; i++) {
			Object obj = os[i];
			if (obj != null && obj.getClass().isArray()) {
				int len = Array.getLength(obj);
				for (int c = 0; c < len; c++) {
					params.add(Array.get(obj, c));
				}
			} else {
				params.add(obj);
			}
		}

		return params.toArray();
	}

	/**
	 * @param string
	 * @param accompany
	 * @param aftersales
	 * @param xtm
	 * @return
	 */
	public static Criterion eqOr(String field, Object... objs) {
		if (objs == null)
			return null;
		Criterion last = null;
		for (Object v : objs) {
			Criterion exp = Restrictions.eq(field, v);
			if (last == null) {
				last = exp;
			} else {
				last = Restrictions.or(exp, last);
			}
		}
		return last;
	}

	/**
	 * @param dc
	 * @param string
	 * @param b
	 * @param forceNotConfirmed
	 */
	public static boolean eqIfTrue(DetachedCriteria dc, String field,
			Object value, boolean flag) {
		if (flag) {
			dc.add(Restrictions.eq(field, value));
			return true;
		}

		return false;

	}

	/**
	 * @param dc
	 * @param string
	 * @param year
	 * @param month
	 */
	public static void betweenMonth(DetachedCriteria dc, String field,
			int year, int month, int monthStarts) {
		Calendar bcal = DateJedi.create(year, month - monthStarts)
				.firstDayOfMonth().to();
		Calendar ecal = DateJedi.create(bcal).lastDayOfMonth().to();
		// long days = 0;

		if (bcal != null) {
			dc.add(Restrictions.gt(field, bcal));
		}
		if (ecal != null) {
			dc.add(Restrictions.lt(field, ecal));
		}

	}

	public static void eq(DetachedCriteria dc, String string, Object b) {
		dc.add(Restrictions.eq(string, b));
	}

	public static void betweenDoubleIfNotEmpty(DetachedCriteria dc,
			String field, String min, String max) {

		if (!StringUtils.isEmpty(min) && NumberUtils.isNumber(min)) {
			double dmin = NumberUtils.toDouble(min);
			dc.add(Restrictions.ge(field, dmin));
		}
		if (!StringUtils.isEmpty(max) && NumberUtils.isNumber(max)) {
			double dmax = NumberUtils.toDouble(max);
			dc.add(Restrictions.le(field, dmax));
		}

	}

	public static ProjectionList injectAliasSum(ProjectionList proList,
			String[] aliasMapping) {

		for (int i = 0; i < aliasMapping.length; i += 2) {
			String s = aliasMapping[i];
			String s1 = aliasMapping[i + 1];
			proList.add(Projections.sum(s).as(s1));
		}
		return proList;
	}

	public static boolean eqIfNotZero(DetachedCriteria dc, String field,
			long value) {
		if (value == 0)
			return false;
		dc.add(Restrictions.eq(field, value));
		return true;
	}

	@SuppressWarnings("unchecked")
	public static <T, E> Map<T, E> asMap(List<Object> find) {
		Map<T, E> map = WxlSugar.newLinkedHashMap();

		for (Object rowso : find) {
			Object[] rows = (Object[]) rowso;
			T id = (T) rows[0];
			E data = (E) rows[1];
			map.put(id, data);

		}

		return map;
	}

	public static final int YES = 1;
	public static final int NO = -1;
	public static final int NONE = 0;

	public static void eqIfHas(DetachedCriteria dc, String string, int s) {
		eqIfTrue(dc, string, s == YES, s != NONE);
	}

	public static void eqOr(DetachedCriteria dc, String string, Object... types) {
		Criterion d = eqOr(string, types);
		if (d == null)
			return;
		dc.add(d);

	}

	@SuppressWarnings("unchecked")
	public static <T, E> Map<T, List<E>> asMapList(List<Object> find) {
		Map<T, List<E>> map = WxlSugar.newLinkedHashMap();

		for (Object rowso : find) {
			Object[] rows = (Object[]) rowso;
			T key = (T) rows[0];
			E data = (E) rows[1];
			List<E> list = null;
			if (map.containsKey(key)) {
				list = map.get(key);
			} else {
				list = WxlSugar.newArrayList();
				map.put(key, list);
			}

			list.add(data);

		}

		return map;
	}

	public static void eqOrIfNotEmpty(DetachedCriteria dc, String value,
			String... strs) {
		if (strs == null || strs.length == 0 || StringUtils.isEmpty(value))
			return;

		Criterion last = null;
		for (String field : strs) {
			Criterion exp = Restrictions.eq(field, value);
			if (last == null) {
				last = exp;
			} else {
				last = Restrictions.or(exp, last);
			}
		}
		dc.add(last);

	}

	public static ProjectionList injectAlias(ProjectionList proList,
			String[] aliasMapping, HibernateInjectMapper hibernateInjectMapper) {
		for (int i = 0; i < aliasMapping.length; i += 2) {
			String s = aliasMapping[i];
			String s1 = aliasMapping[i + 1];
			Projection p = hibernateInjectMapper.make(s, s1);
			if (p != null)
				proList.add(p);
		}
		return proList;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<T, Object[]> asMapArray(List<Object> find) {
		Map<T, Object[]> map = WxlSugar.newLinkedHashMap();

		for (Object rowso : find) {
			Object[] rows = (Object[]) rowso;
			T id = (T) rows[0];

			Object[] es = Arrays.copyOfRange(rows, 1, rows.length);

			map.put(id, es);

		}

		return map;
	}
}
