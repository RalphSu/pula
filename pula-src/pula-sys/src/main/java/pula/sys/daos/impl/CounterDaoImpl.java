package pula.sys.daos.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import pula.sys.daos.CounterDao;
import pula.sys.domains.Counter;

@Repository
public class CounterDaoImpl extends HibernateGenericDao<Counter, Long>
		implements CounterDao {

	@Override
	public int inc(String refId, String no) {

		// sync
		String sql = "select counter from Counter where refId=? and no=?";
		Integer c = this.findSingle(sql, refId, no);
		if (c == null) {
			Counter cc = new Counter();
			cc.setCounter(1);
			cc.setRefId(refId);
			cc.setUpdatedTime(Calendar.getInstance());
			cc.setNo(no);
			getHibernateTemplate().save(cc);
			return 1;

		} else {
			// update
			sql = "update Counter set counter=counter+1,updatedTime=? where refId=? and no=?";
			updateBatch(sql, Calendar.getInstance(), refId, no);
			return c + 1;
		}
	}

}
