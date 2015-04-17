package pula.sys.domains;

import java.util.Calendar;
import java.util.List;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import puerta.support.utils.DateExTool;

@WxlDomain("培训记录")
public class TrainLog implements LoggablePo {

	private long id;
	private Calendar createdTime, updatedTime, trainDate;
	private String location, content, trainer, comments;
	private Branch branch;
	private boolean removed;
	private SysUser creator, updater;
	private List<TrainLogItem> items;

	public List<TrainLogItem> getItems() {
		return items;
	}

	public void setItems(List<TrainLogItem> items) {
		this.items = items;
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

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Calendar getTrainDate() {
		return trainDate;
	}

	public void setTrainDate(Calendar trainDate) {
		this.trainDate = trainDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTrainer() {
		return trainer;
	}

	public void setTrainer(String trainer) {
		this.trainer = trainer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
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

	@Override
	public String toLogString() {
		return String.valueOf(this.id) + ":"
				+ DateExTool.getText(this.trainDate);
	}

}
