/**
 * 
 */
package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.NoticeCondition;
import pula.sys.domains.Notice;

/**
 * @author Liangfei
 *
 */
public interface NoticeDao extends BaseDao<Notice, Long> {

    PaginationSupport<Notice> search(NoticeCondition condition, int pageIndex);

    Notice save(Notice cc);

    Notice update(Notice cc);
}
