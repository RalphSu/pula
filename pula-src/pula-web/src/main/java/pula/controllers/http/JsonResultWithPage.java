package pula.controllers.http;

import puerta.system.vo.MapBean;
import pula.vo.PageInfo;

public class JsonResultWithPage {
	private String no;
	private String message;
	private String redirectTo;
	private PageInfo<MapBean> data;
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

	public PageInfo<MapBean> getData() {
		return data;
	}

	public void setData(PageInfo<MapBean> data) {
		this.data = data;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public static JsonResultWithPage error(Exception e) {
		JsonResultWithPage m = new JsonResultWithPage();
		m.error = true;
		m.message = e.getMessage();

		return m;
	}

}
