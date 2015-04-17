package pula.web.controllers.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.JSONException;

public class HttpClient {

	private static final int OK = 200; // OK: Success!
	private static final int NOT_MODIFIED = 304; // Not Modified: There was no
													// new data to return.
	private static final int BAD_REQUEST = 400; // Bad Request: The request was
												// invalid. An accompanying
												// error message will explain
												// why. This is the status code
												// will be returned during rate
												// limiting.
	private static final int NOT_AUTHORIZED = 401; // Not Authorized:
													// Authentication
													// credentials were missing
													// or incorrect.
	private static final int FORBIDDEN = 403; // Forbidden: The request is
												// understood, but it has been
												// refused. An accompanying
												// error message will explain
												// why.
	private static final int NOT_FOUND = 404; // Not Found: The URI requested is
												// invalid or the resource
												// requested, such as a user,
												// does not exists.
	private static final int NOT_ACCEPTABLE = 406; // Not Acceptable: Returned
													// by the Search API when an
													// invalid format is
													// specified in the request.
	private static final int INTERNAL_SERVER_ERROR = 500;// Internal Server
															// Error: Something
															// is broken. Please
															// post to the group
															// so the Weibo team
															// can investigate.
	private static final int BAD_GATEWAY = 502;// Bad Gateway: Weibo is down or
												// being upgraded.
	private static final int SERVICE_UNAVAILABLE = 503;// Service Unavailable:
														// The Weibo servers are
														// up, but overloaded
														// with requests. Try
														// again later. The
														// search and trend
														// methods use this to
														// indicate when you are
														// being rate limited.

	private final static boolean DEBUG = true;
	static Logger log = Logger.getLogger(HttpClient.class.getName());

	org.apache.commons.httpclient.HttpClient client = null;
	private MultiThreadedHttpConnectionManager connectionManager;
	private int maxSize;

	public HttpClient() {
		this(150, 30000, 30000, 1024 * 1024);
	}

	public HttpClient(int maxConPerHost, int conTimeOutMs, int soTimeOutMs,
			int maxSize) {
		connectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = connectionManager.getParams();
		params.setDefaultMaxConnectionsPerHost(maxConPerHost);
		params.setConnectionTimeout(conTimeOutMs);
		params.setSoTimeout(soTimeOutMs);

		HttpClientParams clientParams = new HttpClientParams();
		// 忽略cookie 避免 Cookie rejected 警告
		clientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		client = new org.apache.commons.httpclient.HttpClient(clientParams,
				connectionManager);
		// Protocol myhttps = new Protocol("https", new MySSLSocketFactory(),
		// 443);
		// Protocol.registerProtocol("https", myhttps);
		this.maxSize = maxSize;
		// 支持proxy
		// if (proxyHost != null && !proxyHost.equals("")) {
		// client.getHostConfiguration().setProxy(proxyHost, proxyPort);
		// client.getParams().setAuthenticationPreemptive(true);
		// if (proxyAuthUser != null && !proxyAuthUser.equals("")) {
		// client.getState().setProxyCredentials(
		// AuthScope.ANY,
		// new UsernamePasswordCredentials(proxyAuthUser,
		// proxyAuthPassword));
		// log("Proxy AuthUser: " + proxyAuthUser);
		// log("Proxy AuthPassword: " + proxyAuthPassword);
		// }
		// }
	}

	public Response post(String url, PostParameter... params) throws Exception {
		return post(url, params, true);

	}

	public Response post(String url, PostParameter[] params,
			Boolean WithTokenHeader) throws Exception {
		log("Request:");
		log("POST" + url);
		PostMethod postMethod = new PostMethod(url);
		for (int i = 0; i < params.length; i++) {
			postMethod.addParameter(params[i].getName(), params[i].getValue());
		}
		HttpMethodParams param = postMethod.getParams();
		param.setContentCharset("UTF-8");
		if (WithTokenHeader) {
			return httpRequest(postMethod);
		} else {
			return httpRequest(postMethod, WithTokenHeader);
		}
	}

	public Response httpRequest(HttpMethod method) throws HttpException {
		return httpRequest(method, true);
	}

