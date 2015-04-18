package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("礼品中转")
public class GiftTransfer implements LoggablePo {

	public static final int STATUS_INPUT = 1;
	public static final int STATUS_SUBMIT = 2;
	// public static final int STATUS_ACCEPT = 3;
	// public static final int STATUS_REJECT = 4;
	public static final int STATUS_SENT = 5;
	public static final int STATUS_DELIVERIED = 6;

	private long id;
	private Gift gift;
	private Calendar createdTime, updatedTime, sentTime, submitTime,
			receivedTime;
	private SysUser creator, updater, senter, receiver;
	private int status;
	private String comments;
	private Branch branch, toBranch;
	private boolean removed;
	private String outNo;
	private int sentQuantity, arriveQuantity;

	public Branch getToBranch() {
		return toBranch;
	}

	public void setToBranch(Branch toBranch) {
		this.toBranch = toBranch;
	}

	public Calendar getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Calendar submitTime) {
		this.submitTime = submitTime;
	}

	public SysUser getSenter() {
		return senter;
	}

	public void setSenter(SysUser senter) {
		this.senter = senter;
	}

	public SysUser getReceiver() {
		return receiver;
	}

	public void setReceiver(SysUser receiver) {
		this.receiver = receiver;
	}

	public Calendar getSentTime() {
		return sentTime;
	}

	public void setSentTime(Calendar sentTime) {
		this.sentTime = sentTime;
	}

	public Calendar getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Calendar deliveriedTime) {
		this.receivedTime = deliveriedTime;
	}

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	public int getSentQuantity() {
		return sentQuantity;
	}

	public void setSentQuantity(int sentQuantity) {
		this.sentQuantity = sentQuantity;
	}

	public int getArriveQuantity() {
		return arriveQuantity;
	}

	public void setArriveQuantity(int arriveQuantity) {
		this.arriveQuantity = arriveQuantity;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public SysUser getUpdater() {
		return updater;
	}

	public void setUpdater(SysUser updater) {
		this.updater = updater;
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

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	@Override
	public String toLogString() {
		return String.valueOf(id);
	}

	public GiftTransfer copy() {
		GiftTransfer rt = this;
		GiftTransfer po = new GiftTransfer();
		po.setArriveQuantity(rt.getArriveQuantity());

		po.setBranch(rt.getBranch());
		po.setComments(rt.getComments());
		po.setCreator(rt.getCreator());
		po.setId(rt.getId());
		po.setGift(rt.getGift());
		po.setOutNo(rt.getOutNo());
		po.setReceivedTime(rt.getReceivedTime());
		po.setReceiver(rt.getReceiver());
		po.setSentQuantity(rt.getSentQuantity());
		po.setSentTime(rt.getSentTime());
		po.setSenter(rt.getSenter());
		po.setStatus(rt.getStatus());
		po.setUpdater(rt.getUpdater());

		return po;
	}

	public GiftStockLog toGiftStockLog(int out) {
		GiftStockLog sl = new GiftStockLog();
		sl.setBranch(this.branch);
		sl.setGift(this.gift);
		sl.setGiftTransfer(this);
		sl.setComments(this.comments);
		sl.setCreatedTime(this.createdTime);
		sl.setCreator(this.creator);
		sl.setEventTime(Calendar.getInstance());
		sl.setOutNo(this.getOutNo());
		sl.setType(out);
		if (StockLog.OUT == out) {
			sl.setQuantity(this.sentQuantity);
		} else {
			sl.setQuantity(this.arriveQuantity);
		}
		return sl;
	}

}
