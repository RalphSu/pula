package puerta.system.dao.impl;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.dao.NostrumDao;
import puerta.system.po.Insider;

@Repository
public class NostrumDaoImpl extends HibernateGenericDao<Insider, String>
		implements NostrumDao {

	@Override
	public int update(String sql, Object... params) {
		return updateBatch(sql, params);
	}

}
