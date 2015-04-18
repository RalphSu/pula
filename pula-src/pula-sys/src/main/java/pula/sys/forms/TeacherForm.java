package pula.sys.forms;

import java.util.List;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Teacher;

public class TeacherForm extends Teacher {
	private String birthdayText;
	// private String confirmationTimeText;

	private String joinTimeText;
	private String leaveTimeText;

	private List<FileAttachmentForm> attachmentForms;
	private String cardNo;
	private boolean changePassword;

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBirthdayText() {
		return birthdayText;
	}

	public void setBirthdayText(String birthdayText) {
		this.birthdayText = birthdayText;
	}

	public String getJoinTimeText() {
		return joinTimeText;
	}

	public void setJoinTimeText(String joinTimeText) {
		this.joinTimeText = joinTimeText;
	}

	public String getLeaveTimeText() {
		return leaveTimeText;
	}

	public void setLeaveTimeText(String leaveTimeText) {
		this.leaveTimeText = leaveTimeText;
	}

	public List<FileAttachmentForm> getAttachmentForms() {
		return attachmentForms;
	}

	public void setAttachmentForms(List<FileAttachmentForm> attachmentForms) {
		this.attachmentForms = attachmentForms;
	}

	public Teacher toTeacher() {
		Teacher obj = new Teacher();
		obj.setBirthday(DateExTool.getDate(this.birthdayText));
		obj.setComments(this.getComments());
		// obj.setConfirmationTime(DateExTool.getDate(this.confirmationTimeText));
		// obj.setEmployee(this.getEmployee());
		obj.setGender(this.getGender());
		obj.setHjAddress(this.getHjAddress());
		obj.setHomeplace(this.getHomeplace());
		obj.setId(this.getId());
		obj.setIdentity(this.getIdentity());
		obj.setJoinTime(DateExTool.getDate(this.joinTimeText));
		obj.setLeaveTime(DateExTool.getDate(this.leaveTimeText));
		obj.setLiveAddress(this.getLiveAddress());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setSchool(this.getSchool());
		obj.setStatus(this.getStatus());
		obj.setSpeciality(this.getSpeciality());
		obj.setBarcode(this.getBarcode());
		obj.setMobile(this.getMobile());
		obj.setPhone(this.getPhone());
		obj.setEmail(this.getEmail());
		obj.setZip(this.getZip());
		obj.setLevel(this.getLevel());

		// links
		obj.setLinkman(this.getLinkman());
		obj.setLinkmanCaption(this.getLinkmanCaption());
		obj.setLinkmanTel(this.getLinkmanTel());

		obj.setPassword(this.getPassword());

		return obj;
	}

}
