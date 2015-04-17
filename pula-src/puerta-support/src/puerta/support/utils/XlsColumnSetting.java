package puerta.support.utils;

public class XlsColumnSetting {
	private String name;
	private int length;
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public static XlsColumnSetting create(String name, int len) {
		XlsColumnSetting cs = new XlsColumnSetting();
		cs.name = name;
		cs.length = len;
		return cs;
	}

	public static XlsColumnSetting create(String name, String key, int len) {
		XlsColumnSetting cs = new XlsColumnSetting();
		cs.name = name;
		cs.length = len;
		cs.key = key;
		return cs;
	}
}
