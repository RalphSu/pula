package pula.controllers.http;

import pula.vo.CourseMix;

public class JsonResultCourseMix {

	private String no;
	private String message;
	private String redirectTo;
	private CourseMix data;
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

	public CourseMix getData() {
		return data;
	}

	public void setData(CourseMix data) {
		this.data = data;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static JsonResultCourseMix error(Exception e) {
		JsonResultCourseMix m = new JsonResultCourseMix();
		m.error = true;
		m.message = e.getMessage();

		return m;
	}

}
