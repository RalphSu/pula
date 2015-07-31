package puerta.system.po;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

public class Purview {
    @JsonProperty
	private Module module;
    @JsonProperty
	private String id;
    @JsonProperty
	private String name;
    @JsonProperty
	private boolean menuItem = false;
    @JsonProperty
	private boolean visible = true, removed = false;
    @JsonProperty
	private String no;
	private Purview parentPurview;
    @JsonProperty
	private int indexNo;
    @JsonProperty
	private int level;
    @JsonProperty
	private String treePath;
    @JsonProperty
	private String defaultURL;
    @JsonProperty
	private boolean leaf;
    @JsonProperty
	private AppField appField;

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField applicationScope) {
		this.appField = applicationScope;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getDefaultURL() {
		return defaultURL;
	}

	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
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

	public Purview getParentPurview() {
		return parentPurview;
	}

	public void setParentPurview(Purview parentPurview) {
		this.parentPurview = parentPurview;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isMenuItem() {
		return menuItem;
	}

	public void setMenuItem(boolean menuItem) {
		this.menuItem = menuItem;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getTreePath() {
		return treePath;
	}

	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getIndentName() {
		String str = "|" + StringUtils.repeat(" ", this.level);
		if (this.level != 0) {
			// str += "|";
		}
		return str + "|-" + this.name;
	}

	public static Purview create(String attribute) {
		if (StringUtils.isEmpty(attribute)) {
			return null;
		}
		Purview p = new Purview();
		p.setId(attribute);
		return p;
	}

	public static Purview createByNo(String s) {
		Purview p = new Purview();
		p.setNo(s);
		return p;
	}
}
