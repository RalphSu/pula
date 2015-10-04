package pula.sys.daos.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import puerta.support.dao.HibernateTool;
import puerta.support.utils.WxlSugar;
import puerta.system.base.HibernateGenericDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.domains.FileAttachment;
import pula.sys.intfs.RefIdSupport;

@Repository
public class FileAttachmentDaoImpl extends
		HibernateGenericDao<FileAttachment, Long> implements FileAttachmentDao {

	@Override
	public void save(RefIdSupport ef, List<FileAttachment> attachments,
			boolean clearFirst) {

		String refId = ef.toRefId();
		int typeRange = ef.getTypeRange();

		_save(attachments, clearFirst, refId, typeRange);

		// 文件先留在服务器了
		// return m;
	}

	private void _save(List<FileAttachment> attachments, boolean clearFirst,
			String refId, int typeRange) {
		Map<Long, Long> m = null;
		if (clearFirst) {
			// means update
			// hit!

			m = load(refId, typeRange);

		} else {
			m = WxlSugar.newHashMap();
		}

		// save all , to hit
		for (FileAttachment a : attachments) {
			if (a.getId() != 0) {
				if (m.containsKey(a.getId())) {
					m.remove(a.getId());
					continue;
				}
				// 已经不存在了，直接退掉
				continue;
			}

			a.setRefId(refId);
			a.setCreatedTime(Calendar.getInstance());
			getHibernateTemplate().save(a);
		}

		// hits 是关键
		// 多出那么多，怎么处理？直接删！
		String sql = "update FileAttachment set removed=? where id=? and removed=?";
		for (Long id : m.keySet()) {
			updateBatch(sql, true, id, false);
		}
	}

	private Map<Long, Long> load(String id, int typeRange) {
		int[] ranges = getRanges(typeRange);
		int rangeBegin = ranges[0];
		int rangeEnd = ranges[1];
		String sql = "select u.id,u.fileId from FileAttachment u where u.refId=? and u.removed=? and ( type>=? and type<=?)";
		return HibernateTool.asMap(find(sql, id, false, rangeBegin, rangeEnd));
	}

	private int[] getRanges(int typeRange) {
		int rangeBegin = FileAttachment.TYPE_TEACHER_ICON;
		int rangeEnd = FileAttachment.TYPE_TEACHER_ETC;
		if (typeRange >= FileAttachment.TYPE_STUDENT_ICON
				&& typeRange <= FileAttachment.TYPE_STUDENT_ETC) {
			rangeBegin = FileAttachment.TYPE_STUDENT_ICON;
			rangeEnd = FileAttachment.TYPE_STUDENT_ETC;
		} else if (typeRange >= FileAttachment.TYPE_STUENDT_WORK
				&& typeRange <= FileAttachment.TYPE_STUENDT_WORK) {
			rangeBegin = FileAttachment.TYPE_STUENDT_WORK;
			rangeEnd = FileAttachment.TYPE_STUENDT_WORK;
        } else if (typeRange == FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK) {
            rangeBegin = FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK;
            rangeEnd = FileAttachment.TYPE_STUENDT_TIME_COURSE_WORK;
        }

		return new int[] { rangeBegin, rangeEnd };
	}

	@Override
	public List<FileAttachment> loadByRefId(String id, int typeRange) {
		int[] ranges = getRanges(typeRange);
		int rangeBegin = ranges[0];
		int rangeEnd = ranges[1];
		String sql = "select u from FileAttachment u where u.refId=? and u.removed=? and ( type>=? and type<=?)";
		return find(sql, id, false, rangeBegin, rangeEnd);
	}

	@Override
	public FileAttachment getByRefId(String id, int type) {
		String sql = "select u from FileAttachment u where u.refId=? and u.removed=? and u.type=?";
		return findSingle(sql, id, false, type);
	}

}
