package pula.sys.daos;

import java.util.List;

import puerta.support.dao.BaseDao;
import pula.sys.domains.TrainLog;
import pula.sys.domains.TrainLogItem;

public interface TrainLogItemDao extends BaseDao<TrainLogItem, Long> {

	void save(List<TrainLogItem> items, TrainLog cc, boolean clearFirst);

}
