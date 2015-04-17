package puerta.system.vo;

import java.util.Map;

public interface YuiResultMapper<T> {

	Map<String, Object> toMap(T obj);
}
