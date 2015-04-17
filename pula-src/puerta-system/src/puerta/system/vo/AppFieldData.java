package puerta.system.vo;

public class AppFieldData {

	private String path;
	private String no;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public static AppFieldData create(String p, String m) {
		AppFieldData d = new AppFieldData();
		d.no = p;
		d.path = m;
		return d;
	}
}
