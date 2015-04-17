/**
 * Created on 2008-3-21 上午11:05:22
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @author tiyi
 * 
 */
public class GenericsUtils {
	public static <T, E> Class<T> getGeneric(Class<E> clazz) {
		return getGeneric(clazz, 1);
	}

	@SuppressWarnings("unchecked")
	public static <T, E> Class<T> getGeneric(Class<E> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (genType instanceof ParameterizedType) {
			Type[] params = ((ParameterizedType) genType)
					.getActualTypeArguments();

			if ((params != null) && (params.length >= index)) {
				return (Class<T>) params[index - 1];
			}
		}
		return null;
	}
}
