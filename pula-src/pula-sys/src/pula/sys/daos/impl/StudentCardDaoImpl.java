package pula.sys.daos.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import pula.sys.daos.StudentCardDao;
import pula.sys.domains.Card;
import pula.sys.domains.Student;
import pula.sys.domains.StudentCard;

@Repository
public class StudentCardDaoImpl extends HibernateGenericDao<StudentCard, Long>
		implements StudentCardDao {

	@Override
	public boolean saveIfNeed(Student ef, Long cardId, boolean force) {
		if (force) {

		} else {
			// 检查旧的有没有，然后新的跟上去

			String sql = "select id from StudentCard where card.id=? and student.id=? and current=?";
			Long id = findSingle(sql, cardId, ef.getId(), true);
			if (id == null) {
				// 没有历史记录，直接旧的推翻存新的
				sql = "update StudentCard set current=? where student.id=? ";
				updateBatch(sql, false, ef.getId());
			} else {
				// 存在，就是旧的，不需要改，不需要新增
				return false;

			}
		}

		// 保存新纪录

		StudentCard tc = new StudentCard();
		tc.setCard(Card.create(cardId));
		tc.setCurrent(true);
		tc.setStudent(ef);
		tc.setCreatedTime(Calendar.getInstance());

		_save(tc);

		return true;

	}

	@Override
	public void cancel(Student ef) {
		// cancel the the student card , make no card now
		String sql = "update StudentCard set current=? where student.id=? ";
		updateBatch(sql, false, ef.getId());
	}

	@Override
	public MapBean meta(String rfid, Long branch_id) {
		String sql = "select s.no as no ,s.name as name from StudentCard u LEFT JOIN u.student as s where"
				+ " u.card.mac=? and u.current=? and s.branch.id=? and s.removed=? ";
		return mapBean(sql, rfid, true, branch_id, false);

	}

}
