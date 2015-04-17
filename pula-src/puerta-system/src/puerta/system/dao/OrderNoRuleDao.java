/**
 * Created on 2007-6-6 09:26:49
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.dao;

import java.util.Calendar;
import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.condition.OrderNoRuleCondition;
import puerta.system.flag.OrderNoMutex;
import puerta.system.intfs.IWxlPluginSetting;
import puerta.system.po.OrderNoLog;
import puerta.system.po.OrderNoRule;

/**
 * 
 * @author tiyi
 * 
 */
public interface OrderNoRuleDao extends BaseDao<OrderNoRule, String>,
		IWxlPluginSetting<OrderNoRule> {

	/**
	 * @param condition
	 * @param pageInfo
	 * @return
	 */
	PaginationSupport<OrderNoRule> search(OrderNoRuleCondition condition,
			int pageNo);

	OrderNoRule save(OrderNoRule rule);

	OrderNoRule update(OrderNoRule rule);

	/**
	 * @param ruleId
	 * @return
	 */
	List<OrderNoLog> loadLogs(String ruleNo);

	/**
	 * @return
	 */
	List<OrderNoRule> loadAll();

	/**
	 * @param rules
	 */
	void doAppend(List<OrderNoRule> rules);

	/**
	 * @param rules
	 */
	void doImport(List<OrderNoRule> rules);

	String doFetchReadyNo(OrderNoMutex mutex);

	/**
	 * @param ruleNo
	 * @return
	 */
	String doFetchReadyNo(String ruleNo);

	String doFetchReadyNo(OrderNoMutex mutex, Calendar orderDate);

	/**
	 * 
	 */
	void doClear();
}
