package puerta.system.dao.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.SystemLogDao;
import puerta.system.po.SystemLog;

@Repository
public class SystemLogDaoImpl extends HibernateGenericDao<SystemLog, String>
		implements SystemLogDao {

	@Override
	public void save(String event, String extend) {
		SystemLog s = new SystemLog();
		s.setEvent(event);
		s.setEventTime(Calendar.getInstance());
		s.setExtendInfo(extend);
		s.setIp(null);
		getHibernateTemplate().save(s);
	}

}
