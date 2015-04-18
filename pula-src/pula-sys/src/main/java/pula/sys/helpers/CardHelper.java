package pula.sys.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;

import puerta.support.Pe;
import puerta.support.utils.ExcelHelper;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOption;
import puerta.support.vo.SelectOptionList;
import pula.sys.domains.Card;

public class CardHelper {

	public static List<Card> read(InputStream inputStream)
			throws BiffException, IOException {
		Workbook workbook = Workbook.getWorkbook(inputStream);

		if (workbook.getSheets().length < 1) {
			Pe.raise("工作表数量少于1");
		}

		Sheet sheet = workbook.getSheet(0);

		// int index = 0;
		List<Card> results = WxlSugar.newArrayList();
		// PartForm singleMoldBase = null;
		for (int j = 1; j < sheet.getRows(); j++) {

			String value = ExcelHelper.getValue(sheet, 'A', j);

			if (StringUtils.isEmpty(value)) {
				break;
			}

			// 开始读取
			Card pf = new Card();
			String no = ExcelHelper.getValue(sheet, 'A', j);
			String mac = ExcelHelper.getValue(sheet, 'B', j);

			pf.setNo(no);
			// pf.setGraphNo(code);
			pf.setMac(mac);
			pf.setStatus(Card.STATUS_FREE);

			results.add(pf);

		}

		return results;
	}

	public static int[] status = new int[] { Card.STATUS_FREE, Card.STATUS_USED };

	public static String[] status_names = new String[] { "空闲", "使用" };

	public static SelectOptionList getStatusList(int n) {
		return SelectOption.getList(n, status, status_names);
	}

	public static String getStatusName(int n) {
		return SelectOption.getName(n, status, status_names);
	}

}
