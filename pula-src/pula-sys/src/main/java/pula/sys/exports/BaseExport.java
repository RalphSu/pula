package pula.sys.exports;

import puerta.support.utils.XlsColumnSetting;

public abstract class BaseExport {
	static XlsColumnSetting col(String string, int i) {
		return XlsColumnSetting.create(string, i);
	}
	
	
}
