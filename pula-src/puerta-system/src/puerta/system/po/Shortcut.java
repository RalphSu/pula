/**
 * Created on 2006-12-5 07:49:45
 *
 * DiagCN.COM 2004-2006
 * $Id: Shortcut.java,v 1.1 2006/12/05 12:56:08 tiyi Exp $
 */
package puerta.system.po;

/**
 * 快捷方式,和权限关联 拥有权限者自动拥有该快捷方式 但是是否显示在首页上，还要根据用户设定而来
 * 
 * @author tiyi
 * 
 */
public class Shortcut {
	private String id;

	private String name;

	private String no;

	private Purview purview;

	private boolean removed;

	private int indexNo;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Purview getPurview() {
		return purview;
	}

	public void setPurview(Purview purview) {
		this.purview = purview;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

}
