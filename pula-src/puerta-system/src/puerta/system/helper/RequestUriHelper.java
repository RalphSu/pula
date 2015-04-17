package puerta.system.helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.springframework.transaction.annotation.Transactional;

import puerta.support.annotation.Barrier;
import puerta.support.annotation.BarrierEx;
import puerta.support.annotation.BarrierExClz;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.NoNameVo;
import puerta.system.po.RequestUri;
import puerta.system.vo.RequestUriVo;
import puerta.system.vo.UrlsVo;

public class RequestUriHelper {
	private static final String CONTROLLER = "Controller";

	public static String getControllerPath(Class<?> cls) {
		String sn = cls.getSimpleName();
		String pn = cls.getName();
		int en = StringUtils.lastIndexOf(pn, ".");
		int bn = StringUtils.lastIndexOf(pn, ".", en - 1);
		String path = "/" + pn.substring(bn + 1, en);

		if (sn.endsWith(CONTROLLER)) {
			sn = sn.substring(0, sn.length() - CONTROLLER.length())
					.toLowerCase();
		}
		return path += "/" + sn.toLowerCase();
	}

	public static UrlsVo mix(List<RequestUri> all, List<RequestUri> assigned) {
		Map<String, RequestUriVo> allurls = WxlSugar.newLinkedHashMap();
		List<RequestUriVo> ass = WxlSugar.newArrayList();
		for (RequestUri ri : all) {
			allurls.put(ri.getUri(), RequestUriVo.create(ri));
		}
		for (RequestUri ri : assigned) {
			ass.add(RequestUriVo.create(ri));
			allurls.remove(ri.getUri());
		}

		return UrlsVo
				.create(new ArrayList<RequestUriVo>(allurls.values()), ass);
	}

	public static List<String> getPackageNames(String fp) {
		List<String> packages = WxlSugar.newArrayList(5);
		InputStream is = null;
		SAXBuilder sb = new SAXBuilder();
		Namespace NS = Namespace.getNamespace("context",
				"http://www.springframework.org/schema/context");

		try {
			is = new FileInputStream(fp);
			Document doc = sb.build(is);
			org.jdom.Element rootElement = doc.getRootElement();

			@SuppressWarnings("unchecked")
			List<Element> scans = rootElement.getChildren("component-scan", NS);
			for (Element scan : scans) {
				packages.add(scan.getAttributeValue("base-package"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}

		return packages;
	}

	public static List<NoNameVo> checkMethod(String controllerPath, Method m,
			Class<?> cls) {

		List<NoNameVo> results = WxlSugar.newArrayList();

		// 1.是否标记了事务
		boolean trans = m.isAnnotationPresent(Transactional.class);

		if (!trans) {
			results.add(NoNameVo.create("1", "NO_TRANS", cls.getSimpleName()
					+ "." + m.getName() + " 未配置事务"));
		} else {

			// 2.事务类型是否合理 ,不过只能说view,get,list 这类开头的都应该是只读的

			Transactional ts = m.getAnnotation(Transactional.class);

			if (!ts.readOnly()
					&& (m.getName().startsWith("view")
							|| m.getName().startsWith("list")
							|| m.getName().startsWith("get") || m.getName()
							.startsWith("find"))) {

				results.add(NoNameVo.create("2", "TRANS_READ_ONLY",
						cls.getSimpleName() + "." + m.getName() + " 应该配置只读事务"));
			}
		}

		if (cls.isAnnotationPresent(Barrier.class)) {
			// 通常表示整个类都忽略了
		} else {

			// 3.是否标记了权限
			boolean hasBarrie = m.isAnnotationPresent(Barrier.class)
					|| m.isAnnotationPresent(BarrierEx.class);

			if (!hasBarrie) {
				results.add(NoNameVo.create("3", "NO_BARRIER",
						cls.getSimpleName() + "." + m.getName() + " 未配置权限"));
			}

			if (m.isAnnotationPresent(Barrier.class)) {
				Barrier barr = m.getAnnotation(Barrier.class);
				if (barr.ignore() || !barr.check()) {

				} else {
					String[] value = barr.value();
					checkDomain(cls, m, results, value);
				}
			} else if (m.isAnnotationPresent(BarrierEx.class)) {
				BarrierEx barr = m.getAnnotation(BarrierEx.class);
				BarrierExClz[] clzss = barr.value();

				for (BarrierExClz cl : clzss) {
					checkDomain(cls, m, results, cl.value());
				}
			}
		}

		return results;

	}

	private static String extractDomain(String simpleName) {
		int n = StringUtils.indexOf(simpleName, "Controller");
		String left = StringUtils.left(simpleName, n);
		return left;
	}

	private static void checkDomain(Class<?> cls, Method m,
			List<NoNameVo> results, String[] value) {
		String domainName = extractDomain(cls.getSimpleName());
		if (value == null) {

		} else {
			for (String no : value) {
				if (StringUtils.isEmpty(no)) {
					continue;
				}
				String n = StringUtils.replace(no.toLowerCase(), "P_", "");
				n = StringUtils.replace(n.toLowerCase(), "_", "");

				if (!StringUtils.contains(n, domainName.toLowerCase())) {

					results.add(NoNameVo.create("4", "BARRIER_NOT_MATCH",
							cls.getSimpleName() + "." + m.getName() + " 权限代码:"
									+ no + "和领域对象名称不匹配:" + domainName));
				}
			}
		}
	}
}
