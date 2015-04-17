package puerta.system.dao;

import java.util.List;

import puerta.support.dao.BaseDao;
import puerta.system.po.RequestHistory;

public interface RequestHistoryDao extends BaseDao<RequestHistory, Long> {

	void save(RequestHistory rh);

	List<RequestHistory> listHistory(String purviewId);

}
