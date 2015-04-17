package puerta.system.keeper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import puerta.system.vo.AppFieldData;

@Service
public class AppFieldKeeper {

	private Map<String, AppFieldData> pathMap;

	private Map<String, Pattern> pathPatternMap;

	// public void reload() {
	// pathMap = appFieldDao.loadPathMap();
	// pathPatternMap = new HashMap<String, Pattern>();
	// for (String key : pathMap.keySet()) {
	// pathPatternMap.put(key, Pattern.compile("^" + key + ".*"));
	//
	// }
	// }

	public void register(String key, String path) {
		if (pathMap == null) {
			pathMap = new LinkedHashMap<String, AppFieldData>();
		}
		pathMap.put(path, AppFieldData.create(key, path));
		if (pathPatternMap == null) {
			pathPatternMap = new LinkedHashMap<String, Pattern>();
		}
		pathPatternMap.put(path, Pattern.compile("^" + path + ".*"));
	}

	public AppFieldData getAppFieldData(String path) {
		for (String key : pathPatternMap.keySet()) {
			Pattern pattern = pathPatternMap.get(key);
			Matcher matcher = pattern.matcher(path);
			boolean matchFound = matcher.find();
			if (matchFound) {
				return pathMap.get(key);
			}
		}
		return null;
	}
}
