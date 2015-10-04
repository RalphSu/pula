package pula.sys.domains;

import java.util.Calendar;

//学生照片，老师照片等
public class FileAttachment {

	public static final int TYPE_STUDENT_ICON = 10;
	public static final int TYPE_STUDENT_ETC = 12;
	public static final int TYPE_STUENDT_WORK = 13;
	public static final int TYPE_STUENDT_TIME_COURSE_WORK = 14;
	
	
	public static final int TYPE_TEACHER_ICON = 20;
	public static final int TYPE_TEACHER_ETC = 22;

	private long id;
	private String refId;
	private String name; // orginalName;
	private String extName; // 扩展名
	private String fileId; // 基于存储位置的目录信息和文件名

	private int type;

	private Calendar createdTime; // 创建时间
	private boolean removed;

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String filePath) {
		this.fileId = filePath;
	}

}
