package puerta.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import puerta.support.annotation.ObjectParam;

/**
 * 現在還不支持原生類型,int,string ... 只支持bean
 * 
 * @author tiyi
 * 
 */
public class PuertaObjectResolver implements WebArgumentResolver {

	private final static Log log = LogFactory
			.getLog(PuertaObjectResolver.class);

	public PuertaObjectResolver() {
		// System.out.println("PuertaObjectResolver==");
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		Annotation[] paramAnns = methodParameter.getParameterAnnotations();
		// String[] keys =
		// webRequest.getParameterValues("condition.appFieldNo");
		String paramName;
		for (Annotation paramAnn : paramAnns) {
			if (ObjectParam.class.isInstance(paramAnn)) {

				Class<?> cls = methodParameter.getParameterType();
				ObjectParam requestParam = (ObjectParam) paramAnn;
				paramName = requestParam.value();
				return takeObjectFromRequest(paramName,
						webRequest.getParameterMap(), cls);
			}
		}

		return UNRESOLVED;
	}

	public static <T> T takeArrayObjectFromRequest(String paramName,
			Map<String, String[]> map, Class<T> clazz) {
		int minLen = getLength(map, paramName);
		if (minLen <= 0) {
			return null;
		}

		@SuppressWarnings("unchecked")
		T resultsArrays = (T) Array.newInstance(clazz.getComponentType(),
				minLen);

		for (int i = 0; i < minLen; i++) {

			Object obj = takeObjectFromRequest(paramName, map,
					clazz.getComponentType(), i);
			Array.set(resultsArrays, i, obj);
		}

		return resultsArrays;

	}

	private static int getLength(Map<String, String[]> map, String paramName) {
		int min = Integer.MAX_VALUE;
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(paramName)) {
				min = Math.min(entry.getValue().length, min);
			}
		}

		if (min == Integer.MAX_VALUE) {
			min = -1;
		}

		return min;
	}

	private static Object takeObjectFromRequest(String paramName,
			Map<String, String[]> map, Class<?> clazz, int index) {
		Object obj = null;

		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		boolean apply = false;
		if (obj == null) {
			return obj;
		}

		BeanWrapperImpl bw = new BeanWrapperImpl(obj);
		bw.setAutoGrowNestedPaths(true);
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(paramName)) {
				String target = key.substring(paramName.length());
				if (target.length() == 0) {
					// 直接返回值（还需要根据类型匹配一下）
					return obj;
				} else {
					target = target.substring(1);
					bw.setPropertyValue(target, entry.getValue()[index]);
					apply = true;
				}
			}
		}
		if (apply)
			return obj;
		return null;
	}

	public static <T> T takeObjectFromRequest(String paramName,
			Map<String, String[]> map, Class<T> clazz) {

		if (clazz.isArray()) {
			return takeArrayObjectFromRequest(paramName, map, clazz);
		}

		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (Exception e) {
			log.error("paramName=" + paramName);
			e.printStackTrace();
		}
		boolean apply = false;
		if (obj == null) {
			return obj;
		}

		BeanWrapperImpl bw = new BeanWrapperImpl(obj);
		bw.setAutoGrowNestedPaths(true);
		for (Entry<String, String[]> entry : map.entrySet()) {
			String key = entry.getKey();
			if (key.startsWith(paramName)) {
				String target = key.substring(paramName.length());
				if (target.length() == 0) {
					// 直接返回值（还需要根据类型匹配一下）
					return obj;
				} else {
					target = target.substring(1);
					try {
						bw.setPropertyValue(target, entry.getValue());
					} catch (Exception ex) {
						log.warn(ex);
						log.warn("target=" + target);
					}
					apply = true;
				}
			}
		}
		if (apply)
			return obj;
		return null;
	}
}
