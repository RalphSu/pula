package pula.sys.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import puerta.support.Pe;
import puerta.support.utils.ExcelHelper;
import puerta.support.utils.WxlSugar;
import pula.sys.forms.GiftForm;

public class GiftHelper {

	// 02.19.05.0011

	public static boolean isGiftNo(String no) {
		String s = StringUtils.trim(no);
		if (s.length() != 13) {
			return false;
		}
		return true;
	}

	public static String getTypeNo(String no) {
		if (no.length() >= 2) {
			return no.substring(0, 2);
		}
		return null;
	}

	public static List<GiftForm> read(InputStream inputStream)
			throws IOException, BiffException {

		Workbook workbook = Workbook.getWorkbook(inputStream);

		if (workbook.getSheets().length < 1) {
			Pe.raise("工作表数量少于1");
		}

		Sheet sheet = workbook.getSheet(0);

		// int index = 0;
		List<GiftForm> results = WxlSugar.newArrayList();
		// PartForm singleMoldBase = null;
		for (int j = 1; j < sheet.getRows(); j++) {

			String value = ExcelHelper.getValue(sheet, 'A', j);

			if (StringUtils.isEmpty(value)) {
				break;
			}

			// 开始读取
			GiftForm pf = new GiftForm();
			String no = ExcelHelper.getValue(sheet, 'A', j);
			String code = ExcelHelper.getValue(sheet, 'B', j);
			String name = ExcelHelper.getValue(sheet, 'C', j);
			String py = ExcelHelper.getValue(sheet, 'D', j);
			String raw = ExcelHelper.getValue(sheet, 'E', j);
			String spec = ExcelHelper.getValue(sheet, 'F', j);
			String comments = ExcelHelper.getValue(sheet, 'G', j);
			double weight = NumberUtils.toDouble(ExcelHelper.getValue(sheet,
					'H', j));

			double superficialArea = NumberUtils.toDouble(ExcelHelper.getValue(
					sheet, 'I', j));

			int maxStock = ExcelHelper.getInt(sheet, 'J', j);
			int minStock = ExcelHelper.getInt(sheet, 'K', j);

			pf.setNo(no);
			// pf.setGraphNo(code);
			pf.setName(name);
			pf.setPinyin(py);
			pf.setRaw(raw);
			// pf.setSpec(spec);
			// pf.setStockMax(maxStock);
			// pf.setStockMin(minStock);
			pf.setSuperficialArea(superficialArea);
			pf.setWeight(weight);
			pf.setComments(comments);

			results.add(pf);

		}

		// if (singleMoldBase == null) {
		// Pe.raise("Excel中未找到模架信息:MOLD_BASE");
		// }

		return results;

	}

	public static List<GiftForm> read(String string) throws BiffException,
			FileNotFoundException, IOException {
		return read(new FileInputStream(new File(string)));
	}

	public static String buildRefId(long id) {
		return "G:" + id;
	}

}
