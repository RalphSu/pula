package puerta.support.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.lang.StringUtils;


public class TextHelper {
	public static List<String[]> getText(Reader r) {
		List<String[]> texts = WxlSugar.newArrayList();
		List<String> inner = WxlSugar.newArrayList();
		try {
			BufferedReader reader = new BufferedReader(r);
			int i = 0;
			String line = reader.readLine();
			while (line != null) { // 如果 line 为空说明读完了

				String t = StringUtils.trim(line);
				if (StringUtils.isEmpty(t)) {
					line = reader.readLine();
					continue;
				}
				inner.add(t);
				line = reader.readLine();
				i++;
				if (i >= 1000) {
					i = 0;
					texts.add(inner.toArray(new String[i]));
					inner.clear();
				}
			}

			if (inner.size() > 0) {
				texts.add(inner.toArray(new String[i]));
				inner.clear();
			}

		} catch (IOException e) {

			e.printStackTrace();
		}

		return texts;
	}
}
