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
import pula.sys.conditions.BranchCondition;
import pula.sys.domains.Branch;

/**
 * 
 * @author tiyi
 * 
 */
public interface BranchDao extends BaseDao<Branch, Long> {

	/**
	 * @param Branch
	 * @return
	 */
	Branch save(Branch Branch);

	/**
	 * @param condition
	 * @param pageNo
	 * @return
	 */
	PaginationSupport<Branch> search(BranchCondition condition, int pageNo);

	/**
	 * @param Branch
	 * @return
	 */
	Branch update(Branch Branch);

	/**
	 * @param no
	 * @return
	 */
	List<Branch> loadByKeywords(String no);

	boolean isHeadQuarter(Long i);

	MapList loadMeta();

	MapList loadMetaWithoutHeadquarter();

	String getPrefix(long idLong);

}
