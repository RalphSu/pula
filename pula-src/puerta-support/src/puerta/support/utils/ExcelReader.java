package puerta.support.utils;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelReader {

	Workbook book;
	Sheet sheet;

	public static ExcelReader create(InputStream is) throws BiffException,
			IOException {
		ExcelReader er = new ExcelReader();
		er.book = Workbook.getWorkbook(is);
		return er;
	}
	
	
}
