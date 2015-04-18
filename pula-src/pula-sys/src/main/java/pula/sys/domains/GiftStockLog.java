package pula.sys.domains;

import java.util.Calendar;

//库存日志
public class GiftStockLog {

	public static final int IN = 1;

	public static final int OUT = -1;

	private long id;
	private Calendar eventTime;

	private Gift gift;
	private int quantity;
	private Branch branch;

	private int type; // inbound outbound
	private SysUser creator;
	private Calendar createdTime;
	private String comments;

	private String outNo;// 内外单号
	private GiftTransfer giftTransfer;
	private GiftStockEvent giftStockEvent;

	public GiftTransfer getGiftTransfer() {
		return giftTransfer;
	}

	public void setGiftTransfer(GiftTransfer giftTransfer) {
		this.giftTransfer = giftTransfer;
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

	public String getOutNo() {
		return outNo;
	}

	public void setOutNo(String outNo) {
		this.outNo = outNo;
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

	public void setCreator(SysUser sysUser) {
		this.creator = sysUser;
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

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar logTime) {
		this.eventTime = logTime;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public GiftStockEvent getGiftStockEvent() {
		return giftStockEvent;
	}

	public void setGiftStockEvent(GiftStockEvent giftStockEvent) {
		this.giftStockEvent = giftStockEvent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public GiftStockLog copy() {
		GiftStockLog sl = new GiftStockLog();
		sl.setCreatedTime(this.createdTime);
		sl.setCreator(this.creator);
		sl.setEventTime(this.eventTime);
		sl.setOutNo(this.outNo);
		sl.setType(this.type);

		sl.setGift(this.gift);
		sl.setQuantity(this.quantity);

		return sl;
	}

}
