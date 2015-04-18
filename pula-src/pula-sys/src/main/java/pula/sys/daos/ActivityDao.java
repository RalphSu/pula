/**
 * Created on 2009-7-23
 * WXL 2009
 * $Id$
 */
package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapList;
import pula.sys.conditions.ActivityCondition;
import pula.sys.domains.Activity;

/**
 * 
 * @author tiyi
 * 
 */
public interface ActivityDao extends BaseDao<Activity, Long> {

	/**
	 * @param Activity
	 * @return
	 */
	Activity save(Activity Activity);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<Activity> search(ActivityCondition condition, int pageNo);

	/**
	 * @param Activity
	 * @return
	 */
	Activity update(Activity Activity);

	MapList loadMeta();

	List<Activity> loadByKeywords(String no, long branchId);

	void saveBranch(long id, Long[] branchId, boolean b);

}
