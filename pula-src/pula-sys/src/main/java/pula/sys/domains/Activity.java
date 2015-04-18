package pula.sys.domains;

import java.util.Calendar;
import java.util.List;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("市场活动")
public class Activity implements LoggablePo {

	private long id;
	private String no, name;
	private Calendar createdTime, updatedTime;
	private String comments;
	private boolean removed, enabled;
	private Calendar beginDate, endDate;
	private List<ActivityBranch> activityBranches;
	private SysUser creator, updater;
	private String partner;

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser creator) {
		this.creator = creator;
	}

	public SysUser getUpdater() {
		return updater;
	}

	public void setUpdater(SysUser updater) {
		this.updater = updater;
	}

	public List<ActivityBranch> getActivityBranches() {
		return activityBranches;
	}

	public void setActivityBranches(List<ActivityBranch> activityBranches) {
		this.activityBranches = activityBranches;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Calendar getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Calendar beginDate) {
		this.beginDate = beginDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static Activity create(long id2) {
		Activity a = new Activity();
		a.setId(id2);
		return a;
	}

}
