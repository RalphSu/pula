/**
 * Created on 2006-12-5 08:10:54
 *
 * DiagCN.COM 2004-2006
 * $Id: AdminLog.java,v 1.1 2006/12/05 12:56:08 tiyi Exp $
 */
package puerta.system.po;

import java.util.Calendar;

import puerta.support.dao.IWxlLogger;

/**
 * 管理员日志
 * 
 * @author tiyi
 * 
 */
public class InsiderLog implements IWxlLogger {

	private String id; // ???

	private Insider insider; // ???

	private Calendar eventTime; // ??????

	private String event; // ???

	private String extendInfo; //

	private String ip;

	public Insider getInsider() {
		return insider;
	}

	public void setInsider(Insider insider) {
		this.insider = insider;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar eventTime) {
		this.eventTime = eventTime;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setActorId(String actorId) {
		Insider ins = new Insider();
		ins.setId(actorId);
		this.insider = ins;
	}

}
