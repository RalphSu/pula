package pula.sys.daos;

import java.util.List;

import pula.sys.domains.FileAttachment;
import pula.sys.intfs.RefIdSupport;

public interface FileAttachmentDao {

	void save(RefIdSupport ef, List<FileAttachment> attachments,
			boolean clearFirst);

	List<FileAttachment> loadByRefId(String id, int typeRange);

	FileAttachment getByRefId(String buildFileRefId, int type);

	// void save(Student ef, List<FileAttachment> attachments, boolean
	// clearFirst);

}
