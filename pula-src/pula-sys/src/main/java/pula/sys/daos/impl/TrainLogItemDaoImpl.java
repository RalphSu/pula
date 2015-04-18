package pula.sys.daos.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import pula.sys.daos.TrainLogItemDao;
import pula.sys.domains.TrainLog;
import pula.sys.domains.TrainLogItem;

@Repository
public class TrainLogItemDaoImpl extends
		HibernateGenericDao<TrainLogItem, Long> implements TrainLogItemDao {

	@Override
	public void save(List<TrainLogItem> items, TrainLog cc, boolean clearFirst) {

		if (clearFirst) {
			// clear first
			String sql = "delete from TrainLogItem where trainLog.id=?";
			delete(sql, cc.getId());
		}

		// - -c-- -c
		int n = 0;
		for (TrainLogItem item : items) {
			item.setTrainLog(cc);
			item.setIndexNo(n++);
			getHibernateTemplate().save(item);
		}

	}

}
