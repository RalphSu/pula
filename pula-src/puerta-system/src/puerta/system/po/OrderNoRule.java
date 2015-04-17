/**
 * Created on 2007-6-6 05:48:09
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.po;

import puerta.support.annotation.WxlDomain;

/**
 * 
 * @author tiyi
 * 
 */
@WxlDomain
public class OrderNoRule {

	private String no;

	private String name;

	private boolean removed;

	private String id;

	private String dateFormat;

	private String prefix, suffix;

	private int noLength, cacheRule;

	private boolean reCountByDay;

	public int getCacheRule() {
		return cacheRule;
	}

	public void setCacheRule(int cacheRule) {
		this.cacheRule = cacheRule;
	}

	public OrderNoRule() {
		super();
	}

	public OrderNoRule(String no) {
		this.no = no;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoLength() {
		return noLength;
	}

	public void setNoLength(int noLength) {
		this.noLength = noLength;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isReCountByDay() {
		return reCountByDay;
	}

	public void setReCountByDay(boolean reCountByDay) {
		this.reCountByDay = reCountByDay;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
