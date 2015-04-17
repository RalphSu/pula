package puerta.system.po;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import puerta.support.vo.SelectOption;
import puerta.system.helper.ParameterHelper;

public class Parameter {

	public static final int TYPE_FILE_PATH = 1001;

	public static final int TYPE_WEB_PATH = 1002;

	public static final int TYPE_TIME = 1003;

	public static final int TYPE_DATE = 1004;

	public static final int TYPE_STRING = 1005;

	public static final int TYPE_INT = 1006;

	public static final int TYPE_DOUBLE = 1007;

	public static final int TYPE_MONEY = 1008;

	public static final int TYPE_DATETIME = 1009;

	public static final int TYPE_FILE = 1010;

	public static final int TYPE_DICT = 1011;

	public static final int TYPE_BOOL = 1012;

	public static final int TYPE_CLASSPATH = 1013;

	public static final int TYPE_PASSWORD = 1014;

	private String id;

	private String no;

	private String name;

	private String value;

	private int indexNo;

	private int magicNo;

	private int paramType;

	private String mask;

	private boolean removed;

	private AppField appField;
	private ParameterPage page;
	private ParameterFolder folder;

	public AppField getAppField() {
		return appField;
	}

	public void setAppField(AppField appField) {
		this.appField = appField;
	}

	public ParameterPage getPage() {
		return page;
	}

	public void setPage(ParameterPage page) {
		this.page = page;
	}

	public ParameterFolder getFolder() {
		return folder;
	}

	public void setFolder(ParameterFolder folder) {
		this.folder = folder;
	}

	public boolean isRemoved() {
		return removed;
	}

	public void setRemoved(boolean removed) {
		this.removed = removed;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public int getParamType() {
		return paramType;
	}

	public void setParamType(int paramType) {
		this.paramType = paramType;
	}

	public int getMagicNo() {
		return magicNo;
	}

	public void setMagicNo(int magicNo) {
		this.magicNo = magicNo;
	}

	public int getIndexNo() {
		return indexNo;
	}

	public void setIndexNo(int indexNo) {
		this.indexNo = indexNo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParamTypeText() {
		return ParameterHelper.getName(this.paramType);
	}

	public boolean isHandleEditor() {
		switch (this.paramType) {
		case TYPE_BOOL:
			// case TYPE_DICT:
			return true;
		}

		return false;
	}

	public String getEditor() {
		if ((TYPE_DICT == this.paramType)) {
			String[] dicts = StringUtils.split(this.mask, ",");
			List<SelectOption> ls = new ArrayList<SelectOption>();
			for (int i = 0; i < dicts.length; i++) {
				String[] items = StringUtils.split(dicts[i], "-");
				if (items.length < 2)
					continue;
				SelectOption so = new SelectOption();

				so.setId(items[1]);
				so.setName(items[0]);
				so.setSelected(StringUtils.equals(items[1], this.value));
				ls.add(so);
			}

			return SelectOption.transferToSelect("values", ls);
		} else if (TYPE_BOOL == this.paramType) {
			List<SelectOption> ls = new ArrayList<SelectOption>();

			SelectOption so = new SelectOption(ParameterHelper.YES, "Yes");
			so.setSelected(StringUtils.equals(so.getId(), this.value));
			ls.add(so);
			so = new SelectOption(ParameterHelper.NO, "No");
			so.setSelected(StringUtils.equals(so.getId(), this.value));
			ls.add(so);
			return SelectOption.transferToSelect("values", ls);
		}

		return null;
	}

	public String getEditorType() {

		if ((TYPE_PASSWORD == this.paramType)) {
			return "password";
		}
		return "text";

	}

}
