/*
 * Created on 2005-7-8
 *$Id: Module.java,v 1.3 2006/12/06 13:36:28 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.po;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 模块
 * 
 * @author tiyi 2005-7-8 23:13:36
 */
@WxlDomain
public class Module implements LoggablePo {

	private String id;

	private String no; //

	private String name; //

	private int indexNo; //

	private String comments;

	private boolean removed;

	private AppField appField;

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setNo(String string) {
		no = string;
	}

	/**
	 * @return
	 */
	public int getIndexNo() {
		return indexNo;
	}

	/**
	 * @param i
	 */
	public void setIndexNo(int i) {
		indexNo = i;
	}

	/**
	 * @return
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param string
	 */
	public void setComments(String string) {
		comments = string;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField appField) {
		this.appField = appField;
	}

	@Override
	public String toLogString() {
		return "模块";
	}

}
