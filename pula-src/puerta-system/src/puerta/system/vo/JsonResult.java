package puerta.system.vo;

import java.util.List;
import java.util.Map;

import puerta.support.utils.WxlSugar;

public class JsonResult {
	private static final JsonResult SUCESS = s(null);
	private String no;
	private String message;
	private String redirectTo;
	private Object data;
	private boolean error;

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

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

	public static JsonResult s(Object v) {
		JsonResult r = new JsonResult();
		r.error = false;
		r.data = v;
		return r;
	}

	public static JsonResult create(String string, Object v) {
		JsonResult r = s(v);
		r.message = string;
		return r;
	}

	public static JsonResult s() {
		return SUCESS;
	}

	public static JsonResult e() {
		JsonResult r = new JsonResult();
		r.error = true;
		return r;
	}

	public static JsonResult e(String msg) {
		JsonResult r = e();
		r.message = msg;
		return r;
	}

	public static <T> List<Map<String, Object>> l(List<T> cs,
			YuiResultMapper<T> mapping) {
		List<Map<String, Object>> rs = WxlSugar.newArrayList(cs.size());
		for (T t : cs) {
			rs.add(mapping.toMap(t));
		}
		return rs;
	}

	public static JsonResult e(String msg, Object p) {
		JsonResult r = e(msg);
		r.data = p;
		return r;
	}

	public static <T> JsonResult sl(List<T> cs, YuiResultMapper<T> mapping) {
		List<Map<String, Object>> rs = WxlSugar.newArrayList(cs.size());
		for (T t : cs) {
			rs.add(mapping.toMap(t));
		}
		return JsonResult.s(rs);
	}

	public JsonResult withNo(String no) {
		this.no = no;
		return this;
	}
}
