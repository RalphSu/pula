package pula.sys.daos;

import puerta.support.dao.BaseDao;
import puerta.system.vo.MapBean;
import pula.sys.domains.Student;
import pula.sys.domains.StudentCard;

public interface StudentCardDao extends BaseDao<StudentCard, Long> {

	boolean saveIfNeed(Student ef, Long cardId,boolean force);

	void cancel(Student ef);

	MapBean meta(String rfid, Long branch_id);


}
