/*
 * Created on 2005-1-14
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import puerta.support.utils.StringTool;

/**
 * @author tiyi 2005-1-14
 */
public class TreeNodeDTO {

	private String id;

	private String name;

	private String no;

	private String parentId;

	private String remark;
	private String type;

	private List<TreeNodeDTO> nodes = new ArrayList<TreeNodeDTO>();

	private Map<Object, Object> map = new LinkedHashMap<Object, Object>();

	private boolean checked;

	private int level;

	private TreeNodeDTO firstChildNode, lastChildNode;

	private boolean last = true, first = true;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addChildNode(TreeNodeDTO d) {
		if (firstChildNode == null) {
			firstChildNode = d;
			d.setFirst(true);
		} else {
			d.setFirst(false);
		}

		// always = d.setLast(true);
		if (lastChildNode == null) {

		} else {
			lastChildNode.setLast(false);
		}

		lastChildNode = d;

		d.setLevel(this.level + 1);

		this.nodes.add(d);
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public TreeNodeDTO getFirstChildNode() {
		return firstChildNode;
	}

	public void setFirstChildNode(TreeNodeDTO firstChildNode) {
		this.firstChildNode = firstChildNode;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public TreeNodeDTO getLastChildNode() {
		return lastChildNode;
	}

	public void setLastChildNode(TreeNodeDTO lastChildNode) {
		this.lastChildNode = lastChildNode;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Collection<Object> getMapValues() {
		return map.values();
	}

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
	 * @return
	 */
	public List<TreeNodeDTO> getNodes() {
		return nodes;
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
	 * @param list
	 */
	public void setNodes(List<TreeNodeDTO> list) {
		nodes = list;
	}

	/**
	 * @return
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param b
	 */
	public void setChecked(boolean b) {
		checked = b;
	}

	/**
	 * @return
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param string
	 */
	public void setParentId(String string) {
		parentId = string;
	}

	/**
	 * @return
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param string
	 */
	public void setRemark(String string) {
		remark = string;
	}

	/**
	 * @return
	 */
	public Map<Object, Object> getMap() {
		return map;
	}

	/**
	 * @param map
	 */
	public void setMap(Map<Object, Object> map) {
		this.map = map;
	}

	public boolean hasChildren() {

		if (this.nodes == null || this.nodes.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static String toJson(List<TreeNodeDTO> lists, TreeNodeDTO root) {
		StringBuilder sb = new StringBuilder();
		sb.append("[{label:\"").append(root.getName()).append("\",id:\"")
				.append(root.getId()).append("\",type:\"v\",children:");
		sb.append("[\n");

		makeJson(lists, sb);

		sb.append("\n ]}] ");

		return sb.toString();
	}

	/**
	 * @param lists
	 * @param sb
	 */
	private static void makeJson(List<TreeNodeDTO> lists, StringBuilder sb) {
		for (Iterator<TreeNodeDTO> iter = lists.iterator(); iter.hasNext();) {
			TreeNodeDTO d = iter.next();
			d.toJson(sb);

			if (iter.hasNext()) {
				sb.append(",");
			}
		}
	}

	/**
	 * @param sb
	 */
	private void toJson(StringBuilder sb) {
		TreeNodeDTO dto = this;
		sb.append("\n");
		StringTool.fillChar(sb, "\t", dto.getLevel() + 1);

		sb.append("{\"label\":\"").append(
				StringUtils.defaultIfEmpty(dto.getName(), "--")).append("\"");

		sb.append(",\"type\":\"").append(dto.getType()).append("\",\"id\":\"")
				.append(dto.getId()).append("\"");
		sb.append(",\"last\":").append(
				BooleanUtils.toStringTrueFalse((dto.isLast())));
		sb.append(",\"first\":").append(
				BooleanUtils.toStringTrueFalse((dto.isFirst())));

		sb.append(",\"checked\":").append(
				BooleanUtils.toStringTrueFalse((dto.isChecked())));

		if (dto.hasChildren()) {
			sb.append(",mychildren:[");

			makeJson(dto.getNodes(), sb);
			sb.append("]}");
		}
	}

}
