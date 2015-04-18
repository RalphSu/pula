package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 
 * @author tiyi
 * 
 */
@WxlDomain("分支机构")
public class Branch implements LoggablePo {

	private long id;
	private String no, name;
	private boolean removed;
	private boolean enabled;
	// 网页信息
	private String address;
	private String phone, linkman, email, mobile, fax;
	private String comments;

	private boolean showInWeb, headquarter;

	private Calendar createdTime, updatedTime;
	private SysUser creator, updater;
	private String prefix; //学员的前缀
	

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isHeadquarter() {
		return headquarter;
	}

	public void setHeadquarter(boolean headquarter) {
		this.headquarter = headquarter;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public boolean isShowInWeb() {
		return showInWeb;
	}

	public void setShowInWeb(boolean showInWeb) {
		this.showInWeb = showInWeb;
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

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static Branch create(long branchId) {
		if (branchId == 0) {
			return null;
		}
		Branch b = new Branch();
		b.id = branchId;
		return b;
	}
}
