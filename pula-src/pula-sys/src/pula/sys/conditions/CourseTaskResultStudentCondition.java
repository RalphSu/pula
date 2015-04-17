package pula.sys.conditions;

public class CourseTaskResultStudentCondition extends CourseTaskResultCondition {

	private long teacherId;
	private long studentId;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

}
