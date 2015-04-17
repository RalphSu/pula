package puerta.system.dao;

import puerta.support.dao.BaseDao;
import puerta.system.po.SystemLog;

public interface SystemLogDao extends BaseDao<SystemLog, String> {

	void save(String event, String extend);

}
