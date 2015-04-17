package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.helpers.StockEventHelper;

/**
 * 库存变更
 * 
 * @author tiyi
 * 
 */
@WxlDomain("礼品库存操作")
public class GiftStockEvent implements LoggablePo {
	public static final int TARGET_BUY = 10; // 购买
	public static final int TARGET_CHECK_ADD = 11; // 盘盈
	public static final int TARGET_CONSUME = -10;
	public static final int TARGET_LOST = -11;// 丢失、报损

	private long id;
	private Calendar eventTime;

	private Gift gift;
	private int quantity;
	private Branch branch;

	private SysUser creator;
	private Calendar createdTime;
	private String comments;

	private String outNo;// 内外单号
	private boolean removed;
	private int target;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar eventTime) {
		this.eventTime = eventTime;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public SysUser getCreator() {
		return creator;
	}

	public void setCreator(SysUser creator) {
		this.creator = creator;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	@Override
	public String toLogString() {
		return String.valueOf(id) + ":"
				+ StockEventHelper.getTargetName(this.target);
	}

	public String getTargetName() {
		return StockEventHelper.getTargetName(this.target);
	}

	public GiftStockLog toGiftStockLog() {
		GiftStockLog sl = new GiftStockLog();
		sl.setBranch(this.getBranch());
		sl.setComments(this.getComments());
		sl.setGift(this.gift);
		sl.setOutNo(this.outNo);
		sl.setQuantity(this.quantity);
		sl.setType(StockEventHelper.toType(this.target));
		sl.setEventTime(this.eventTime);
		sl.setGiftStockEvent(this);

		return sl;

	}

	public GiftStockEvent copy() {
		GiftStockEvent sl = new GiftStockEvent();
		sl.setBranch(this.getBranch());
		sl.setComments(this.getComments());
		sl.setGift(this.gift);
		sl.setOutNo(this.outNo);
		sl.setQuantity(this.quantity);
		sl.setTarget(this.target);
		sl.setEventTime(this.getEventTime());

		return sl;
	}
}
