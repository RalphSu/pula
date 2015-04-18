package pula.sys.daos.impl;

import java.util.Calendar;

import org.springframework.stereotype.Repository;

import puerta.system.base.HibernateGenericDao;
import puerta.system.vo.MapBean;
import pula.sys.daos.TeacherCardDao;
import pula.sys.domains.Card;
import pula.sys.domains.Teacher;
import pula.sys.domains.TeacherCard;

@Repository
public class TeacherCardDaoImpl extends HibernateGenericDao<TeacherCard, Long>
		implements TeacherCardDao {

	@Override
	public boolean saveIfNeed(Teacher ef, Long cardId, boolean force) {
		if (force) {

		} else {
			// 检查旧的有没有，然后新的跟上去

			String sql = "select id from TeacherCard where card.id=? and teacher.id=? and current=?";
			Long id = findSingle(sql, cardId, ef.getId(), true);
			if (id == null) {
				// 没有历史记录，直接旧的推翻存新的
				sql = "update TeacherCard set current=? where teacher.id=? ";
				updateBatch(sql, false, ef.getId());
			} else {
				// 存在，就是旧的，不需要改，不需要新增
				return false;

			}
		}

		// 保存新纪录

		TeacherCard tc = new TeacherCard();
		tc.setCard(Card.create(cardId));
		tc.setCurrent(true);
		tc.setTeacher(ef);
		tc.setCreatedTime(Calendar.getInstance());

		_save(tc);

		return true;

	}

	@Override
	public void cancel(Teacher ef) {
		// cancel the the teacher card , make no card now
		String sql = "update TeacherCard set current=? where teacher.id=? ";
		updateBatch(sql, false, ef.getId());
	}

	@Override
	public MapBean meta(String rfid, Long branch_id) {
		String sql = "select t.no as no ,t.name as name from TeacherCard u LEFT JOIN u.teacher as t where"
				+ " u.card.mac=? and u.current=? and exists(select ta.id from TeacherAssignment"
				+ " ta where ta.teacher.id=t.id and ta.branch.id=? and ta.current=?) and t.removed=? ";
		return mapBean(sql, rfid, true, branch_id, true, false);
	}

}
