package puerta.system.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.RequestHistoryDao;
import puerta.system.po.RequestHistory;

@Repository
public class RequestHistoryDaoImpl extends
		HibernateGenericDao<RequestHistory, Long> implements RequestHistoryDao {

	@Override
	public void save(RequestHistory rh) {
		rh.setCreatedTime(Calendar.getInstance());
		getHibernateTemplate().save(rh);

	}

	@Override
	public List<RequestHistory> listHistory(String purviewId) {
		String hql = "select u from RequestHistory u where u.purview.id=? order by url";
		return find(hql, purviewId);
	}
}
