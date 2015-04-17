package pula.sys.daos;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.SalesmanCondition;
import pula.sys.domains.Salesman;

public interface SalesmanDao extends BaseDao<Salesman, Long> {

	PaginationSupport<Salesman> search(SalesmanCondition condition,
			int pageIndex);

	Salesman save(Salesman cc);

	Salesman update(Salesman cc);

	//MapList loadByKeywords(String no, String t, String prefix);

	Long getIdByNo(String mno);

	MapBean meta4order(String slaveSalesmanNo, long branch_id);

	MapList loadByKeywords(String no, long branchId);

}
