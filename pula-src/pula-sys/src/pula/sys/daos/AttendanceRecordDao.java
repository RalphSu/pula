package pula.sys.daos;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import puerta.support.PaginationSupport;
import puerta.support.dao.BaseDao;
import pula.sys.conditions.AttendanceRecordCondition;
import pula.sys.domains.AttendanceRecord;

public interface AttendanceRecordDao extends BaseDao<AttendanceRecord, Long> {

	PaginationSupport<AttendanceRecord> search(
			AttendanceRecordCondition condition, int pageIndex);

	AttendanceRecord save(AttendanceRecord cli);

	Map<Long, List<AttendanceRecord>> map(Calendar date);

}
