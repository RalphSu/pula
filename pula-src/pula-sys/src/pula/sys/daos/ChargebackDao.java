package pula.sys.daos;

import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.conditions.ChargebackCondition;
import pula.sys.domains.Chargeback;

public interface ChargebackDao extends BaseDao<Chargeback, Long> {

	boolean hasChargeback(Long orderform_id, long filter_id);

	Chargeback save(Chargeback cc);

	void checkAllowEdit(long id, long branch_id);

	PaginationSupport<MapBean> search(ChargebackCondition condition,
			int pageIndex);

	void confirm(Long[] id, long branchId, String actorId);

	void precheckForRemove(Long[] id, long branchId);

	void checkAllowView(Long id, long branchId);

	Chargeback update(Chargeback cc);

	Map<String, MapBean> stat4Salesman(int year, int month, long branchId);

	MapList stat4Monthly(int year, long branchId);

	Map<String, MapBean> stat4Teacher(int year, int java_month, long branchId);

}
