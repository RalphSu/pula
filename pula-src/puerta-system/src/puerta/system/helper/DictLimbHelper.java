package puerta.system.helper;

import java.util.Map;

import puerta.support.utils.WxlSugar;
import puerta.system.po.DictLimb;
import puerta.system.vo.YuiResultMapper;

public class DictLimbHelper {
	public static final YuiResultMapper<DictLimb> MAPPING = new YuiResultMapper<DictLimb>() {
		@Override
		public Map<String, Object> toMap(DictLimb obj) {

			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("value", obj.getNo());
			m.put("text", obj.getName());
			return m;
		}
	};
}
