/*
 * Created on 2005-7-11
 *$Id: OrderNoRuleDaoImpl.java,v 1.8 2007/01/09 07:44:30 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import puerta.support.PageInfo;
import puerta.support.PaginationSupport;
import puerta.support.mls.Mls;
import puerta.support.utils.DateExTool;
import puerta.support.utils.StringTool;
import puerta.system.base.HibernateGenericDao;
import puerta.system.condition.OrderNoRuleCondition;
import puerta.system.dao.LoggerDao;
import puerta.system.dao.OrderNoRuleDao;
import puerta.system.flag.OrderNoMutex;
import puerta.system.intfs.JDOMTransfer;
import puerta.system.keeper.OrderNoMutexKeeper;
import puerta.system.po.OrderNoLog;
import puerta.system.po.OrderNoRule;
import puerta.system.transfer.OrderNoRuleTransfer;

/**
 * @author tiyi 2005-7-11 19:59:55
 */

@Repository
public class OrderNoRuleDaoImpl extends
		HibernateGenericDao<OrderNoRule, String> implements OrderNoRuleDao {

	@Resource
	private LoggerDao loggerDao;
	@Resource
	private OrderNoMutexKeeper orderNoMutexKeeper;
	@Resource
	private Mls mls;

	public OrderNoMutexKeeper getOrderNoMutexKeeper() {
		return orderNoMutexKeeper;
	}

	public void setOrderNoMutexKeeper(OrderNoMutexKeeper orderNoMutexKeeper) {
		this.orderNoMutexKeeper = orderNoMutexKeeper;
	}

	public LoggerDao getLoggerDao() {
		return loggerDao;
	}

	public void setLoggerDao(LoggerDao loggerDao) {
		this.loggerDao = loggerDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.module.OrderNoRuleDao#update(com.nut.groundwork
	 * .platform.po.OrderNoRule)
	 */
	public OrderNoRule update(OrderNoRule m) {

		OrderNoRule po = null;
		if (StringUtils.isEmpty(m.getId())) {
			po = this.findByNo(m.getNo());
		} else {
			po = this.findById(m.getId());
		}

		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id", po.getId());
		boolean e = super.exists(v, filter);

		if (e)

			Mls.raise("platform.ordernorule.noExists", m.getNo());

		// m.setNo(m.getNo());
		// m.setName(m.getName());
		// m.setCacheRule(m.getCacheRule());

		po.setNo(m.getNo());
		po.setName(m.getName());
		po.setCacheRule(m.getCacheRule());
		po.setDateFormat(m.getDateFormat());
		po.setNoLength(m.getNoLength());
		po.setPrefix(m.getPrefix());
		po.setReCountByDay(m.isReCountByDay());
		// po.setRemoved(m.isRemoved());
		po.setSuffix(m.getSuffix());

		loggerDao.doLog(mls.t("platform.ordernorule.edit"),
				StringTool.getBean(m));
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.module.OrderNoRuleDao#save(com.nut.groundwork
	 * .platform.po.OrderNoRule)
	 */
	public OrderNoRule save(OrderNoRule m) {
		Map<String, Object> v = new HashMap<String, Object>();
		v.put("no", m.getNo());
		boolean e = super.exists(v);

		if (e) {
			// String s= "shutdown";

			Mls.raise("platform.ordernorule.noExists", m.getNo());

			// System.out.println("Exception="+ex.getMessage());
			// throw ex;
		}

		getHibernateTemplate().save(m);
		loggerDao.doLog(mls.t("platform.ordernorule.add"),
				StringTool.getBean(m));
		return m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.platform.module.OrderNoRuleDao#search(com.nut.groundwork
	 * .commonCommonCondition, com.nut.groundwork.commonPageInfo)
	 */
	public PaginationSupport<OrderNoRule> search(
			OrderNoRuleCondition condition, int pageNo) {

		DetachedCriteria criteria = makeDetachedCriteria(condition);
		return super.findPageByCriteria(criteria, PageInfo.createStart(pageNo),
				Order.asc("no"));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.common.ioc.BaseDaoImpl#afterRemove(java.lang.Object)
	 */

	public void afterRemove(OrderNoRule obj) {
		OrderNoRule m = (OrderNoRule) obj;

		loggerDao.doLog(mls.t("platform.ordernorule.remove"),
				StringTool.getBean(m));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.common.ioc.BusiInspecter#afterEnable(java.lang.Object)
	 */
	public void afterEnable(OrderNoRule obj, boolean b) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nut.groundwork.common.ioc.BusiInspecter#afterVisible(java.lang.Object
	 * )
	 */
	public void afterVisible(OrderNoRule obj, boolean v) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.groundwork.platform.ordernorule.OrderNoRuleDao#loadAll()
	 */
	public List<OrderNoRule> loadAll() {
		String hql = "FROM OrderNoRule p WHERE p.removed=? ORDER BY p.no";
		return find(hql, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nut.groundwork.platform.ordernorule.OrderNoRuleDao#doClear()
	 */
	public void doClear() {
		String hql = "DELETE FROM OrderNoLog";
		super.delete(hql);

		hql = "DELETE FROM OrderNoRule";
		super.delete(hql);

	}

	public OrderNoRule getOrderNoRule(String no) {
		return (OrderNoRule) this.findByNo(OrderNoRule.class, no);
	}

	public void doImport(List<OrderNoRule> ordernorules) {
		// loggerDao.disableLog();
		for (Iterator<OrderNoRule> iter = ordernorules.iterator(); iter
				.hasNext();) {
			OrderNoRule r = iter.next();
			save(r);

		}

		// loggerDao.enableLog();
		// loggerDao.doLog(mls.t("platform.ordernorule.import"),
		// mls.t("platform.shortcut.total") + ordernorules.size());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.ordernorule.OrderNoRuleDao#doAppend(java
	 * .util.List)
	 */
	public void doAppend(List<OrderNoRule> ordernorules) {
		// loggerDao.disableLog();
		for (Iterator<OrderNoRule> iter = ordernorules.iterator(); iter
				.hasNext();) {
			OrderNoRule r = iter.next();
			OrderNoRule po = (OrderNoRule) this.findByNo(OrderNoRule.class,
					r.getNo());
			if (po != null) {
				r.setId(po.getId());
				update(r);
			} else {
				save(r);
			}
		}

		// loggerDao.enableLog();
		// loggerDao.doLog(mls.t("platform.ordernorule.import"),
		// mls.t("platform.shortcut.total") + ordernorules.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.ordernorule.OrderNoRuleDao#loadLogs(java
	 * .lang.String)
	 */
	public List<OrderNoLog> loadLogs(final String ruleNo) {
		String hql = "SELECT u FROM OrderNoLog u WHERE u.no=? ORDER BY u.logTime desc ";
        PaginationSupport<OrderNoLog> ps = this.findPageByQuery(hql, new Object[] { 20, 0, ruleNo });

		return ps.getItems();

	}

	// Map<Integer, Integer> map = WxlSugar.newHashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * wxl.pew.platform.orderrule.OrderNoRuleDao#doFetchReadyNo(wxl.lawn.dao
	 * .OrderNoMutex)
	 */
	public String doFetchReadyNo(OrderNoMutex mutex) {

		int counter = 0;
		synchronized (mutex) {

			// if (map.containsKey(mutex.getNowCount())) {
			// System.err.println(">>>>>>>>" + mutex.getNowCount());
			// } else {
			// map.put(mutex.getNowCount(), mutex.getNowCount());
			// }

			// 加载规则
			if (mutex.getRule() == null) {
				OrderNoRule rule = this.findOrderNoRule(mutex.getNo());
				logger.debug("load OrderNoRule from Db:" + rule.getId() + " @="
						+ mutex.toString());
				mutex.setRule(rule);
			}

			Calendar today = DateExTool.today();
			int dayOfNow = today.get(Calendar.DAY_OF_MONTH);
			OrderNoLog log = null;
			boolean needLoadLog = false;
			boolean needScroll = false;
			boolean needLoadSingleLog = false;

			// 检查是否需要把日志+缓存规则（就是当前计数是否消耗完）
			int cacheRule = mutex.getRule().getCacheRule();
			if (cacheRule <= 0)
				cacheRule = 1;

			if (mutex.getCounter() % cacheRule == 0) {
				// 余数为0 表示满了
				// 例如 1000 % 1000 = 0 , 0 % 1000 = 0
				logger.debug("orderno::mutex is full[" + mutex.getCounter()
						+ "] of cache[" + cacheRule + "] , need Scroll="
						+ mutex.toString());
				needScroll = true;
			}

			// 日期变更
			if (mutex.getRule().isReCountByDay() && dayOfNow != mutex.getDay()) {
				logger.debug("orderno::not same day , need Scroll&needLoadLog ="
						+ mutex.toString());
				// 变日期，加载一次
				needLoadLog = true;
				// 变日期，要滚动
				needScroll = true;
			}

			if (!mutex.getRule().isReCountByDay() && mutex.getDay() == 0) {
				// 第一次
				// 因此nowCount 永远为0 ，所以 needScroll 永远为true
				logger.debug("orderno::not recount everyday, needLoadSingleLog ="
						+ mutex.toString());
				needLoadSingleLog = true;
				// needScroll = true;
			}

			// 变了日期
			if (needLoadLog) {
				// 找一下看存在与否
				String hql = "Select po FROM OrderNoLog as po WHERE po.no=? and po.date=?";

				List<OrderNoLog> lst = find(hql, mutex.getRule().getNo(), today);

				Iterator<OrderNoLog> i = lst.iterator();

				if (i.hasNext()) {
					log = (OrderNoLog) i.next();
					logger.debug("load OrderNoLog from Db :" + log.getCounter()
							+ " @=" + mutex.toString());

				}

				mutex.setDay(dayOfNow); // 设置成当前日期

			} else if (needLoadSingleLog) {
				// 不是按日重计，但是还未初始化，一样要加载一下日志
				String hql = "Select po FROM OrderNoLog "
						+ " as po WHERE po.no=? ";

				List<OrderNoLog> lst = find(hql, mutex.getRule().getNo());
				Iterator<OrderNoLog> i = lst.iterator();

				if (i.hasNext()) {
					log = (OrderNoLog) i.next();
					logger.debug("load OrderNoLog from Db[Single] :"
							+ log.getCounter() + " @=" + mutex.toString());
				}
				needLoadLog = true;
				mutex.setDay(dayOfNow); // 设置成当前日期
			}

			// 日志为空,并且需要访问日志，所以新增一个
			if (log == null && (needLoadLog)) {
				log = new OrderNoLog();
				log.setDate(today);
				log.setNo(mutex.getRule().getNo());
				log.setLogTime(Calendar.getInstance());
				needScroll = true;
				getHibernateTemplate().save(log);

				logger.debug("load OrderNoLog from Db but Null ,make New :"
						+ log.getCounter() + " @=" + mutex.toString());

			}

			if (needLoadLog && log != null) {
				// 要从数据库中加载
				// System.err.println(" reset now count=" + log.getCounter());
				mutex.setCounter(log.getCounter()); // init the count
				mutex.setLogId(log.getId());

				logger.debug("orderno::set mutex counter & logId :"
						+ log.getCounter() + " @=" + mutex.toString());

			}

			if (needScroll) {
				// 0+1000
				// 以当前计数器为准
				// log.setCounter(mutex.getNowCount() + cacheRule);
				// getHibernateTemplate().flush();
				String hql = "UPDATE OrderNoLog SET counter=? WHERE id=?";
				updateBatch(hql, mutex.getCounter() + cacheRule,
						mutex.getLogId());
				logger.debug("orderno::flush order no to db ="
						+ (mutex.getCounter() + cacheRule) + " @="
						+ mutex.toString());
			}
			// 开始自增

			mutex.incCounter();
			// logger.debug("***order no append=" + mutex.getNowCount());
			counter = mutex.getCounter();
			logger.debug("orderno::complete get no =" + counter);

		} // finish lock

		OrderNoRule rule = mutex.getRule();

		StringBuilder nosb = new StringBuilder(20);

		// prefix
		nosb.append(StringUtils.defaultString(rule.getPrefix()));

		// add date mask
		if (!StringUtils.isEmpty(rule.getDateFormat())) {
			SimpleDateFormat format = new SimpleDateFormat(rule.getDateFormat());
			Date date = Calendar.getInstance().getTime();
			nosb.append(format.format(date));
		}

		// counter
		nosb.append(StringTool.fillZero(counter, rule.getNoLength()));

		// suffix
		nosb.append(StringUtils.defaultString(rule.getSuffix()));

		return nosb.toString();
	}

	/**
	 * @param no
	 * @return
	 */
	private OrderNoRule findOrderNoRule(String no) {
		String hql = "FROM OrderNoRule r WHERE r.no=? AND r.removed=?";
		List<OrderNoRule> l = find(hql, new Object[] { no, false });
		if (l.size() > 0) {
			return (OrderNoRule) l.get(0);
		} else {
			Mls.raise("platform.ordernorule.noNotExists", no);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nilnut.groundwork.platform.aide.AideDao#getReadyNo(java.lang.String)
	 */
	public String doFetchReadyNo(String ruleNo) {
		OrderNoMutex mutex = orderNoMutexKeeper.getKey(ruleNo);
		if (mutex == null) {
			Mls.raise("platform.ordernorule.mutexNotExists", ruleNo);
		}
		return doFetchReadyNo(mutex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doExports()
	 */
	public List<OrderNoRule> doExports(String afNo) {
		return this.loadAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#doImport(java.util.List,
	 * boolean)
	 */
	public void doImport(List<OrderNoRule> objs, boolean update) {
		if (update) {
			this.doAppend(objs);
		} else {
			this.doImport(objs);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getPluginName()
	 */
	public String getPluginName() {
		return this.getPojoClassName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see wxlplatform.setting.IWxlPluginSetting#getTransfer()
	 */
	public JDOMTransfer<OrderNoRule> getTransfer() {
		return new OrderNoRuleTransfer();
	}

	// Map<Calendar, Map<Integer, Integer>> map = WxlSugar.newHashMap();

	public String doFetchReadyNo(OrderNoMutex mutex, Calendar orderDate) {
		int counter = 0;

		synchronized (mutex) {
			String day = DateExTool.date2String(orderDate);
			counter = mutex.getCounter();

			// Map<Integer, Integer> m = map.get(orderDate);
			// if (m == null) {
			// m = new HashMap<Integer, Integer>();
			// map.put(orderDate, m);
			// }
			//
			// if (m.containsKey(counter)) {
			// System.err.println(">>>>>>>>" + counter + " day=" + day);
			// } else {
			// m.put(counter, counter);
			// }

			// 加载规则
			if (mutex.getRule() == null) {

				OrderNoRule rule = this.findOrderNoRule(mutex.getNo());
				logger.debug("load OrderNoRule from Db:" + rule.getId() + " @="
						+ mutex.toString());
				mutex.setRule(rule);
			}

			int cacheRule = mutex.getRule().getCacheRule();
			boolean flushToDb = false;
			// 先判断当前日期和信号量是不是统一天
			if (StringUtils.equals(day, mutex.getLastDay())) {
				// 同一天的话，直接加数字
				// System.out.println("same day " + day);
			} else {
				// 已经加载了这个数据
				if (mutex.loadCounter(day)) {

				} else {
					// 还未加载过,要从db中获取

					String hql = "Select po FROM OrderNoLog as po WHERE po.no=? and po.date=?";
					List<OrderNoLog> lst = find(hql, mutex.getRule().getNo(),
							orderDate);

					Iterator<OrderNoLog> i = lst.iterator();
					OrderNoLog log = null;
					if (i.hasNext()) {
						log = (OrderNoLog) i.next();

						logger.debug("load OrderNoLog from Db:"
								+ log.getCounter() + " @=" + mutex.toString());
					}

					if (log == null) {
						log = new OrderNoLog();
						log.setDate(orderDate);
						log.setNo(mutex.getRule().getNo());
						log.setLogTime(Calendar.getInstance());
						log.setCounter(cacheRule);
						getHibernateTemplate().save(log);
						mutex.loadCounter(day, 0, log.getId());

						logger.debug("load OrderNoLog from Db but Null ,make New :"
								+ log.getCounter() + " @=" + mutex.toString());
					} else {

						// 旧的先存起来

						// 新的再加载
						mutex.loadCounter(day, log.getCounter(), log.getId());
						flushToDb = true;
					}

				}
			}

			// 开始自增

			// logger.debug("***order no append=" + mutex.getNowCount());

			// logger.debug("order no flushed");

			// flush
			if (counter % cacheRule == 0 || flushToDb) {
				// 用完了

				// 需要存到数据库了
				String hql = "UPDATE OrderNoLog SET counter=? WHERE id=?";
				updateBatch(hql, counter + cacheRule, mutex.getLogId());

				logger.debug("flush order no to db ="
						+ (mutex.getCounter() + cacheRule) + " @="
						+ mutex.toString());
			}

			counter = mutex.incCounter();

		} // finish lock

		OrderNoRule rule = mutex.getRule();

		StringBuilder nosb = new StringBuilder(20);

		// prefix
		nosb.append(StringUtils.defaultString(rule.getPrefix()));

		// add date mask
		if (!StringUtils.isEmpty(rule.getDateFormat())) {
			SimpleDateFormat format = new SimpleDateFormat(rule.getDateFormat());
			Date date = orderDate.getTime();
			nosb.append(format.format(date));
		}

		// counter
		nosb.append(StringTool.fillZero(counter, rule.getNoLength()));

		// suffix
		nosb.append(StringUtils.defaultString(rule.getSuffix()));

		return nosb.toString();

	}
}
