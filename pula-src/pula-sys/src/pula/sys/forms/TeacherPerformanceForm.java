package pula.sys.forms;

import pula.sys.domains.Teacher;
import pula.sys.domains.TeacherPerformance;

public class TeacherPerformanceForm extends TeacherPerformance {

	private long teacherId;

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public TeacherPerformance toTeacherPerformance() {
		TeacherPerformance obj = new TeacherPerformance();
		obj.setBranch(this.getBranch());
		obj.setChargebacks(this.getChargebacks());
		obj.setComplex(this.getComplex());
		obj.setCourseCount(this.getCourseCount());
		obj.setCreator(this.getCreator());
		obj.setEarlier(this.getEarlier());
		obj.setFactWorkDays(this.getFactWorkDays());
		obj.setId(this.getId());
		obj.setLater(this.getLater());
		obj.setLeave(this.getLeave());
		obj.setOrders(this.getOrders());
		obj.setPerformance(this.getPerformance());
		obj.setStatMonth(this.getStatMonth());
		obj.setStatYear(this.getStatYear());
		obj.setTeacher(Teacher.create(teacherId));
		obj.setUpdater(this.getUpdater());
		obj.setWorkdays(this.getWorkdays());
		return obj;
	}

}
