package pula.sys.domains;

import java.util.Calendar;
import java.util.List;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.BaseHelper;
import pula.sys.helpers.StudentHelper;
import pula.sys.intfs.RefIdSupport;

@WxlDomain("学生")
public class Student implements LoggablePo, RefIdSupport {

	private long id;
	private String no, name; // 编号，姓名
	private String barcode; // 卡号，有时候和编号不一定一致
	private int gender; // 学员性别

	private Calendar birthday;

	private boolean removed;
	private boolean enabled; // 有效与否

	private Calendar createdTime, updatedTime;
	private SysUser creator, updater;

	// 登录到网站
	private String loginId, password;

	private String parentName, mobile, phone, email, parentCaption;
	private Branch branch;
	private String attachmentKey; // 组成ref-id
	private String address, zip;
	private String comments;
	private List<StudentCard> cards;
	private int points;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<StudentCard> getCards() {
		return cards;
	}

	public void setCards(List<StudentCard> cards) {
		this.cards = cards;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getAttachmentKey() {
		return attachmentKey;
	}

	public void setAttachmentKey(String attachmentKey) {
		this.attachmentKey = attachmentKey;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getParentCaption() {
		return parentCaption;
	}

	public void setParentCaption(String parentCaption) {
		this.parentCaption = parentCaption;
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

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getGenderName() {
		return BaseHelper.getGenderName(this.gender);
	}

	public static Student create(long id2) {
		Student s = new Student();
		s.setId(id2);
		return s;
	}

	public String toRefId() {
		return StudentHelper.buildFileRefId(this.getId(),
				this.getAttachmentKey());
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	@Override
	public int getTypeRange() {
		return FileAttachment.TYPE_STUDENT_ICON;
	}

}
