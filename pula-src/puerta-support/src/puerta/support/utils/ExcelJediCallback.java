package puerta.support.utils;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

public interface ExcelJediCallback {

	void process(WritableWorkbook workbook, Workbook tplWorkbook)
			throws JXLException;
}
