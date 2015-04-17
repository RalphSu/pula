package pula.sys.daos;

import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.support.vo.Mix;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.OrderFormCondition;
import pula.sys.domains.OrderForm;
import pula.sys.domains.StudentPoints;

public interface OrderFormDao extends BaseDao<OrderForm, Long> {

	OrderForm save(OrderForm cc);

	void checkAllowEdit(long id, long branchId);

	Mix<OrderForm, StudentPoints> update(OrderForm cc, boolean hq);

	void checkAllowView(Long id, long branchId);

	PaginationSupport<MapBean> search(OrderFormCondition condition,
			int pageIndex);

	void confirm(Long[] id, long branchId, String actorId);

	MapList precheckForRemove(Long[] id, long branchId);

	String getOpenOrderForm(Long student, long id);

	void checkAllowChargeback(Long id, long branch_id);

	Map<String, MapBean> stat4Salesman(int year, int month, long branchId);

	MapList stat4Monthly(int year, long branchId);

	Map<String, MapBean> stat4Teacher(int year, int java_month, long branchId);

	void consumeRestore(Long asLong);

	MapBean meta4consumeRestore(Long studentId);

	MapBean meta4consume(long studentId);

	void complete(Long asLong);

	void consume(Long asLong);

}
