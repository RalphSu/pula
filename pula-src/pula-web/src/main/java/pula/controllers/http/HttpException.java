package pula.controllers.http;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpException extends Exception {

	private int statusCode = -1;
	private int errorCode = -1;
	private String request;
	private String error;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public HttpException(String string, Exception jsone) {
		super(string, jsone);
	}

	public HttpException(String msg, JSONObject json, int statusCode)
			throws JSONException {
		super(msg + "\n error:" + json.getString("error") + " error_code:"
				+ json.getInt("error_code") + json.getString("request"));
		this.statusCode = statusCode;
		this.errorCode = json.getInt("error_code");
		this.error = json.getString("error");
		this.request = json.getString("request");

	}

	public HttpException(String msg, Exception cause, int statusCode) {
		super(msg, cause);
		this.statusCode = statusCode;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -643314887583220060L;

}
