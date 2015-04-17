/**
 * Created on 2007-5-7 05:52:35
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.po;

/**
 * 参数页面
 * 
 * @author tiyi
 * 
 */
public class ParameterPage {

	private String id;

	private String name;

	private String no;

	private int indexNo;

	private AppField appField;

	private boolean removed;

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField appField) {
		this.appField = appField;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}
