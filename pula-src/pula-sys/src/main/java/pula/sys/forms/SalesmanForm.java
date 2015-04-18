package pula.sys.forms;

import pula.sys.domains.Branch;
import pula.sys.domains.Salesman;

public class SalesmanForm extends Salesman {

	private long branchId;

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public Salesman toSalesman() {
		Salesman obj = new Salesman();
		obj.setComments(this.getComments());
		obj.setBranch(Branch.create(branchId));
		obj.setCreator(this.getCreator());
		obj.setGender(this.getGender());
		obj.setId(this.getId());
		obj.setMobile(this.getMobile());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		obj.setPhone(this.getPhone());
		obj.setSysUser(this.getSysUser());
		obj.setUpdater(this.getUpdater());
		obj.setGiftPoints(this.getGiftPoints());
		return obj;
	}

}
