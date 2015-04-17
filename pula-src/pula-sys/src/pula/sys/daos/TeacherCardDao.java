package pula.sys.daos;

import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.domains.Teacher;
import pula.sys.domains.TeacherCard;

public interface TeacherCardDao extends BaseDao<TeacherCard, Long> {

	boolean saveIfNeed(Teacher ef, Long cardId,boolean force);

	void cancel(Teacher ef);

	MapBean meta(String rfid, Long branch_id);


}
