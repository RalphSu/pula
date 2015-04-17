package pula.sys.helpers;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import puerta.support.utils.StringTool;
import puerta.system.vo.MenuVo;

public class HgMenuHelper {

	public static String toString(List<MenuVo> ms, boolean strict) {
		// 排序，如果排出来了。就开始展开
		Collections.sort(ms);

		StringBuilder sb = new StringBuilder();
		// boolean first = true;
		// int lastLevel = -1;
		MenuVo last = null;
		for (MenuVo v : ms) {
			// System.out.println(v.getNo() + ">>" + v.getTreePath());
			// module , begin
			// if (v.getLevel() == -1) {
			//
			// if (!first) {
			// sb.append("},\n");
			// }
			//
			// first = false;
			//
			// }

			if (last == null) {
				// 首个
			} else {
				//
				if (last.getLevel() < v.getLevel()) {
					// 深入，说明当前节点是上一个节点的子

					sb.append(",\"submenu\":{ \"id\":\"").append(last.getNo())
							.append("\",\"itemdata\":[\n");

				} else if (last.getLevel() == v.getLevel()) {

					// 上一个节点需要关闭了。
					sb.append("},\n");
				} else {

					// 一下子跳跃了好几个，有几个就要关几次
					// int left = last.getLevel() - v.getLevel();
					appendfix(sb, last.getLevel(), v.getLevel(), false);
				}
			}

			// output content
			outputContent(sb, v, strict);

			last = v;

		}
		if (last != null)
			appendfix(sb, last.getLevel(), -1, true);
		return "[\n" + sb.toString() + "]";

	}

	private static void appendfix(StringBuilder sb, int from, int to,
			boolean eof) {
		sb.append("}\n");

		for (int i = from; i > to; i--) {
			// 缩进
			StringTool.fillChar(sb, "\t", i + 2);
			sb.append("]}\n");

			StringTool.fillChar(sb, "\t", i + 1);
			sb.append("}");
			if (i == to + 1 && !eof) {
				sb.append(",");
			}
			sb.append("\n");
		}

	}

	private static void outputContent(StringBuilder sb, MenuVo v, boolean strict) {
		// 缩进
		StringTool.fillChar(sb, "\t", v.getLevel() + 2);
		// 输出内容
		sb.append("{\"text\":\"").append(v.getText()).append("\", \"no\" :\"")
				.append(v.getNo()).append("\"");

		if (!StringUtils.isEmpty(v.getUrl())) {
			sb.append(",\"onclick\": { \"fn\":").append(strict(strict))
					.append(", \"obj\":[\"").append(v.getUrl()).append("\",\"")
					.append(v.getNo()).append("\",\"").append(v.getText())
					.append("\"] }");
		}

		// submenu & onclick

	}

	private static String strict(boolean strict) {
		if (!strict) {
			return "\"onMenuItemClick\"";
		} else {
			return "onMenuItemClick";
		}

	}

}
