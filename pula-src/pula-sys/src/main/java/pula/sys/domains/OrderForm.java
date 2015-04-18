package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.helpers.OrderFormHelper;

@WxlDomain("订单")
public class OrderForm implements LoggablePo {
	public static final int COMMISSION_TYPE_100 = 1;
	public static final int COMMISSION_TYPE_37 = 2; // 28
	public static final int COMMISSION_TYPE_28 = 4; // 三七
	// public static final int COMMISSION_TYPE_46 = 5; // 46
	public static final int COMMISSION_TYPE_55 = 6; // 5,5开

	public static final int STATUS_INPUT = 1;
	public static final int STATUS_CONFIRM = 2; // 确认
	public static final int STATUS_COMPLETED = 3; // 完成
	public static final int STATUS_RETURN = 4; // 发生退单

	public static final int PAY_STATUS_PREPAY = 2;
	public static final int PAY_STATUS_PAID = 3;
	public static final int PAY_STATUS_NONE = 1;

	private long id;
	private String no; // 订单号自动生成
	private Branch branch;
	private Salesman masterSalesman, slaveSalesman; // 两个销售
	private int commissionType;// 佣金类型

	private CourseProduct courseProduct;// 选用的产品

	private int status;
	private double totalAmount;
	private boolean removed, canceled;

	private Student student; // 学生，或者就是买单的人

	private Calendar createdTime, updatedTime, auditTime;
	private Teacher teacher; // 提成的老师
	private int payStatus;
	private double prepay;
	private String comments;
	private SysUser creator, updater, auditor;
	private int points, courseCount, consumeCourseCount;// 赠送分

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

	public int getConsumeCourseCount() {
		return consumeCourseCount;
	}

	public void setConsumeCourseCount(int consumeCourseCount) {
		this.consumeCourseCount = consumeCourseCount;
	}

	public int getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Calendar getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Calendar auditTime) {
		this.auditTime = auditTime;
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

	public SysUser getAuditor() {
		return auditor;
	}

	public void setAuditor(SysUser auditer) {
		this.auditor = auditer;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(int payStatus) {
		this.payStatus = payStatus;
	}

	public double getPrepay() {
		return prepay;
	}

	public void setPrepay(double prepay) {
		this.prepay = prepay;
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

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

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

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch school) {
		this.branch = school;
	}

	public Salesman getMasterSalesman() {
		return masterSalesman;
	}

	public void setMasterSalesman(Salesman masterSalesman) {
		this.masterSalesman = masterSalesman;
	}

	public Salesman getSlaveSalesman() {
		return slaveSalesman;
	}

	public void setSlaveSalesman(Salesman slaveSalesman) {
		this.slaveSalesman = slaveSalesman;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(int commissionType) {
		this.commissionType = commissionType;
	}

	public CourseProduct getCourseProduct() {
		return courseProduct;
	}

	public void setCourseProduct(CourseProduct coursePackage) {
		this.courseProduct = coursePackage;
	}

	@Override
	public String toLogString() {
		return this.no;
	}

	public String getCommissionTypeName() {
		return OrderFormHelper.getCommissionTypeName(this.commissionType);
	}

	public String getPayStatusName() {
		return OrderFormHelper.getPayStatusName(this.payStatus);
	}

	public static OrderForm create(long orderFormId) {
		OrderForm of = new OrderForm();
		of.id = orderFormId;
		return of;
	}

}
