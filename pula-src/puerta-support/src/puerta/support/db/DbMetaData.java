package puerta.support.db;

public class DbMetaData {
	private int maxLength;
	private String name;

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static DbMetaData create(String cn, int maxl) {
		DbMetaData dd = new DbMetaData();
		dd.name = cn;
		dd.maxLength = maxl;
		return dd;
	}

}
