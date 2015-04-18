package pula.sys.domains;

import java.util.Calendar;

/**
 * 课程序列，每个班级都有个序列，也是班级的一种表现形式
 * 
 * @author tiyi
 * 
 */
public class CourseSeries {

	private long id;
	private Calendar createdTime;
	private SysUser creator;
	private String name;
	private Branch school; // 哪个学校的

	private boolean removed, canceled; // 删除 或取消
	private Calendar cancelTime;

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	public Calendar getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Calendar cancelTime) {
		this.cancelTime = cancelTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser creator) {
		this.creator = creator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Branch getSchool() {
		return school;
	}

	public void setSchool(Branch school) {
		this.school = school;
	}

}
