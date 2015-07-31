/**
 * Created on 2010-2-22
 * WXL 2009
 * $Id$
 */
package puerta.system.po;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 角色 用於關聯權限等
 * 
 * @author tiyi
 * 
 */
public class SysRole {
    @JsonProperty
	private String id;
    @JsonProperty
	private String no;
    @JsonProperty
	private boolean removed;
    @JsonProperty
	private String name;
    @JsonProperty
	private AppField appField;
	private List<Purview> purviews; // 非po属性

	public List<Purview> getPurviews() {
		return purviews;
	}

	public void setPurviews(List<Purview> purviews) {
		this.purviews = purviews;
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

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField appField) {
		this.appField = appField;
	}

	public static SysRole create(String rid) {
		SysRole ar = new SysRole();
		ar.id = rid;
		return ar;
	}

}
