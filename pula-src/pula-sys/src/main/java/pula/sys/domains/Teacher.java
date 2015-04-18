package pula.sys.domains;

import java.util.Calendar;
import java.util.List;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import pula.sys.BaseHelper;
import pula.sys.helpers.TeacherHelper;
import pula.sys.intfs.RefIdSupport;

@WxlDomain("教师")
public class Teacher implements LoggablePo, RefIdSupport {

	public static final int ON_DUTY = 1;
	public static final int LEAVE_DUTY = 2;

	public static final int LEVEL_MASTER = 1; // 老师
	public static final int LEVEL_ASSISTANT = 2; // 助教

	public static final int LEVEL_A = 1;
	public static final int LEVLE_B = 2;
	public static final int LEVEL_C = 3;
	public static final int LEVEL_D = 4;

	public static final int ATTACHMENT_ICON = 1;
	public static final int ATTACHMENT_OTHER = 2;

	private long id;
	private String no, name;
	private String barcode;
	private boolean enabled, removed;

	private Calendar createdTime, updatedTime;
	private SysUser creator, updater;

	// 教师级别
	private int level; // abcd 1,2,3,4

	// 在职时间，离职时间
	private int status;
	private Calendar joinTime, leaveTime;
	private String school, identity, homeplace;
	private Calendar birthday;
	private String liveAddress, hjAddress; // 户籍
	private int gender;
	private String mobile, phone, email;

	private String comments; // 备注
	private String speciality, zip; // 特长

	// 紧急联络人
	private String linkman, linkmanTel, linkmanCaption;

	private List<TeacherAssignment> assignments;
	private List<TeacherCard> cards;

	// 教师自己的登录账号和密码
	private String loginId, password;
	private String attachmentKey; // 组成ref-id

	public List<TeacherCard> getCards() {
		return cards;
	}

	public void setCards(List<TeacherCard> cards) {
		this.cards = cards;
	}

	public String getAttachmentKey() {
		return attachmentKey;
	}

	public void setAttachmentKey(String attachmentKey) {
		this.attachmentKey = attachmentKey;
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

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
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

	public String getLinkmanCaption() {
		return linkmanCaption;
	}

	public void setLinkmanCaption(String linkmanCaption) {
		this.linkmanCaption = linkmanCaption;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkmanTel() {
		return linkmanTel;
	}

	public void setLinkmanTel(String linkmanTel) {
		this.linkmanTel = linkmanTel;
	}

	public List<TeacherAssignment> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<TeacherAssignment> assignments) {
		this.assignments = assignments;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Calendar getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Calendar joinTime) {
		this.joinTime = joinTime;
	}

	public Calendar getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(Calendar leaveTime) {
		this.leaveTime = leaveTime;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getHomeplace() {
		return homeplace;
	}

	public void setHomeplace(String homeplace) {
		this.homeplace = homeplace;
	}

	public Calendar getBirthday() {
		return birthday;
	}

	public void setBirthday(Calendar birthday) {
		this.birthday = birthday;
	}

	public String getLiveAddress() {
		return liveAddress;
	}

	public void setLiveAddress(String liveAddress) {
		this.liveAddress = liveAddress;
	}

	public String getHjAddress() {
		return hjAddress;
	}

	public void setHjAddress(String hjAddress) {
		this.hjAddress = hjAddress;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public String getStatusName() {
		return TeacherHelper.getStatusName(this.status);
	}

	public String getGenderName() {
		return BaseHelper.getGenderName(this.gender);
	}

	public static Teacher create(long id2) {
		Teacher t = new Teacher();
		t.id = id2;
		return t;
	}

	public String toRefId() {
		return this.getId() + this.getAttachmentKey();
	}

	@Override
	public int getTypeRange() {
		return FileAttachment.TYPE_TEACHER_ICON;
	}

}
