/**
 * Created on 2007-12-2 10:18:39
 *
 * DiagCN.COM 2004-2006
 * $Id$
 */
package puerta.system.keeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import puerta.system.po.Parameter;

/**
 * 本地参数读取
 * 
 * @author tiyi
 * 
 */
@Service
public class LocalParameter {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(LocalParameter.class);

	/**
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Parameter> readLocal() throws IOException {

		Map<String, Parameter> map = new LinkedHashMap<String, Parameter>();
		DefaultResourceLoader rs = new DefaultResourceLoader();
		logger.debug("local="
				+ rs.getResource("/wxl-config.xml").getFile().getAbsolutePath());
		InputStream is = rs.getResource("/wxl-config.xml").getInputStream();
		SAXBuilder sb = new SAXBuilder();
		Document doc;
		try {
			doc = sb.build(is);
			Element rootElement = doc.getRootElement(); // nilnut-config
			List<Element> parameters = rootElement.getChildren("parameter");
			for (Element ele : parameters) {
				String no = ele.getAttributeValue("no");
				String name = ele.getAttributeValue("name");
				String value = ele.getValue();
				Parameter p = new Parameter();
				p.setNo(no);
				p.setName(name);
				p.setValue(value);
				logger.debug(no + "=" + value);
				map.put(no, p);
			}
		} catch (Exception e) {
			logger.warn(e);
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return map;
	}

}
