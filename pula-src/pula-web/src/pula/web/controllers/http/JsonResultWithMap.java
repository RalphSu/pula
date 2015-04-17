package pula.web.controllers.http;

import puerta.system.vo.MapBean;

public class JsonResultWithMap {

	private String no;
	private String message;
	private String redirectTo;
	private MapBean data;
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

	public MapBean getData() {
		return data;
	}

	public void setData(MapBean data) {
		this.data = data;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static JsonResultWithMap error(Exception e) {
		JsonResultWithMap m = new JsonResultWithMap();
		m.error = true;
		m.message = e.getMessage();

		return m;
	}

}
