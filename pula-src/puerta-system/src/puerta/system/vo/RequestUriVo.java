package puerta.system.vo;

import puerta.system.po.RequestUri;

public class RequestUriVo {
	private String uri, text;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String no) {
		this.uri = no;
	}

	public String getText() {
		return text;
	}

	public void setText(String name) {
		this.text = name;
	}

	public static RequestUriVo create(String uri, String string, String id) {
		RequestUriVo v = new RequestUriVo();
		v.uri = uri;
		v.text = string;
		v.id = id;
		return v;
	}

	public static RequestUriVo create(RequestUri ri) {
		if (ri.getAssignedCount() == 0) {
			return create(ri.getUri(), ri.getUri(), ri.getId());
		}
		return create(ri.getUri(), ri.getUri() + "(" + ri.getAssignedCount()
				+ ")", ri.getId());
	}
}
