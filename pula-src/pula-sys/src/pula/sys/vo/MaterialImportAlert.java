package pula.sys.vo;

/**
 * 导入的警告信息
 * 
 * @author tiyi
 * 
 */
public class MaterialImportAlert {

	public static final int INFO = 1;
	public static final int WARN = 2;
	public static final int ERROR = 3;
	public static final String GROUP_NOT_EXISTS = "##GROUP_NOT_EXISTS";
	public static final String MATERIAL_NOT_EXISTS = "##MATERIAL_NOT_EXISTS";
	public static final String EMPTY_NO = "##EMPTY_NO";
	public static final String QUANTITY_INVAILD = "##QUANTITY_INVAILD";
	public static final String TOTALS = "##TOTALS";
	public static final String ERROR_NO = "##ERROR_NO";
	public static final String NOT_MATCH_FROMTYPE = "##NOT_MATCH_FROMTYPE";
	public static final String NO_EXISTS = "##NO_EXISTS";
	public static final String ERROR_VOLUME = "##ERROR_VOL";
	public static final String NEW = "##NEW";
	public static final String EDIT = "##EDIT";
	public static final String MISS_MATERIAL_TYPE = "##MISS_MATERIAL_TYPE";

	private String no, name;
	private int type;
	private String message;
	private String spec; // 涉及数量

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypeName() {
		if (type == INFO) {
			return "信息";
		}
		if (type == WARN) {
			return "警告";
		}
		if (type == ERROR) {
			return "错误";
		}
		return "";
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static MaterialImportAlert error(String no2, String name,
			String spec, String string) {
		MaterialImportAlert p = new MaterialImportAlert();
		p.no = no2;
		p.spec = spec;
		p.message = string;
		p.type = ERROR;
		p.name = name;
		return p;
	}

	public static MaterialImportAlert warn(String no2, String name,
			String spec, String string) {
		MaterialImportAlert p = new MaterialImportAlert();
		p.no = no2;
		p.spec = spec;
		p.message = string;
		p.type = WARN;
		p.name = name;
		return p;
	}

	public static MaterialImportAlert info(String no2, String name,
			String spec, String string) {
		MaterialImportAlert p = new MaterialImportAlert();
		p.no = no2;
		p.spec = spec;
		p.message = string;
		p.type = INFO;
		p.name = name;
		return p;
	}

}
