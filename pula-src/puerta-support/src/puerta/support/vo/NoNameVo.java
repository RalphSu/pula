package puerta.support.vo;

import java.io.Serializable;

public class NoNameVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5427216474968440044L;
	private String no, name, id;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public NoNameVo() {

	}

	public NoNameVo(String no2, String name2) {
		this.no = no2;
		this.name = name2;
	}

	public static NoNameVo create(String id2, String no2, String name2) {
		NoNameVo v = new NoNameVo();
		v.id = id2;
		v.no = no2;
		v.name = name2;
		return v;
	}

	public long getIdLong() {
		return Long.parseLong(this.id);
	}

}
