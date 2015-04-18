package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.BaseHelper;

@WxlDomain("销售人员")
public class Salesman implements LoggablePo {

	private long id;
	private String no, name;
	private Branch branch;
	private String mobile, phone;

	private String comments;
	private boolean enabled, removed;

	private Calendar createdTime, updatedTime;
	private SysUser creator, updater;
	private int gender;
	private SysUser sysUser; // 系统用户对象,
	private int giftPoints;

	public int getGiftPoints() {
		return giftPoints;
	}

	public void setGiftPoints(int giftPoints) {
		this.giftPoints = giftPoints;
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

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String barcode) {
		this.comments = barcode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean eanbled) {
		this.enabled = eanbled;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
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

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String getGenderName() {
		return BaseHelper.getGenderName(this.gender);
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static Salesman create(Long asLong) {
		if (asLong == null)
			return null;
		Salesman s = new Salesman();
		s.id = asLong;
		return s;
	}

}
