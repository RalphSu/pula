package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 教师每月考核
 * 
 * @author 乙郎
 * 
 */
@WxlDomain("教师绩效")
public class TeacherPerformance implements LoggablePo {

	private long id;
	private Teacher teacher;
	private Branch branch;
	private SysUser creator, updater;
	private Calendar createdTime, updatedTime;

	private int courseCount; // 根据系统加载
	private int workdays, factWorkDays;
	private int later, earlier, leave;
	private double complex, performance;
	private boolean removed;

	private int statYear, statMonth;

	private int orders, chargebacks;

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public int getChargebacks() {
		return chargebacks;
	}

	public void setChargebacks(int chargebacks) {
		this.chargebacks = chargebacks;
	}

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

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
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

	public int getCourseCount() {
		return courseCount;
	}

	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}

	public int getWorkdays() {
		return workdays;
	}

	public void setWorkdays(int workdays) {
		this.workdays = workdays;
	}

	public int getFactWorkDays() {
		return factWorkDays;
	}

	public void setFactWorkDays(int factWorkDays) {
		this.factWorkDays = factWorkDays;
	}

	public int getLater() {
		return later;
	}

	public void setLater(int later) {
		this.later = later;
	}

	public int getEarlier() {
		return earlier;
	}

	public void setEarlier(int earlier) {
		this.earlier = earlier;
	}

	public int getLeave() {
		return leave;
	}

	public void setLeave(int leave) {
		this.leave = leave;
	}

	public double getComplex() {
		return complex;
	}

	public void setComplex(double complex) {
		this.complex = complex;
	}

	public double getPerformance() {
		return performance;
	}

	public void setPerformance(double performance) {
		this.performance = performance;
	}

	@Override
	public String toLogString() {
		// TODO Auto-generated method stub
		return null;
	}

}
