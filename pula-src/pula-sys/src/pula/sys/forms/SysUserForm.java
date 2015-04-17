package pula.sys.forms;

import puerta.system.po.SysRole;
import pula.sys.domains.Branch;
import pula.sys.domains.SysUser;
import pula.sys.domains.SysUserGroup;

public class SysUserForm extends SysUser {

	private boolean changePassword;
	private String groupId;

	private String roleId;
	private long branchId;

	private int[] types;

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getBranchId() {
		return branchId;
	}

	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}

	public SysUser toSysUser() {
		SysUser adm = new SysUser();
		adm.setName(this.name);
		adm.setPassword(this.password);
		adm.setId(this.id);
		// adm.getRole();
		adm.setLoginId(this.getLoginId());
		adm.setRole(SysRole.create(roleId));
		adm.setEnabled(this.enabled);
		adm.setBelongsToGroup(SysUserGroup.create(groupId));
		adm.setBranch(Branch.create(branchId));
		// adm.setPrinter(this.getPrinter());
		// adm.setCamNo(this.getCamNo());
		// adm.setPosition(this.getPosition());
		// List<SysUserRole> roles = WxlSugar.newArrayList();
		// if (roleId != null) {
		// for (String rid : roleId) {
		// roles.add(SysUserRole.createByRoleId(rid));
		// }
		// }

		return adm;
	}

}
