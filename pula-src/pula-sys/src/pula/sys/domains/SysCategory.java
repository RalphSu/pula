package pula.sys.domains;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import puerta.support.dao.TreePathPo;

@WxlDomain("系统分类")
public class SysCategory implements LoggablePo,
		TreePathPo<SysCategory, String>, Comparable<SysCategory> {
	private String id;
	private String no;
	private String name;
	private boolean removed;
	private Calendar createdTime, updatedTime;
	private SysCategory parentCategory;
	private int level, indexNo;
	private String treePath;
	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public SysCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(SysCategory parentColumn) {
		this.parentCategory = parentColumn;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public Calendar getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Calendar createdTime) {
		this.createdTime = createdTime;
	}

	public Calendar getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Calendar updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getId() {
		return id;
	}

	public String getIdentify() {
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

	@Override
	public String toLogString() {
		return this.no + "(" + StringUtils.left(this.name, 10) + ")";
	}

	public static SysCategory create(String columnId) {
		if (StringUtils.isEmpty(columnId))
			return null;
		SysCategory cc = new SysCategory();
		cc.id = columnId;
		return cc;
	}

	@Override
	public SysCategory getParent() {
		return this.parentCategory;
	}

	@Override
	public void setParent(SysCategory p) {
		this.parentCategory = p;
	}

	public static SysCategory createByNo(String columnId) {
		if (StringUtils.isEmpty(columnId))
			return null;
		SysCategory cc = new SysCategory();
		cc.no = columnId;
		return cc;
	}

	@Override
	public int compareTo(SysCategory o) {
		return this.getNo().compareTo(o.getNo());
	}

}
