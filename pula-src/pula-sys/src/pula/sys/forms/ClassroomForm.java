package pula.sys.forms;

import pula.sys.domains.Branch;
import pula.sys.domains.Classroom;

public class ClassroomForm extends Classroom {

	private long branchId;

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public Classroom toClassroom() {
		Classroom obj = new Classroom();
		obj.setBranch(Branch.create(branchId));
		obj.setCreator(this.getCreator());
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setUpdater(this.getUpdater());
		return obj;
	}

}
