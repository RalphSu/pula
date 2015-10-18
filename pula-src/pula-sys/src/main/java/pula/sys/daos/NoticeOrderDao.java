/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.NoticeOrderCondition;
import pula.sys.domains.NoticeOrder;

/**
 * @author Liangfei
 *
 */
public interface NoticeOrderDao extends BaseDao<NoticeOrder, Long>  {

    PaginationSupport<NoticeOrder> search(NoticeOrderCondition condition, int pageIndex);

    NoticeOrder save(NoticeOrder cc);

    NoticeOrder update(NoticeOrder cc);
}
