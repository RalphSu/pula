package puerta.support.utils;

import java.util.List;

import puerta.support.vo.NoNameVo;

public class TreeViewState {

	public static String make(List<NoNameVo> tree) {
		if (tree.size() == 0)
			return null;
		NoNameVo last = tree.get(tree.size() - 1);
		StringBuilder sbParentName = new StringBuilder();
		StringBuilder sbId = new StringBuilder();
		StringBuilder sbNamePath = new StringBuilder();
		StringBuilder sbNoPath = new StringBuilder();
		int level = tree.size();
		int index = 0;
		for (NoNameVo v : tree) {
			if (index == 0) {
				sbParentName.append(v.getName());
			} else {
				sbParentName.append(" >> ").append(v.getName());
			}
			sbId.append("\"").append(v.getId()).append("\"");
			sbNamePath.append("\"").append(sbParentName).append("\"");
			sbNoPath.append("\"").append(v.getNo()).append("\"");

			if (index == tree.size() - 1) {
				// lastone
			} else {
				sbId.append(",");
				sbNamePath.append(",");
				sbNoPath.append(",");
			}

			index++;

		}

		// output time
		StringBuilder sb = new StringBuilder();
		return sb.append("{\"parentId\":\"").append(last.getId())
				.append("\",\"parentName\":\"").append(sbParentName)
				.append("\",\"parentNo\":\"").append(last.getNo())
				.append("\",\"idPath\":[").append(sbId)
				.append("],\"namePath\":[").append(sbNamePath)
				.append("],\"noPath\":[").append(sbNoPath)
				.append("],\"level\":").append(level)
				.append(",\"selectedId\":\"\"}").toString();
	}
}
