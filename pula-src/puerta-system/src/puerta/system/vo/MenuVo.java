package puerta.system.vo;

import java.util.List;

public class MenuVo implements Comparable<MenuVo> {

	private String text, no;
	private String url;
	private List<MenuVo> items;
	private String treePath;
	private String extData;
	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getExtData() {
		return extData;
	}

	public void setExtData(String extData) {
		this.extData = extData;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuVo> getItems() {
		return items;
	}

	public void setItems(List<MenuVo> items) {
		this.items = items;
	}

	@Override
	public int compareTo(MenuVo o) {
		// 从大到小排
		return this.getTreePath().compareTo(o.getTreePath());

	}

	public static MenuVo create(String no, String name, int i, String tp) {
		MenuVo v = new MenuVo();
		v.no = no;
		v.text = name;
		v.level = i;
		v.treePath = tp;

		return v;
	}

}
