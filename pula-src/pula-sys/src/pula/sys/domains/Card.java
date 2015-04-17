package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("卡片")
public class Card implements LoggablePo {

	public static final int STATUS_FREE = 1;
	public static final int STATUS_USED = 2;
	// public static final int REPLACED = 3;

	private long id;
	private String no; // 卡号
	private String mac; // 物理
	private boolean enabled, removed; // 黑名单
	private Calendar createdTime;
	private int status;// 占用？
	private String comments, refId;// 使用备注

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
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

	public void setEnabled(boolean block) {
		this.enabled = block;
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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Override
	public String toLogString() {
		return this.no;
	}

	public static Card create(Long cardId) {
		if (cardId == null || cardId == 0) {
			return null;
		}

		Card c = new Card();
		c.setId(cardId);
		return c;
	}
}
