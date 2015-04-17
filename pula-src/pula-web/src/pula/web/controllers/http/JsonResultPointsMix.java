package pula.web.controllers.http;

import pula.web.vo.PointsMix;

public class JsonResultPointsMix {

	private String no;
	private String message;
	private String redirectTo;
	private PointsMix data;
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

	public PointsMix getData() {
		return data;
	}

	public void setData(PointsMix data) {
		this.data = data;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static JsonResultPointsMix error(Exception e) {
		JsonResultPointsMix m = new JsonResultPointsMix();
		m.error = true;
		m.message = e.getMessage();

		return m;
	}

}
