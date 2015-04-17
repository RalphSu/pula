/**
 * Created on 2008-3-21 上午11:05:22
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @author tiyi
 * 
 */
public class GenericsUtils {
	public static Class<?> getGeneric(Class<?> clazz) {
		return getGeneric(clazz, 1);
	}

	public static Class<?> getGeneric(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length >= index)) {
				return (Class<?>) params[index - 1];
			}
		}
		return null;
	}
}