	public Response httpRequest(HttpMethod method, Boolean WithTokenHeader)
			throws HttpException {
		// InetAddress ipaddr;
		int responseCode = -1;
		try {
			// ipaddr = InetAddress.getLocalHost();
			// List<Header> headers = new ArrayList<Header>();
			// if (WithTokenHeader) {
			// if (token == null) {
			// throw new IllegalStateException("Oauth2 token is not set!");
			// }
			// headers.add(new Header("Authorization", "OAuth2 " + token));
			// headers.add(new Header("API-RemoteIP", ipaddr.getHostAddress()));
			// client.getHostConfiguration().getParams()
			// .setParameter("http.default-headers", headers);
			// for (Header hd : headers) {
			// log(hd.getName() + ": " + hd.getValue());
			// }
			// }

			// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
			// new DefaultHttpMethodRetryHandler(3, false));
			client.executeMethod(method);
			Header[] resHeader = method.getResponseHeaders();
			responseCode = method.getStatusCode();
			log("Response:");
			log("https StatusCode:" + String.valueOf(responseCode));

			for (Header header : resHeader) {
				log(header.getName() + ":" + header.getValue());
			}
			Response response = new Response();
			response.setResponseAsString(readString(method));
			log(response.toString() + "\n");

			if (responseCode != OK)

			{
				try {
					throw new HttpException(getCause(responseCode),
							response.asJSONObject(), method.getStatusCode());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return response;

		} catch (IOException ioe) {
			throw new HttpException(ioe.getMessage(), ioe, responseCode);
		} finally {
			method.releaseConnection();
		}

	}

	private String readString(HttpMethod method) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				method.getResponseBodyAsStream()));
		StringBuffer stringBuffer = new StringBuffer();
		String str = "";
		while ((str = reader.readLine()) != null) {
			stringBuffer.append(str);
		}
		String ts = stringBuffer.toString();
		return ts;
	}

	/**
	 * log调试
	 * 
	 */
	private static void log(String message) {
		if (DEBUG) {
			log.debug(message);
		}
	}

	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
		case NOT_MODIFIED:
			break;
		case BAD_REQUEST:
			cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
			break;
		case NOT_AUTHORIZED:
			cause = "Authentication credentials were missing or incorrect.";
			break;
		case FORBIDDEN:
			cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
			break;
		case NOT_FOUND:
			cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
			break;
		case NOT_ACCEPTABLE:
			cause = "Returned by the Search API when an invalid format is specified in the request.";
			break;
		case INTERNAL_SERVER_ERROR:
			cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
			break;
		case BAD_GATEWAY:
			cause = "Weibo is down or being upgraded.";
			break;
		case SERVICE_UNAVAILABLE:
			cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
			break;
		default:
			cause = "";
		}
		return statusCode + ":" + cause;
	}

	/*
	 * 对parameters进行encode处理
	 */
	public static String encodeParameters(PostParameter[] postParams) {
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < postParams.length; j++) {
			if (j != 0) {
				buf.append("&");
			}
			try {
				buf.append(URLEncoder.encode(postParams[j].getName(), "UTF-8"))
						.append("=")
						.append(URLEncoder.encode(postParams[j].getValue(),
								"UTF-8"));
			} catch (java.io.UnsupportedEncodingException neverHappen) {
			}
		}
		return buf.toString();
	}

	/**
	 * 处理http getmethod 请求
	 * 
	 */

	public Response get(String url) throws HttpException {

		return get(url, new PostParameter[0]);

	}

	public Response get(String url, PostParameter[] params)
			throws HttpException {
		log("Request:");
		log("GET:" + url);
		if (null != params && params.length > 0) {
			String encodedParams = HttpClient.encodeParameters(params);
			if (-1 == url.indexOf("?")) {
				url += "?" + encodedParams;
			} else {
				url += "&" + encodedParams;
			}
		}
		GetMethod getmethod = new GetMethod(url);
		return httpRequest(getmethod);

	}

	// public Response get(String url, PostParameter[] params, Paging paging)
	// throws HttpException {
	// if (null != paging) {
	// List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
	// if (-1 != paging.getMaxId()) {
	// pagingParams.add(new PostParameter("max_id", String
	// .valueOf(paging.getMaxId())));
	// }
	// if (-1 != paging.getSinceId()) {
	// pagingParams.add(new PostParameter("since_id", String
	// .valueOf(paging.getSinceId())));
	// }
	// if (-1 != paging.getPage()) {
	// pagingParams.add(new PostParameter("page", String
	// .valueOf(paging.getPage())));
	// }
	// if (-1 != paging.getCount()) {
	// if (-1 != url.indexOf("search")) {
	// // search api takes "rpp"
	// pagingParams.add(new PostParameter("rpp", String
	// .valueOf(paging.getCount())));
	// } else {
	// pagingParams.add(new PostParameter("count", String
	// .valueOf(paging.getCount())));
	// }
	// }
	// PostParameter[] newparams = null;
	// PostParameter[] arrayPagingParams = pagingParams
	// .toArray(new PostParameter[pagingParams.size()]);
	// if (null != params) {
	// newparams = new PostParameter[params.length
	// + pagingParams.size()];
	// System.arraycopy(params, 0, newparams, 0, params.length);
	// System.arraycopy(arrayPagingParams, 0, newparams,
	// params.length, pagingParams.size());
	// } else {
	// if (0 != arrayPagingParams.length) {
	// String encodedParams = HttpClient
	// .encodeParameters(arrayPagingParams);
	// if (-1 != url.indexOf("?")) {
	// url += "&" + encodedParams;
	// } else {
	// url += "?" + encodedParams;
	// }
	// }
	// }
	// return get(url, newparams);
	// } else {
	// return get(url, params);
	// }
	// }
}
