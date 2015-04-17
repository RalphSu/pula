package pula.sys.forms;

import java.util.List;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Student;

public class StudentForm extends Student {
	private String birthdayText;
	private List<FileAttachmentForm> attachmentForms;
	private boolean changePassword;
	private String cardNo;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public List<FileAttachmentForm> getAttachmentForms() {
		return attachmentForms;
	}

	public void setAttachmentForms(List<FileAttachmentForm> attachmentForms) {
		this.attachmentForms = attachmentForms;
	}

	public String getBirthdayText() {
		return birthdayText;
	}

	public void setBirthdayText(String birthdayText) {
		this.birthdayText = birthdayText;
	}

	public Student toStudent() {
		Student obj = new Student();
		obj.setBarcode(this.getBarcode());
		obj.setBirthday(DateExTool.getDate(this.birthdayText));
		obj.setBranch(this.getBranch());
		obj.setCreator(this.getCreator());
		obj.setEmail(this.getEmail());
		obj.setGender(this.getGender());
		obj.setId(this.getId());
		obj.setLoginId(this.getLoginId());
		obj.setMobile(this.getMobile());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setParentCaption(this.getParentCaption());
		obj.setParentName(this.getParentName());
		obj.setPassword(this.getPassword());
		obj.setPhone(this.getPhone());
		obj.setUpdater(this.getUpdater());
		obj.setAddress(this.getAddress());
		obj.setComments(this.getComments());
		obj.setZip(this.getZip());
		return obj;
	}

}
