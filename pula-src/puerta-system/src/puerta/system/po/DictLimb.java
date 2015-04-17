/**
 * Created on 2009-2-20 10:10:09
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.system.po;

import puerta.support.annotation.WxlDomain;
import puerta.support.dao.LoggablePo;
import puerta.support.dao.TreePathPo;

/**
 * 
 * @author tiyi
 * 
 */
@WxlDomain
public class DictLimb implements LoggablePo, TreePathPo<DictLimb, String> {

	private String no, name;
	private String id;
	private DictLimb parent;
	private int indexNo; // same level ;
	private int level;
	private String treePath;
	private boolean removed;
	private String parentNo;

	public String getId() {
		return id;
	}

	/**
	 * @param attributeValue
	 */
	public DictLimb(String attributeValue) {
		this.no = attributeValue;
	}

	public DictLimb() {
	}

	public String getName() {
		return name;
	}

	public String getParentNo() {
		return parentNo;
	}

	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getIdentify() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DictLimb getParent() {
		return parent;
	}

	public void setParent(DictLimb parent) {
		this.parent = parent;
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

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("no=").append(no).append(",name=").append(name)
				.append(" treePath=").append(this.treePath);
		return sb.toString();
	}

	public static DictLimb create(String categoryId) {
		if (categoryId == null || "".equals(categoryId)) {
			return null;
		}
		DictLimb d = new DictLimb();
		d.id = categoryId;
		return d;

	}

	@Override
	public String toLogString() {
		return this.no + "(" + this.name + ")";
	}

	public static DictLimb create(String no, String name) {
		DictLimb dl = new DictLimb();
		dl.no = no;
		dl.name = name;
		return dl;
	}
}
