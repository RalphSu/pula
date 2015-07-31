package pula.sys.domains;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;

/**
 * 用户组
 * 
 * @author tiyi
 * 
 */
@WxlDomain("用户组")
public class SysUserGroup implements LoggablePo {
    @JsonProperty
	private String id;
    @JsonProperty
	private String name;
    @JsonProperty
	private String no;
    @JsonProperty
	private boolean removed;
    @JsonProperty
	private boolean enabled;

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

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public static SysUserGroup create(String groupId) {
		if (StringUtils.isEmpty(groupId)) {
			return null;
		}
		SysUserGroup s = new SysUserGroup();
		s.setId(groupId);
		return s;
	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

}
