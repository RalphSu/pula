package puerta.system.vo;

import java.util.List;

public class UrlsVo {

	private List<RequestUriVo> optional;
	private List<RequestUriVo> assigned;

	public List<RequestUriVo> getOptional() {
		return optional;
	}

	public void setOptional(List<RequestUriVo> optional) {
		this.optional = optional;
	}

	public List<RequestUriVo> getAssigned() {
		return assigned;
	}

	public void setAssigned(List<RequestUriVo> assigned) {
		this.assigned = assigned;
	}

	public static UrlsVo create(List<RequestUriVo> allurls,
			List<RequestUriVo> ass) {
		UrlsVo v = new UrlsVo();
		v.optional = allurls;
		v.assigned = ass;
		return v;
	}

}
