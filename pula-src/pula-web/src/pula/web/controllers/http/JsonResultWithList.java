package pula.web.controllers.http;

import puerta.system.vo.MapList;

public class JsonResultWithList {
	private String no;
	private String message;
	private String redirectTo;
	private MapList data;
	private boolean error;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public MapList getData() {
		return data;
	}

	public void setData(MapList data) {
		this.data = data;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static JsonResultWithList error(Exception e) {
		JsonResultWithList m = new JsonResultWithList();
		m.error = true;
		m.message = e.getMessage();

		return m;
	}

}
