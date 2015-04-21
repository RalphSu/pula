package puerta.system.po;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import puerta.system.vo.AppFieldVo;

@WxlDomain
public class AppField implements LoggablePo {

	private String id;
	private String no;
	private String name;
	private String path;
	private boolean removed;
	private int indexNo;
	private String comments;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public static AppField create(AppFieldVo vo) {
		AppField af = new AppField();
		af.setNo(vo.getNo());
		af.setName(vo.getName());
		af.setPath(vo.getPath());
		return af;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

}