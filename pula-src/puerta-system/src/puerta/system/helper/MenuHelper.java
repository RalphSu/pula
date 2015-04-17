package puerta.system.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import puerta.support.utils.StringTool;
import puerta.support.utils.WxlSugar;
import puerta.system.po.Module;
import puerta.system.po.Purview;
import puerta.system.vo.MenuVo;

public class MenuHelper {

	public static String toString(List<MenuVo> ms, boolean strict) {
		// 排序，如果排出来了。就开始展开
		Collections.sort(ms);

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		int lastLevel = -1;
		MenuVo last = null;
		for (MenuVo v : ms) {
			System.out.println(v.getNo() + ">>" + v.getTreePath());
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
					.append("contentFrame").append("\"] }");
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

	public static List<MenuVo> transferModule(List<Module> modules) {

		List<MenuVo> m = WxlSugar.newArrayList();
		List<String> keys = WxlSugar.newArrayList();

		for (Module mdl : modules) {
			if (keys.indexOf(mdl.getNo()) >= 0) {
				continue;
			}

			// make menu
			MenuVo v = new MenuVo();
			v.setText(mdl.getName());
			v.setNo(mdl.getNo());
			v.setTreePath("M" + StringTool.fillZero(mdl.getIndexNo(), 3));
			v.setExtData(mdl.getId());
			v.setLevel(-1); // module ;
			m.add(v);

			keys.add(mdl.getNo());

		}

		return m;
	}

	public static List<MenuVo> transferPurview(List<Purview> purviews) {

		List<MenuVo> m = WxlSugar.newArrayList();
		List<String> keys = WxlSugar.newArrayList();

		for (Purview mdl : purviews) {
			if (keys.indexOf(mdl.getNo()) >= 0) {
				continue;
			}

			// make menu
			MenuVo v = new MenuVo();
			v.setText(mdl.getName());
			v.setNo(mdl.getNo());
			v.setTreePath(mdl.getTreePath());
			v.setExtData(mdl.getId());
			v.setLevel(mdl.getLevel()); // module ;
			v.setUrl(mdl.getDefaultURL());
			m.add(v);

			keys.add(mdl.getNo());

		}

		return m;
	}

	public static List<MenuVo> mix(List<MenuVo> v1, List<MenuVo> v2) {
		// 合并
		// 就是要把重复的去掉
		Map<String, String> exists = WxlSugar.newHashMap();
		for (MenuVo v : v1) {
			exists.put(v.getNo(), v.getNo());
		}

		// 不重复的
		for (MenuVo v : v2) {
			if (exists.containsKey(v.getNo())) {
				continue;
			}
			v1.add(v);
		}
		return v1;
	}

	public static void main(String[] args) {
		List<MenuVo> m = new ArrayList<MenuVo>();

		m.add(MenuVo.create("A", "AA", -1, "M010"));
		m.add(MenuVo.create("B", "BB", -1, "M020"));
		m.add(MenuVo.create("P1", "P1", 0, "M010-P050"));
		m.add(MenuVo.create("P0", "P0", 0, "M010-P040"));
		m.add(MenuVo.create("P3", "P3", 0, "M020-P010"));
		m.add(MenuVo.create("P3=1", "P3=1", 1, "M020-P010-P40"));
		MenuVo mv = MenuVo.create("P3=2", "P3=2", 1, "M020-P010-P50");
		mv.setUrl("http://news.sina.com.cn");
		m.add(mv);

		System.out.println(toString(m, true));
	}
}
