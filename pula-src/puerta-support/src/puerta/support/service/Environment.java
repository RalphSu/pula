package puerta.support.service;

import org.apache.commons.lang.StringUtils;

public class Environment {

	private String appFieldNo;
	private String ip;
	private String lang, backURL;
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getBackURL() {
		return backURL;
	}

	public void setBackURL(String backURL) {
		this.backURL = backURL;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppFieldNo() {
		return appFieldNo;
	}

	public void setAppFieldNo(String appFieldNo) {
		this.appFieldNo = appFieldNo;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public static Environment create(String afNo, String ip, String lang,
			String backURL, String uri) {
		Environment b = new Environment();
		b.appFieldNo = afNo;
		b.ip = ip;
		b.lang = lang;

		if (!StringUtils.isEmpty(backURL)) {
			b.backURL = backURL;
		}
		b.uri = uri;
		return b;
	}
}
