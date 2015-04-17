/**
 * Created on 2007-2-17 10:47:56
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.base;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import puerta.support.utils.StringTool;
import puerta.support.vo.TreeNodeDTO;
import puerta.system.po.Module;
import puerta.system.po.Purview;

/**
 * 
 * @author tiyi
 * 
 */
public class PurviewJSon {

	public static TreeNodeDTO getDTO(Purview p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		d.setRemark(p.getDefaultURL());
		d.setLevel(p.getLevel());
		return d;
	}

	public static void makeJson(StringBuilder sb, List<TreeNodeDTO> menus,
			boolean moduleLevel) {
		for (Iterator<TreeNodeDTO> iter = menus.iterator(); iter.hasNext();) {
			TreeNodeDTO d = iter.next();
			makeJson(sb, d, moduleLevel);

			if (iter.hasNext()) {
				sb.append(",");
			}
		}
	}

	public static void makeJson(StringBuilder sb, TreeNodeDTO dto,
			boolean moduleLevel) {
		String t = "p";
		if (moduleLevel && dto.getLevel() == 0) {
			t = "m";
		}
		if (dto.hasChildren()) {
			sb.append("\n");
			StringTool.fillChar(sb, "\t", dto.getLevel() + 1);

			sb.append("{\"label\":\"").append(dto.getName()).append("\"");
			// if (!StringUtils.isEmpty(dto.getRemark())) {
			sb.append(",\"type\":\"").append(t).append("\",\"id\":\"")
					.append(dto.getId()).append("\"");
			sb.append(",\"last\":").append(
					BooleanUtils.toStringTrueFalse((dto.isLast())));
			sb.append(",\"first\":").append(
					BooleanUtils.toStringTrueFalse((dto.isFirst())));
			// }
			sb.append(",\"checked\":").append(
					BooleanUtils.toStringTrueFalse((dto.isChecked())));
			sb.append(",\"children1\":[");
			makeJson(sb, dto.getNodes(), false);
			sb.append("]}");
		} else {
			sb.append("\n");
			StringTool.fillChar(sb, "\t", dto.getLevel() + 1);

			sb.append("{\"label\":\"").append(dto.getName()).append("\"");
			// if (!StringUtils.isEmpty(dto.getRemark())) {

			sb.append(",\"type\":\"").append(t).append("\",\"id\":\"")
					.append(dto.getId()).append("\"");
			// }
			sb.append(",\"last\":").append(
					BooleanUtils.toStringTrueFalse((dto.isLast())));
			sb.append(",\"first\":").append(
					BooleanUtils.toStringTrueFalse((dto.isFirst())));
			sb.append(",\"checked\":").append(
					BooleanUtils.toStringTrueFalse((dto.isChecked())));
			sb.append(",\"disabled\":").append(
					BooleanUtils.toStringTrueFalse(("disabled".equals(dto
							.getRemark()))));
			sb.append("}");
		}

	}

	public static void addToTree(Map<Object, Object> map, TreeNodeDTO root,
			Purview p) {

		TreeNodeDTO thisDto = getDTO(p);
		if (p.getParentPurview() != null) {
			if (map.containsKey(p.getParentPurview().getId())) {
				TreeNodeDTO dto = (TreeNodeDTO) map.get(p.getParentPurview()
						.getId());

				dto.addChildNode(thisDto);
				// return;
			} else {
				// logger.debug("p.name=" + p.getName());
				// logger.debug(";p.getParent=" + p.getParentPurview().getId());
				// logger.debug(";p.getParentName="
				// + p.getParentPurview().getName());
			}

		} else {
			root.addChildNode(thisDto);
		}

		map.put(p.getId(), thisDto);

	}

	public static TreeNodeDTO getDTO(Module p) {
		TreeNodeDTO d = new TreeNodeDTO();
		d.setId(p.getId());
		d.setNo(p.getNo());
		d.setName(p.getName());
		return d;
	}

	public static void makeMenuTree(List<Purview> purviews, Module m,
			StringBuffer sb, String target, boolean strict) {
		MenuMaker mm = new MenuMaker(target, strict);
		mm.put(purviews, m, sb);

	}

}
