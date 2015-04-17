/**
 * Created on 2008-3-21 上午11:03:05
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.base;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import puerta.support.GenericsUtils;
import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.PaginationSupportFactory;
import puerta.support.annotation.WxlDomain;
import puerta.support.dao.BaseDao;
import puerta.support.dao.CommonCondition;
import puerta.support.dao.DomainInspector;
import puerta.support.dao.LoggablePo;
import puerta.support.dao.OrderBySupport;
import puerta.support.db.SqlHelper;
import puerta.support.mls.Mls;
import puerta.support.utils.PewUtils;
import puerta.system.dao.LoggerDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;

/**
 * 
 * @author tiyi
 * 
 */
@SuppressWarnings("unchecked")
public class HibernateGenericDao<T, ID extends Serializable> extends
		HibernateDaoSupport implements BaseDao<T, ID> {

	protected Class<T> pojoClass;
	protected boolean domainPo, loggable, gotUpdatedTime, domainInspector;

	@Resource
	protected LoggerDao loggerDao;

	@Resource
	protected Mls mls;

	@Resource(name = "sessionFactory")
	public void setBaseDaoSessionFactory(SessionFactory sf) {
		super.setSessionFactory(sf);
	}

	/** */
	/**
	 * 初始化DAO，获取POJO类型
	 */
	public HibernateGenericDao() {
		// this.pojoClass =
		// (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).
		// getActualTypeArguments()[0];
		this.pojoClass = GenericsUtils.getGeneric(getClass());
		this.loggable = false;
		this.domainInspector = DomainInspector.class
				.isAssignableFrom(getClass());
		if (this.pojoClass != null) {
			domainPo = this.pojoClass.isAnnotationPresent(WxlDomain.class);
			loggable = LoggablePo.class.isAssignableFrom(pojoClass);
		} else {

		}
		// this.
		Method[] fs = this.pojoClass.getMethods();
		for (Method f : fs) {
			if (f.getName().equals("setUpdatedTime")) {
				gotUpdatedTime = true;
				break;
			}
		}

	}

	/** */
	/**
	 * 获得该DAO对应的POJO类型
	 */
	public Class<T> getPojoClass() {
		return this.pojoClass;
	}

	/** */
	/**
	 * 获得该DAO对应的POJO类型名
	 */
	public String getPojoClassName() {
		return getPojoClass().getName();
	}

	// 加载对象

	/** */
	/**
	 * 加载所有的对象
	 */
	public List<T> loadAll() {
		if (!domainPo) {
			return (List<T>) getHibernateTemplate().loadAll(getPojoClass());
		} else {
			return loadAllWithoutRemoved();
		}
	}

	public List<T> loadAllWithoutRemoved() {
		return findByProperty("removed", false);
	}

	/** */
	/**
	 * 根据hql查询
	 * 
	 * @param values
	 *            可变参数
	 */
	public <E> List<E> find(String hql, Object... values) {
		return getHibernateTemplate().find(hql, values);
	}

	/** */
	/**
	 * 根据条件加载对象
	 * 
	 * @param criteria
	 *            Criteria实例
	 */
	public List<T> findByCriteria(final Criteria criteria) {
		List<Object> list = criteria.list();
		return transformResults(list);
	}

	/** */
	/**
	 * 根据条件加载对象
	 * 
	 * @param detachedCriteria
	 *            DetachedCriteria实例
	 */
	public <E> List<E> findByCriteria(final DetachedCriteria detachedCriteria) {
		return (List<E>) getHibernateTemplate().execute(
				new HibernateCallback<List<E>>() {
					public List<E> doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						List<E> list = criteria.list();
						return transformResults(list);
					}
				});
	}

	/** */
	/**
	 * 根据给定的实例查找对象
	 */
	public List<T> findByExample(T instance) {
		List<T> results = (List<T>) getHibernateTemplate().findByExample(
				instance);
		return results;
	}

	/** */
	/**
	 * 根据ID查找对象
	 */
	public T findById(ID id) {
		return (T) getHibernateTemplate().get(getPojoClassName(), id);
	}

	public T findByNo(String no) {
		List<T> rets;
		if (this.domainPo) {
			rets = this.findByProperty(new String[] { "no", "removed" },
					new Object[] { no, false });
		} else {
			rets = this.findByProperty("no", no);
		}

		if (rets.size() == 0) {
			return null;
		}
		return rets.get(0);
	}

	/** */
	/**
	 * 根据某个具体属性进行查找
	 */
	public List<T> findByProperty(String propertyName, Object value) {
		String queryString = "from " + getPojoClassName()
				+ " as model where model." + propertyName + "= ?";
		return (List<T>) getHibernateTemplate().find(queryString, value);
	}

	/**
	 * 都找找看
	 * 
	 * @param propertyName
	 * @param objects
	 * @return
	 */
	public List<T> findByProperty(String[] propertyName, Object[] objects) {
		String queryString = "from " + getPojoClassName()
				+ " as model where 1=1 ";

		StringBuilder sb = new StringBuilder(queryString);
		for (int i = 0; i < propertyName.length; i++) {
			sb.append(" and model." + propertyName[i] + "= ?");
		}
		String hql = sb.toString();
		logger.debug("hql=" + hql);
		return (List<T>) getHibernateTemplate().find(hql, objects);
	}

	// 新建、修改、删除

	/** */
	/**
	 * 新建对象实例化
	 */
	public ID _save(T transientInstance) {

		ID i = (ID) getHibernateTemplate().save(transientInstance);

		if (loggable) {
			String e = mls.t("save");
			loggerDao.doLog(e, (LoggablePo) transientInstance);
		}

		return i;
	}

	public void _update(T transientInstance) {
		if (loggable) {
			String e = mls.t("update");
			loggerDao.doLog(e, (LoggablePo) transientInstance);
		}

	}

	/** */
	/**
	 * 更新已存在的对象
	 */
	public void updateDirectly(T transientInstance) {
		getHibernateTemplate().update(transientInstance);
	}

	/**
	 * 删除指定ID的对象
	 */
	public void deleteById(ID id) {
		T instance = null;

		if (domainInspector) {
			instance = findById(id);
		}

		if (domainPo) {
			deleteToFlag(id);
		} else {

			String hql = "DELETE FROM " + getPojoClassName() + " WHERE id=?";
			this.delete(hql, id);

		}
		if (domainInspector) {
			if (instance != null) {
				((DomainInspector<T>) this).afterRemove(instance);
			}
		}
		if (loggable && domainPo) {
			String e = mls.t("remove");
			loggerDao.doLog(e, (LoggablePo) findById(id));
			// AutoLogger.DELETE.log(loggerDao, (LoggablePo) findById(id),
			// false);
		}
	}

	/**
	 * @param id
	 */
	private void deleteToFlag(ID id) {
		if (gotUpdatedTime) {
			String hql = "UPDATE " + getPojoClassName()
					+ " SET removed=?,updatedTime=? WHERE id=?";
			this.updateBatch(hql, true, Calendar.getInstance(), id);
		} else {
			String hql = "UPDATE " + getPojoClassName()
					+ " SET removed=? WHERE id=?";
			this.updateBatch(hql, true, id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.lawn.dao.BaseDao#delete(java.lang.String[])
	 */
	public void deleteById(ID[] objId) {
		if (objId == null)
			return;
		for (ID n : objId) {
			deleteById(n);
		}
	}

	/** */
	/**
	 * 删除指定对象
	 */
	public void delete(T persistentInstance) {
		getHibernateTemplate().delete(persistentInstance);
	}

	/**
	 * 删除对象根据HQL
	 * 
	 * @param hql
	 * @param id
	 */
	public int delete(final String hql, final Object... objects) {
		HibernateTemplate ht = this.getHibernateTemplate();
		// System.out.println("id=" + id);
		return (Integer) ht.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) {

				Query q = session.createQuery(hql);
				for (int i = 0; i < objects.length; i++) {
					q.setParameter(i, objects[i]);
				}
				return q.executeUpdate();

				// return null;

			}
		});

	}

	// 分页
	/** */
	/**
	 * 根据Criteria加载分页，指定页大小和起始位置
	 */
	public PaginationSupport<T> findPageByCriteria(final Criteria criteria,
			final int pageSize, final int startIndex) {
		int totalCount = getCountByCriteria(criteria);
		criteria.setProjection(null);
		List<T> items = criteria.setFirstResult(startIndex)
				.setMaxResults(pageSize).list();
		items = transformResults(items);
		PaginationSupport<T> ps = new PaginationSupport<T>(items, totalCount,
				pageSize, startIndex);
		return ps;
	}

	/** */
	/**
	 * 根据Criteria加载分页，默认页大小，从第0条开始
	 */
	public PaginationSupport<T> findPageByCriteria(final Criteria criteria) {
		return findPageByCriteria(criteria, PaginationSupport.PAGESIZE, 0);
	}

	/** */
	/**
	 * 根据Criteria加载分页，默认页大小，从第startIndex条开始
	 */
	public PaginationSupport<T> findPageByCriteria(final Criteria criteria,
			final int startIndex) {
		return findPageByCriteria(criteria, PaginationSupport.PAGESIZE,
				startIndex);
	}

	/** */
	/**
	 * 根据Criteria统计总数
	 */
	public int getCountByCriteria(final Criteria criteria) {
		Integer count = (Integer) criteria
				.setProjection(Projections.rowCount()).uniqueResult();
		return count.intValue();
	}

	/** */
	/**
	 * 根据DetachedCriteria加载分页，指定页大小和起始位置
	 */
	public <E> PaginationSupport<E> findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int startIndex, final Order... order) {
		return getHibernateTemplate().execute(
				new HibernateCallback<PaginationSupport<E>>() {
					public PaginationSupport<E> doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						CriteriaImpl impl = (CriteriaImpl) criteria;
						Projection projection = (impl).getProjection();

						ResultTransformer transformer = impl
								.getResultTransformer();

						Criteria c1 = criteria.setProjection(Projections
								.rowCount());

						Long obj = (Long) c1.uniqueResult();
						int totalCount = 0;
						if (obj == null) {

						} else {
							totalCount = obj.intValue();
						}
						if (totalCount == 0) {
							List<E> ls = Collections.emptyList();
							return new PaginationSupport<E>(ls, totalCount,
									pageSize, startIndex);
						}

						criteria.setProjection(projection);

						if (transformer != null) {
							criteria.setResultTransformer(transformer);
						}

						for (Order o : order) {
							criteria.addOrder(o);
						}
						int si = startIndex;
						if (startIndex > totalCount) {
							si = 0;
						}
						List<E> items = criteria.setFirstResult(si)
								.setMaxResults(pageSize).list();

						items = transformResults(items);
						PaginationSupport<E> ps = new PaginationSupport<E>(
								items, totalCount, pageSize, si);
						return ps;
					}
				});
	}

	/** */
	/**
	 * 根据DetachedCriteria加载分页，默认页大小，从第0条开始
	 */
	public <E> PaginationSupport<E> findPageByCriteria(
			final DetachedCriteria detachedCriteria, Order order) {
		return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE,
				0, null, order);
	}

	/** */
	/**
	 * 根据DetachedCriteria加载分页，默认页大小，从第startIndex条开始
	 */
	public <E> PaginationSupport<E> findPageByCriteria(
			final DetachedCriteria detachedCriteria, final int startIndex,
			Order order) {
		return findPageByCriteria(detachedCriteria, PaginationSupport.PAGESIZE,
				startIndex, null, order);
	}

	/**
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	public <E> PaginationSupport<E> findPageByCriteria(
			DetachedCriteria detachedCriteria, PageInfo pageInfo,
			Order... order) {
		return findPageByCriteria(detachedCriteria, pageInfo.getPageSize(),
				pageInfo.getStartIndex(), order);
	}

	/** */
	/**
	 * 根据DetachedCriteria统计总数
	 */
	public int getCountByCriteria(final DetachedCriteria detachedCriteria) {
		Long count = getHibernateTemplate().execute(
				new HibernateCallback<Long>() {
					public Long doInHibernate(Session session)
							throws HibernateException {
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return (Long) criteria.setProjection(
								Projections.rowCount()).uniqueResult();
					}
				});

		return count.intValue();
	}

	/** */
	/**
	 * 根据hql加载分页，指定页大小和起始位置
	 */
	public <E> PaginationSupport<E> findPageByQuery(final String hql,
			final int pageSize, final int startIndex, Object... values) {
		int totalCount = getCountByQuery(hql, values);

		if (totalCount < 1)
			return new PaginationSupport<E>(new ArrayList<E>(0), 0);

		Query query = createQuery(hql, values);
		List<E> items = query.setFirstResult(startIndex)
				.setMaxResults(pageSize).list();
		PaginationSupport<E> ps = new PaginationSupport<E>(items, totalCount,
				pageSize, startIndex);
		return ps;
	}

	/** */
	/**
	 * 根据hql加载分页，指定页大小和起始位置
	 */
	public <E> PaginationSupport<E> findPageByQuery(final String hql,
			final int pageSize, final int startIndex,
			PaginationSupportFactory<E> factory, Object... values) {
		int totalCount = getCountByQuery(hql, values);

		if (totalCount < 1)
			return new PaginationSupport<E>(new ArrayList<E>(0), 0);

		Query query = createQuery(hql, values);
		List<E> items = query.setFirstResult(startIndex)
				.setMaxResults(pageSize).list();
		PaginationSupport<E> ps = factory.create(items, totalCount, pageSize,
				startIndex);
		return ps;
	}

	/** */
	/**
	 * 根据hql加载分页，默认页大小，从第0条开始
	 */
	public <E> PaginationSupport<E> findPageByQuery(final String hql,
			Object... values) {
		return findPageByQuery(hql, PaginationSupport.PAGESIZE, 0, values);
	}

	public <E> PaginationSupport<E> findPageByQuery(final String hql,
			final PageInfo pageInfo, Object... values) {
		return findPageByQuery(hql, pageInfo.getPageSize(),
				pageInfo.getStartIndex(), values);

	}

	/** */
	/**
	 * 根据hql加载分页，默认页大小，从第startIndex条开始
	 */
	public <E> PaginationSupport<E> findPageByQuery(final String hql,
			final int startIndex, Object... values) {
		return findPageByQuery(hql, PaginationSupport.PAGESIZE, startIndex,
				values);
	}

	/** */
	/**
	 * 根据hql统计总数
	 */
	public int getCountByQuery(final String hql, Object... values) {
		String countQueryString = " select count (*) "
				+ SqlHelper.removeSelect(SqlHelper.removeOrders(hql));
		List<Long> countlist = getHibernateTemplate().find(countQueryString,
				values);
		if (countlist.size() == 0)
			return 0;
		return (countlist.get(0)).intValue();
	}

	// 创建Criteria和Query

	/** */
	/**
	 * 创建Criteria对象
	 * 
	 * @param criterions
	 *            可变的Restrictions条件列表
	 */
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(getPojoClass());
		for (Criterion c : criterions)
			criteria.add(c);
		return criteria;
	}

	/** */
	/**
	 * 创建Criteria对象，带排序字段与升降序字段
	 */
	public Criteria createCriteria(String orderBy, boolean isAsc,
			Criterion... criterions) {
		Criteria criteria = createCriteria(criterions);
		if (isAsc)
			criteria.addOrder(Order.asc(orderBy));
		else
			criteria.addOrder(Order.desc(orderBy));
		return criteria;
	}

	/** */
	/**
	 * 方法取自SpringSide. 创建Query对象.
	 * 对于需要first,max,fetchsize,cache,cacheRegion等诸多设置的函数,可以在返回Query后自行设置.
	 * 留意可以连续设置,如下：
	 * 
	 * <pre>
	 * dao.getQuery(hql).setMaxResult(100).setCacheable(true).list();
	 * </pre>
	 * 
	 * 调用方式如下：
	 * 
	 * <pre>
	 * 
	 *        dao.createQuery(hql)   
	 *        dao.createQuery(hql,arg0);   
	 *        dao.createQuery(hql,arg0,arg1);   
	 *        dao.createQuery(hql,new Object[arg0,arg1,arg2])
	 * </pre>
	 * 
	 * @param values
	 *            可变参数.
	 */
	public Query createQuery(String hql, Object... values) {
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		return query;
	}

	/** */
	/**
	 * 将联合查询的结果内容从Map或者Object[]转换为实体类型，如果没有转换必要则直接返回
	 */
	private <C, E> List<E> transformResults(List<C> items) {

		return (List<E>) items;

		// if (items.size() > 0) {
		// if (items.get(0) instanceof Map) {
		// List<E> list = new ArrayList<E>(items.size());
		// for (int i = 0; i < items.size(); i++) {
		// Map<String, E> map = (Map<String, E>) items.get(i);
		// list.add(map.get(CriteriaSpecification.ROOT_ALIAS));
		// }
		// return list;
		// } else if (items.get(0) instanceof Object[]) {
		// List<E> list = new ArrayList<E>(items.size());
		// int pos = -1;
		// for (int i = 0; i < ((Object[]) items.get(0)).length; i++) {
		// if (((Object[]) items.get(0))[i].getClass() == getPojoClass()) {
		// pos = i;
		// break;
		// }
		// }
		// if (pos >= 0) {
		// for (int i = 0; i < items.size(); i++) {
		// list.add(((E[]) items.get(i))[pos]);
		// }
		// return list;
		// }
		//
		// return (List<E>) items;
		//
		// } else
		// return (List<E>) items;
		// } else
		// return (List<E>) items;
	}

	public boolean existsNo(String no) {
		DetachedCriteria dc = DetachedCriteria.forClass(pojoClass);

		dc.add(Restrictions.eq("no", no));
		dc.setProjection(Projections.property("id"));
		if (domainPo) {
			dc.add(Restrictions.eq("removed", false));
		}
		Object id = this.uniqueResult(dc);

		return (id != null);
	}

	public boolean existsNo(String no, ID filterId) {
		DetachedCriteria dc = DetachedCriteria.forClass(pojoClass);

		dc.add(Restrictions.eq("no", no));
		dc.add(Restrictions.ne("id", filterId));

		if (domainPo) {
			dc.add(Restrictions.eq("removed", false));
		}
		int n = this.getCountByCriteria(dc);

		return n > 0;
	}

	/**
	 * @param map
	 * @param f
	 * @return
	 */
	public boolean exists(Map<String, Object> map, Map<String, Object> f) {

		DetachedCriteria dc = DetachedCriteria.forClass(pojoClass);
		for (String key : map.keySet()) {
			dc.add(Restrictions.eq(key, map.get(key)));
		}

		for (String key : f.keySet()) {
			dc.add(Restrictions.ne(key, f.get(key)));
		}

		int n = this.getCountByCriteria(dc);

		return n > 0;
	}

	public boolean exists(Map<String, Object> map) {
		DetachedCriteria dc = DetachedCriteria.forClass(pojoClass);
		for (String key : map.keySet()) {
			dc.add(Restrictions.eq(key, map.get(key)));
		}
		dc.setProjection(Projections.id());
		List<Object> list = this.findLimitByCriteria(dc, 1);

		return list.size() > 0;
	}

	/***************************************************************************
	 * UPDATE
	 **************************************************************************/
	public int updateBatch(final String hql, final Object... objs) {
		HibernateTemplate ht = this.getHibernateTemplate();
		// logger.debug(hql);
		// System.out.println("id=" + id);
		return ht.execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) {

				Query q = session.createQuery(hql);

				if (objs != null)
					for (int i = 0; i < objs.length; i++) {
						q.setParameter(i, objs[i]);
					}

				int i = q.executeUpdate();
				return i;

			}
		});

		// return i;
	}

	/***************************************************************************
	 * DetachedCriteria Base Maker
	 **************************************************************************/
	public DetachedCriteria makeDetachedCriteria(CommonCondition condition) {
		DetachedCriteria dc = DetachedCriteria.forClass(this.pojoClass, "uu");
		if (!StringUtils.isEmpty(condition.getNo())) {

			dc.add(Restrictions.like("no", condition.getNo().trim(),
					MatchMode.ANYWHERE));
		}

		if (!StringUtils.isEmpty(condition.getName())) {
			dc.add(Restrictions.like("name", condition.getName().trim(),
					MatchMode.ANYWHERE));
		}

		if (domainPo) {
			dc.add(Restrictions.eq("removed", false));
		}

		return dc;
	}

	/***************************************************************************
	 * Batch Update
	 **************************************************************************/
	/**
	 * @param objId
	 * @param enable
	 */
	public void doEnable(ID[] objId, boolean enable) {

		if (objId == null) {
			return;
		}
		if (this.gotUpdatedTime) {
			String hql = "UPDATE " + this.getPojoClassName()
					+ " SET enabled=?,updatedTime=? WHERE id=?";
			for (ID id : objId) {

				this.updateBatch(hql, enable, Calendar.getInstance(), id);
				if (domainInspector) {
					((DomainInspector<T>) this).afterEnable(this.findById(id),
							enable);
				}
				if (loggable) {
					String e = PewUtils.toEnabledString(mls, enable);
					loggerDao.doLog(e, (LoggablePo) this.findById(id));

				}

			}
		} else {
			String hql = "UPDATE " + this.getPojoClassName()
					+ " SET enabled=? WHERE id=?";
			for (ID id : objId) {

				this.updateBatch(hql, enable, id);

				if (domainInspector) {
					((DomainInspector<T>) this).afterEnable(this.findById(id),
							enable);
				}

				if (loggable) {
					String e = PewUtils.toEnabledString(mls, enable);
					loggerDao.doLog(e, (LoggablePo) this.findById(id));

				}
			}
		}

	}

	/**
	 * @param class1
	 * @param no
	 * @return
	 */
	public <E> E findByNo(Class<E> clazz, String no) {

		String hql = "FROM " + clazz.getName() + " WHERE no=? AND removed=?";
		List<E> objs = find(hql, no, false);
		if (objs.size() == 0) {
			return null;
		}
		return objs.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.lawn.dao.BaseDao#doVisible(ID[], boolean)
	 */
	public void doVisible(ID[] objId, boolean visible) {
		if (objId == null) {
			return;
		}

		String hql = "UPDATE " + this.getPojoClassName()
				+ " SET visible=? WHERE id=?";
		for (ID id : objId) {

			this.updateBatch(hql, visible, id);
			if (domainInspector) {
				((DomainInspector<T>) this).afterVisible(this.findById(id),
						visible);
			}

			if (loggable) {
				String e = PewUtils.toVisibleString(mls, visible);
				loggerDao.doLog(e, (LoggablePo) this.findById(id));

			}
		}
	}

	/**
	 * @param hql
	 * @param id
	 * @return
	 */
	public <E> E findSingle(String hql, Object... obj) {
		// List<E> lst = find(hql, obj);
		// if (lst.size() <= 0) {
		// return null;
		// }
		// return lst.get(0);

		List<E> es = this.findLimitByQuery(hql, 1, obj);
		if (es.size() > 0) {
			return es.get(0);
		}
		return null;
	}

	public T findRef(String hql, Object... obj) {
		// List<E> lst = find(hql, obj);
		// if (lst.size() <= 0) {
		// return null;
		// }
		// return lst.get(0);

		List<ID> es = this.findLimitByQuery(hql, 1, obj);
		if (es.size() > 0) {
			ID e = es.get(0);
			if (e == null) {
				return null;
			}

			try {
				IdentifyDomain<ID> po = (IdentifyDomain<ID>) pojoClass
						.newInstance();
				po.setId(e);
				return (T) po;
			} catch (InstantiationException e1) {

				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			}

		}
		return null;
	}

	public <E> E findRow(String hql, Object... obj) {
		List<E> lst = find(hql, obj);
		if (lst.size() <= 0) {
			return null;
		}
		return lst.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.lawn.dao.BaseDao#loadEnabled()
	 */
	public List<T> loadEnabled() {
		String hql = "FROM " + getPojoClassName()
				+ " WHERE removed=? AND enabled=? ORDER BY no";
		return find(hql, false, true);
	}

	/**
	 * @param dc
	 * @param i
	 * @param makeOrderBy
	 * @return
	 */
	public <E> List<E> findLimitByCriteria(final DetachedCriteria dc,
			final int i, final Order... order) {
		return getHibernateTemplate().execute(new HibernateCallback<List<E>>() {
			public List<E> doInHibernate(Session session)
					throws HibernateException {
				Criteria criteria = dc.getExecutableCriteria(session);

				for (Order o : order) {
					criteria.addOrder(o);
				}
				List<E> items = criteria.setFirstResult(0).setMaxResults(i)
						.list();
				// items = transformResults(items);

				return items;
			}
		});
	}

	public <E> List<E> findLimitByQuery(String hql, int i, Object... values) {
		Query query = createQuery(hql, values);
		List<E> items = query.setFirstResult(0).setMaxResults(i).list();
		return items;
	}

	public int getInt(String hql, Object... objs) {
		Object obj = findSingle(hql, objs);
		if (obj == null) {
			return 0;
		} else {
			if (obj instanceof Integer) {
				return (Integer) obj;
			} else {
				return ((Long) obj).intValue();
			}
		}

	}

	/**
	 * @param class1
	 * @param id
	 * @return
	 */
	public <E> E findById(Class<E> class1, Serializable id) {
		return (E) getHibernateTemplate().get(class1, id);
	}

	/**
	 * @param condition
	 * @return
	 */
	public Order createOrderBy(OrderBySupport condition) {
		if (condition.isDesc()) {
			return (Order.desc(condition.getOrderBy()));
		} else {
			return (Order.asc(condition.getOrderBy()));
		}
	}

	/**
	 * @param id
	 * @return
	 */
	public boolean existsId(String id) {
		String hql = "SELECT id FROM " + getPojoClassName() + " WHERE id=?";
		return findSingle(hql, id) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxl.basement.bonding.dao.BaseDao#findTarget(java.lang.String,
	 * java.lang.String)
	 */
	public <E> E findTarget(String target, String where, Object... objs) {
		String hql = "SELECT " + target + " FROM " + this.getPojoClassName()
				+ " WHERE " + where;

		return findSingle(hql, objs);
	}

	/**
	 * @param dc
	 * @return
	 */
	public <E> E uniqueResult(final DetachedCriteria dc) {
		return getHibernateTemplate().execute(new HibernateCallback<E>() {
			public E doInHibernate(Session session) throws HibernateException {
				Criteria criteria = dc.getExecutableCriteria(session);
				return (E) criteria.uniqueResult();
			}
		});
	}

	public ID getIdByNo(String no) {
		String hql = "select id from " + getPojoClassName()
				+ " where no=? AND removed=?";
		return findSingle(hql, no, false);
	}

	public String getNoById(ID id) {
		String hql = "select no from " + getPojoClassName() + " where id=? ";
		return findSingle(hql, id);
	}

	public boolean exists(String hql, Object... objs) {
		return findSingle(hql, objs) != null;
	}

	public <E> List<E> jdbcFind(final String sql, final Object... params) {
		return getHibernateTemplate().executeFind(
				new HibernateCallback<List<E>>() {

					public List<E> doInHibernate(Session session)
							throws HibernateException, SQLException {
						Query query = session.createSQLQuery(sql);
						if (params != null) {
							for (int i = 0; i < params.length; i++) {
								query.setParameter(i, params[i]);
							}
						}

						// if (rowNumber > 0) {
						// query.setFirstResult(firstReslult);
						// query.setMaxResults(rowNumber);
						// }

						return query.list();
					}

				});
	}

	public MapList mapList(final String sql, final Object... params) {
		return getHibernateTemplate().execute(new HibernateCallback<MapList>() {

			@Override
			public MapList doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}

				return MapList.create((List<Map<String, Object>>) q
						.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list());
			}

		});
	}

	public MapBean mapBean(final String sql, final Object... params) {
		return getHibernateTemplate().execute(new HibernateCallback<MapBean>() {

			@Override
			public MapBean doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(sql);

				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						q.setParameter(i, params[i]);
					}
				}

				q.setMaxResults(1);

				List<Map<String, Object>> list = (List<Map<String, Object>>) q
						.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
						.list();

				if (list.size() > 0) {
					MapBean mb = new MapBean();
					mb.putAll(list.get(0));
					return mb;
				}

				return null;

			}

		});
	}

	public MapList mapListLimit(String hql, int i, Object... parameters) {
		Query q = createQuery(hql, parameters);

		return MapList.create((List<Map<String, Object>>) q
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setFirstResult(0).setMaxResults(i).list());

	}
}
