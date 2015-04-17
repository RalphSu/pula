package pula.sys.domains;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.IWxlActor;
import puerta.support.dao.IWxlLogger;
import puerta.support.dao.LoggablePo;
import puerta.system.po.SysRole;

/*
 * 系统用户
 */

@WxlDomain("用户")
public class SysUser implements LoggablePo, IWxlActor {

	protected String id;
	protected String password;
	protected String name;
	protected boolean enabled;
	private boolean removed;
	private Calendar createdTime, updatedTime;
	private String loginId;
	private SysUserGroup belongsToGroup;
	private SysRole role; // 从数据库查出对应的ID，根据固化的no
	// private int role; // 系统角色是固化的，是根据账号的自身信息确定的。

	private Salesman salesman; // 关联
	private Branch branch;// 从属分支机构

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public SysUserGroup getBelongsToGroup() {
		return belongsToGroup;
	}

	public void setBelongsToGroup(SysUserGroup belongsToGroup) {
		this.belongsToGroup = belongsToGroup;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}

	public Salesman getSalesman() {
		return salesman;
	}

	public void setSalesman(Salesman salesman) {
		this.salesman = salesman;
	}

	@Override
	public String toLogString() {
		return this.loginId + "(" + this.name + ")";
	}

	@Override
	public String getActorId() {
		return this.getId();
	}

	@Override
	public IWxlLogger getNewLogger() {
		return new SysUserLog();
	}

	public static SysUser create(String actorId) {
		if (StringUtils.isEmpty(actorId))
			return null;
		SysUser s = new SysUser();
		s.setId(actorId);
		return s;
	}

	public static SysUser createByLoginId(String managerNo) {
		SysUser su = new SysUser();
		su.loginId = managerNo;
		return su;
	}
}
