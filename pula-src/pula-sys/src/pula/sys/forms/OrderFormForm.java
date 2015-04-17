package pula.sys.forms;

import pula.sys.domains.OrderForm;

public class OrderFormForm extends OrderForm {

	private long courseProductId;
	private String masterSalesmanNo, slaveSalesmanNo;
	private String studentNo;
	private String teacherNo;

	public long getCourseProductId() {
		return courseProductId;
	}

	public void setCourseProductId(long courseProductId) {
		this.courseProductId = courseProductId;
	}

	public String getMasterSalesmanNo() {
		return masterSalesmanNo;
	}

	public void setMasterSalesmanNo(String masterSalesmanNo) {
		this.masterSalesmanNo = masterSalesmanNo;
	}

	public String getSlaveSalesmanNo() {
		return slaveSalesmanNo;
	}

	public void setSlaveSalesmanNo(String slaveSalesmanNo) {
		this.slaveSalesmanNo = slaveSalesmanNo;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getTeacherNo() {
		return teacherNo;
	}

	public void setTeacherNo(String teacherNo) {
		this.teacherNo = teacherNo;
	}

	public OrderForm toOrderForm() {
		OrderForm obj = new OrderForm();
		obj.setBranch(this.getBranch());
		obj.setCanceled(this.isCanceled());
		obj.setComments(this.getComments());
		obj.setCommissionType(this.getCommissionType());
		obj.setCourseProduct(this.getCourseProduct());
		obj.setId(this.getId());
		obj.setMasterSalesman(this.getMasterSalesman());
		obj.setNo(this.getNo());
		obj.setPayStatus(this.getPayStatus());
		obj.setPrepay(this.getPrepay());
		obj.setSlaveSalesman(this.getSlaveSalesman());
		obj.setStatus(this.getStatus());
		obj.setStudent(this.getStudent());
		obj.setTeacher(this.getTeacher());
		obj.setTotalAmount(this.getTotalAmount());
		obj.setPoints(this.getPoints());
		obj.setCourseCount(this.getCourseCount());
		return obj;
	}

}
