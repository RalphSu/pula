package pula.sys.forms;

import puerta.support.utils.DateExTool;
import pula.sys.domains.Branch;
import pula.sys.domains.Classroom;
import pula.sys.domains.CourseClient;

public class CourseClientForm extends CourseClient {
	private String expiredTimeText;
	private long branchId, classroomId;

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public long getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(long classroomId) {
		this.classroomId = classroomId;
	}

	public String getExpiredTimeText() {
		return expiredTimeText;
	}

	public void setExpiredTimeText(String expiredTimeText) {
		this.expiredTimeText = expiredTimeText;
	}

	public CourseClient toCourseClient() {
		CourseClient obj = new CourseClient();
		obj.setApplier(this.getApplier());
		obj.setApplyComments(this.getApplyComments());
		obj.setComments(this.getComments());
		obj.setExpiredTime(DateExTool.getDate(this.expiredTimeText));
		obj.setId(this.getId());
		obj.setIp(this.getIp());
		obj.setLicenseKey(this.getLicenseKey());
		obj.setMachineNo(this.getMachineNo());
		obj.setName(this.getName());
		obj.setStatus(this.getStatus());
		obj.setBranch(Branch.create(branchId));
		obj.setClassroom(Classroom.create(classroomId));
		return obj;
	}

}
