package pula.sys.exports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import jxl.Workbook;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import puerta.support.AttachmentFile;
import puerta.support.Pe;
import puerta.support.utils.DateExTool;
import puerta.support.utils.ExcelHelper;
import puerta.support.utils.RandomTool;
import puerta.support.utils.XlsColumnSetting;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import pula.sys.helpers.StockLogHelper;

public class StockLogExport extends BaseExport {
	private static final Logger log = Logger.getLogger(StockLogExport.class);
	private static final XlsColumnSetting[] FULL_COLUMNS = new XlsColumnSetting[] {
			col("日期", 10), col("物料编号", 20), col("类型", 5), col("物料名称", 40),
			col("仓库", 10), col("区域", 10), col("数量", 10), col("单据类型", 10),
			col("内单号", 15), col("外单号", 15)

	};

	public static AttachmentFile export(String path, MapList results) {
		log.debug("results.size=" + results.size());

		File f = makeFullFile(path, results);
		if (f == null) {
			Pe.raise("数据写入错误");
		}
		AttachmentFile af = AttachmentFile.attach(f,
				AttachmentFile.APPLICATION_XLS);
		af.setFileName(DateExTool.formatDateTime(Calendar.getInstance(),
				"yyyy-MM-dd_HH_mm") + ".xls");

		return af;
	}

	private static File makeFullFile(String path, MapList results) {
		String filePath = path + File.separator
				+ RandomTool.getRandomString(10);
		WritableWorkbook wwb = null;// 创建Excel工作簿
		OutputStream os = null;
		try {
			os = new FileOutputStream(filePath);
			wwb = Workbook.createWorkbook(os);

			// 添加第一个工作表并设置第一个sheet的名字,0是工作表的索引
			WritableSheet sheet = wwb.createSheet("sheet1", 0);
			ExcelHelper.initColumns(sheet, FULL_COLUMNS);
			// 写数据
			int row = 1;

			for (MapBean r : results) {
				WritableCellFormat wcf_normal = new WritableCellFormat();
				wcf_normal.setVerticalAlignment(VerticalAlignment.JUSTIFY);
				// wcf.setAlignment(Alignment.CENTRE);
				ExcelHelper
						.writeRowWithFormat(sheet, 0, row, wcf_normal,
								DateExTool.date2String((Calendar) r
										.get("eventTime")),

								r.string("no"), StockLogHelper.getTypeName(r
										.integer("type")), r.string("name"), r
										.string("warehouseName"), r
										.string("spaceName"), r
										.integer("quantity"), getOrderType(r),
								r.string("orderNo"), r.string("outNo"));

				// int height = buildRowHeight(r.getFlights()) * 300;
				// sheet.setRowView(row, height);
				// log.debug("row:" + row + " , height=" + height);

				row++;
			}

			// 写入数据
			wwb.write();

			return new File(filePath);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (wwb != null) {
				try {
					wwb.close();
				} catch (WriteException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private static String getOrderType(MapBean r) {
		if (r.containsKey("siid")) {
			return "入库";
		} else if (r.containsKey("soid")) {
			return "出库";
		} else if (r.containsKey("isid")) {
			return "盘点";
		} else if (r.containsKey("toid")) {
			return "转仓";
		}
		return StringUtils.EMPTY;
	}

}
