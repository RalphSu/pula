package pula.sys.domains;

import java.util.Calendar;

import puerta.support.annotation.WxlDomain;

@WxlDomain("试听记录")
public class Audition {

	protected long id;
	protected String student, age, phone, parent, content, comments;
	protected String plan1, plan2, plan3, plan4, plan5;
	protected SysCategory result;
	protected boolean removed, closed;
	protected Calendar createdTime, updatedTime;
	protected SysUser owner;
	protected Branch branch;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStudent() {
		return student;
	}

	public void setStudent(String student) {
		this.student = student;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getPlan1() {
		return plan1;
	}

	public void setPlan1(String plan1) {
		this.plan1 = plan1;
	}

	public String getPlan2() {
		return plan2;
	}

	public void setPlan2(String plan2) {
		this.plan2 = plan2;
	}

	public String getPlan3() {
		return plan3;
	}

	public void setPlan3(String plan3) {
		this.plan3 = plan3;
	}

	public String getPlan4() {
		return plan4;
	}

	public void setPlan4(String plan4) {
		this.plan4 = plan4;
	}

	public String getPlan5() {
		return plan5;
	}

	public void setPlan5(String plan5) {
		this.plan5 = plan5;
	}

	public SysCategory getResult() {
		return result;
	}

	public void setResult(SysCategory result) {
		this.result = result;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
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

	public SysUser getOwner() {
		return owner;
	}

	public void setOwner(SysUser owner) {
		this.owner = owner;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

}
