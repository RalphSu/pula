package pula.sys.daos;

import java.util.List;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.CourseProductCondition;
import pula.sys.domains.CourseProduct;

public interface CourseProductDao extends BaseDao<CourseProduct, Long> {

	PaginationSupport<CourseProduct> search(CourseProductCondition condition,
			int pageIndex);

	CourseProduct save(CourseProduct cc);

	CourseProduct update(CourseProduct cc);

	MapList loadByKeywords(String no, String t, String prefix);

	Long getIdByNo(String mno);

	List<CourseProduct> listByBranch(long branchId);

	MapBean meta4order(long courseProductId, long branch_id);

}
