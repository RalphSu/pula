/**
 * Created on 2006-12-7 01:29:23
 *
 * DiagCN.COM 2004-2006
 * $Id: UserLogCondition.java,v 1.2 2006/12/11 11:31:53 tiyi Exp $
 */
package pula.sys.conditions;

import puerta.support.dao.CommonCondition;

/**
 * 
 * @author tiyi
 * 
 */
public class SysUserLogCondition extends CommonCondition {

	private String beginDate, endDate;

	private String userId, extendInfo, event;

	private String vendee, domain;

	private String domainId;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getVendee() {
		return vendee;
	}

	public void setVendee(String vendee) {
		this.vendee = vendee;
	}

	public String getExtendInfo() {
		return extendInfo;
	}

	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

}
