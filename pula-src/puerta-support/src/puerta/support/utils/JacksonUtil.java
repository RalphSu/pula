package puerta.support.utils;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import puerta.support.mls.Mls;

public class JacksonUtil {
	/**  
     *   
     */
	private JacksonUtil() {
	}

	/**   
     *    
     */
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @return
	 */
	public static ObjectMapper getInstance() {
		return mapper;
	}

	public static <T> T get(String text, Class<T> target) {
		try {
			return getInstance().readValue(text, target);
		} catch (Exception e) {
			e.printStackTrace();
			Mls.raise("JsonFormatError");
			return null;
		}
	}

	public static <T> List<T> getList(String text, Class<T> target) {
		try {
			return getInstance().readValue(text,
					TypeFactory.collectionType(ArrayList.class, target));
		} catch (Exception e) {
			e.printStackTrace();
			Mls.raise("JsonFormatError");
			return null;
		}
	}

	public static String toString(Object obj) {
		try {
			return getInstance().writeValueAsString(obj);
		} catch (Exception e) {
			Mls.raise("JsonFormatError");
			e.printStackTrace();
		}
		return null;
	}

}