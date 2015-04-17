/*
 * Created on 2006-6-28
 *$Id: MenuMaker.java,v 1.3 2006/06/28 06:44:35 tiyi Exp $
 * DiagCN.com (2003-2005)
 */
package puerta.system.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import puerta.support.utils.StringTool;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.po.Module;
import puerta.system.po.Purview;

/**
 * @author tiyi 2006-6-28 11:21:47
 */
public class MenuMaker {

	private List<TreeNodeDTO> menus;

	private Map<String, TreeNodeDTO> map = new LinkedHashMap<String, TreeNodeDTO>();

	private String target;
	private boolean strict;

	/**
	 * @param pTarget
	 */
	public MenuMaker(String pTarget, boolean strict) {
		this.target = pTarget;
		this.strict = strict;
	}

	private TreeNodeDTO getDTO(Purview p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		d.setRemark(p.getDefaultURL());
		d.setLevel(p.getLevel());
		return d;
	}

	private void addToTree(Purview p) {

		if (p.getParentPurview() != null) {
			if (map.containsKey(p.getParentPurview().getId())) {
				TreeNodeDTO dto = (TreeNodeDTO) map.get(p.getParentPurview()
						.getId());

				dto.addChildNode(getDTO(p));
				return;
			}

		}

		map.put(p.getId(), getDTO(p));

	}

	/**
	 * @param purviews
	 * @param m
	 */
	public void put(List<Purview> purviews, Module m, StringBuffer sb) {
		for (Iterator<Purview> iter = purviews.iterator(); iter.hasNext();) {
			Purview p = (Purview) iter.next();
			addToTree(p);
		}
		menus = new ArrayList<TreeNodeDTO>(map.values());

		parseMenuToString(menus, m, sb);

	}

	/**
	 * @param menus
	 * @param sb
	 */
	private void parseMenuToString(List<TreeNodeDTO> menus, Module m,
			StringBuffer sb) {
		// module
		// sb.append("_cmSplit,\n");
		// sb.append("[null,'").append(m.getName()).append("',null,null,'")
		// .append(m.getRemark()).append("',");
		StringTool.fillChar(sb, "\t", 1);

		sb.append("{ \"text\" :\"").append(m.getName())
				.append("\", \"no\" :\"").append(m.getNo())
				.append("\",\"submenu\":{ \"id\":\"").append(m.getNo())
				.append("\",\"itemdata\":[");

		for (Iterator<TreeNodeDTO> iter = menus.iterator(); iter.hasNext();) {
			TreeNodeDTO dto = (TreeNodeDTO) iter.next();

			if (dto.hasChildren()) {
				parseMenuToString(dto.getNodes(), dto, sb);
			} else {
				// sb.append(b)
				sb.append("\n");
				StringTool.fillChar(sb, "\t", 2);

				sb.append("{\"text\":\"").append(dto.getName())
						.append("\",\"no\":\"").append(dto.getNo())
						.append("\"");
				if (!StringUtils.isEmpty(dto.getRemark())) {
					sb.append(",");

					// sb.append(",url:\"").append(dto.getRemark()).append("\",")
					// ;

					if (!StringUtils.isEmpty(target)) {
						appendMenuItemClick(sb, dto);

					} else {
						sb.append("\"url\":\"").append(dto.getRemark())
								.append("\"");
					}
				}

				sb.append("}");

				// sb.append("{text:\"").append(dto.getName()).append("\"}");

				// sb.append("{text:\"").append(dto.getName()).append("\",url:\""
				// )
				// .append(dto.getRemark()).append("\"}");
			}

			if (iter.hasNext()) {
				sb.append(",");
			} else {
				sb.append("\n");
			}

			// purview
			// sb.append("['<img src=\"images/menuitem/").append(dto.getNo())
			// .append(".png\" />',").append("'").append(dto.getName())
			// .append("','").append(dto.getRemark()).append(
			// "',null,'" + dto.getRemark() + "'],");

		}

		// module end
		StringTool.fillChar(sb, "\t", 2);
		sb.append("]}\n\t}");
	}

	/**
	 * @param nodes
	 * @param parentNode
	 * @param sb
	 */
	private void parseMenuToString(List<TreeNodeDTO> nodes,
			TreeNodeDTO parentNode, StringBuffer sb) {
		sb.append("\n");
		StringTool.fillChar(sb, "\t", parentNode.getLevel() + 2);
		sb.append("{\"text\":\"").append(parentNode.getName())
				.append("\" ,\"submenu\":{\"id\":\"")
				.append(parentNode.getNo()).append("\",\"itemdata\": [");

		for (Iterator<TreeNodeDTO> iter = nodes.iterator(); iter.hasNext();) {
			TreeNodeDTO dto = iter.next();

			if (dto.hasChildren()) {
				parseMenuToString(dto.getNodes(), dto, sb);
			} else {
				// sb.append(b)
				sb.append("\n");
				StringTool.fillChar(sb, "\t", dto.getLevel() + 2);
				sb.append("{\"text\":\"").append(dto.getName()).append("\",");
				if (!StringUtils.isEmpty(target)) {
					appendMenuItemClick(sb, dto);

				} else {
					sb.append("\"url\":\"").append(dto.getRemark())
							.append("\"");
				}
				sb.append("}");
			}

			if (iter.hasNext()) {
				sb.append(",");
			} else {
				sb.append("\n");
			}

			// purview
			// sb.append("['<img src=\"images/menuitem/").append(dto.getNo())
			// .append(".png\" />',").append("'").append(dto.getName())
			// .append("','").append(dto.getRemark()).append(
			// "',null,'" + dto.getRemark() + "'],");

		}

		// purview end
		StringTool.fillChar(sb, "\t", parentNode.getLevel() + 3);
		sb.append("]}\n");
		StringTool.fillChar(sb, "\t", parentNode.getLevel() + 2);
		sb.append("}");
		//
		// .append(dto.getRemark()).append("\"}");

	}

	private void appendMenuItemClick(StringBuffer sb, TreeNodeDTO dto) {
		sb.append("\"onclick\": { \"fn\":").append(strict())
				.append(", \"obj\":[\"").append(dto.getRemark())
				.append("\",\"").append(target).append("\"] }");

		// goMenu('").append(dto.getRemark()).append("'");
		// sb.append(",\'" + target + "\')");
	}

	private String strict() {
		if (!strict) {
			return "\"onMenuItemClick\"";
		} else {
			return "onMenuItemClick";
		}

	}
}
