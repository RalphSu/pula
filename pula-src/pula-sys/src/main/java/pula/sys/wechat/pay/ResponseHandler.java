package pula.sys.wechat.pay;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pula.sys.vo.WechatNotifyEntity;
import pula.sys.wechat.pay.util.MD5Util;
import pula.sys.wechat.pay.util.TenpayUtil;

/**
 * Ӧ������
 * Ӧ������̳д��࣬��дisTenpaySign�������ɡ�
 * @author miklchen
 *
 */
public class ResponseHandler { 
	private static final Logger logger= LoggerFactory.getLogger(RequestHandler.class);
	
	/** ��Կ */
	private String key;
	
	/** Ӧ��Ĳ��� */
	private SortedMap parameters; 
	
	/** debug��Ϣ */
	private String debugInfo;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private String uriEncoding;
	
	/**
	 * ���캯��
	 * @param requestBodyXml 
	 * 
	 * @param request
	 * @param response
	 */
	public ResponseHandler(String requestBodyXml, HttpServletRequest request,
			HttpServletResponse response)  {
		this.request = request;
		this.response = response;

		this.key = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";
		
		this.uriEncoding = "";

		Map m = this.request.getParameterMap();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String k = (String) it.next();
			String v = ((String[]) m.get(k))[0];			
			this.setParameter(k, v);
		}

		// load parameter from the request body xml
		{
			try {
				JAXBContext jc = JAXBContext
						.newInstance(WechatNotifyEntity.class);
				Unmarshaller unmarshaller = jc.createUnmarshaller();
				WechatNotifyEntity sc = (WechatNotifyEntity) unmarshaller
						.unmarshal(new ByteArrayInputStream(requestBodyXml
								.getBytes(Charset.forName("utf8"))));
				if (sc != null) {
					sc.toParameterMap(this.parameters);
				}
			} catch (Exception e) {
				logger.error("wechat notify xml parse error!", e);
			}
		}

	}
	
	/**
	*��ȡ��Կ
	*/
	public String getKey() {
		return key;
	}

	/**
	*������Կ
	*/
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * ��ȡ����ֵ
	 * @param parameter ��������
	 * @return String 
	 */
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	
	/**
	 * ���ò���ֵ
	 * @param parameter ��������
	 * @param parameterValue ����ֵ
	 */
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	/**
	 * �������еĲ���
	 * @return SortedMap
	 */
	public SortedMap getAllParameters() {
		return this.parameters;
	}
	
	/**
	 * �Ƿ�Ƹ�ͨǩ��,������:����������a-z����,������ֵ�Ĳ������μ�ǩ����
	 * @return boolean
	 */
	public boolean isTenpaySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			if(!"sign".equals(k) && entry.getValue() != null && !"".equals(entry.getValue().toString())) {
				String v = entry.getValue().toString();
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		
		//���ժҪ
		String enc = TenpayUtil.getCharacterEncoding(this.request, this.response);
		String sign = MD5Util.MD5Encode(sb.toString(), enc).toLowerCase();
		
		String tenpaySign = this.getParameter("sign").toLowerCase();
		
		//debug��Ϣ
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		logger.info(debugInfo);
		
		return tenpaySign.equals(sign);
	}
	
	/**
	 * ���ش��������Ƹ�ͨ��������
	 * @param msg: Success or fail��
	 * @throws IOException 
	 */
	public void sendToCFT(String msg) throws IOException {
		String strHtml = msg;
		PrintWriter out = this.getHttpServletResponse().getWriter();
		out.println(strHtml);
		out.flush();
		out.close();

	}
	
	/**
	 * ��ȡuri����
	 * @return String
	 */
	public String getUriEncoding() {
		return uriEncoding;
	}

	/**
	 * ����uri����
	 * @param uriEncoding
	 * @throws UnsupportedEncodingException
	 */
	public void setUriEncoding(String uriEncoding)
			throws UnsupportedEncodingException {
		if (!"".equals(uriEncoding.trim())) {
			this.uriEncoding = uriEncoding;

			// ����ת��
			String enc = TenpayUtil.getCharacterEncoding(request, response);
			Iterator it = this.parameters.keySet().iterator();
			while (it.hasNext()) {
				String k = (String) it.next();
				String v = this.getParameter(k);
				v = new String(v.getBytes(uriEncoding.trim()), enc);
				this.setParameter(k, v);
			}
		}
	}

	/**
	*��ȡdebug��Ϣ
	*/
	public String getDebugInfo() {
		return debugInfo;
	}
	
	/**
	*����debug��Ϣ
	*/
	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	protected HttpServletRequest getHttpServletRequest() {
		return this.request;
	}
	
	protected HttpServletResponse getHttpServletResponse() {
		return this.response;
	}
	
}
