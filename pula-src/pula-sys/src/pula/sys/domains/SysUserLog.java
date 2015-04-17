package pula.sys.domains;

import java.util.Calendar;

import puerta.support.dao.IWxlLogger;

/**
 * 系统日志
 * 
 * @author tiyi
 * 
 */
public class SysUserLog implements IWxlLogger {

	private String id;
	private SysUser sysUser;
	private Calendar eventTime;
	private String event;
	private String extendInfo;
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar eventTime) {
		this.eventTime = eventTime;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	@Override
	public void setActorId(String actorId) {
		SysUser usr = new SysUser();
		usr.setId(actorId);
		this.sysUser = usr;
	}

}
