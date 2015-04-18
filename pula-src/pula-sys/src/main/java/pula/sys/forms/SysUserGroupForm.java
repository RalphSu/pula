package pula.sys.forms;

import pula.sys.domains.SysUserGroup;

public class SysUserGroupForm extends SysUserGroup {
	public SysUserGroup toSysUserGroup() {
		SysUserGroup obj = new SysUserGroup();
		obj.setEnabled(this.isEnabled());
		obj.setId(this.getId());
		obj.setName(this.getName());
		obj.setNo(this.getNo());
		return obj;
	}

}