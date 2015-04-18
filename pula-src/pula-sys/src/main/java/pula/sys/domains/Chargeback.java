package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

@WxlDomain("退单")
public class Chargeback implements LoggablePo {

	public static final int STATUS_INPUT = 1;
	public static final int STATUS_CONFIRM = 2;

	private long id;
	private String no;
	private OrderForm orderForm;
	private int backCourses;
	private double backAmount; // 退款额度
	private Calendar createdTime, updatedTime, auditTime;
	private SysUser creator, updater, auditor;
	private int status;
	private String comments;
	private boolean removed;
	private int statYear, statMonth;

	public int getStatYear() {
		return statYear;
	}

	public void setStatYear(int statYear) {
		this.statYear = statYear;
	}

	public int getStatMonth() {
		return statMonth;
	}

	public void setStatMonth(int statMonth) {
		this.statMonth = statMonth;
	}
	public Calendar getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		this.auditTime = auditTime;
	}

	public SysUser getAuditor() {
		return auditor;
	}

	public void setAuditor(SysUser auditor) {
		this.auditor = auditor;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;
	}

	public int getBackCourses() {
		return backCourses;
	}

	public void setBackCourses(int backCourses) {
		this.backCourses = backCourses;
	}

	public double getBackAmount() {
		return backAmount;
	}

	public void setBackAmount(double backAmount) {
		this.backAmount = backAmount;
	}

	@Override
	public String toLogString() {
		return this.no;
	}

}
