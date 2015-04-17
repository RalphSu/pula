/**
 * Created on 2010-1-27
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.format.CellFormat;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * 
 * @author tiyi
 * 
 */
public class ExcelHelper {

	/**
	 * @param sheet
	 * @param i
	 * @param i2
	 * @return
	 */
	public static String getValue(Sheet sheet, int col, int row) {
		return StringUtils.trim(sheet.getCell(col, row).getContents());
	}

	public static String getValue(Sheet sheet, char c, int row) {
		if (c >= 'A' && c <= 'Z') {
			Cell cell = sheet.getCell(c - 'A', row);
			// cell.get
			return StringUtils.trim(cell.getContents());
		}
		return "";
	}

	/**
	 * @param sheet
	 * @param col
	 * @param row
	 * @return
	 */
	public static int getInt(Sheet sheet, int col, int row) {
		return NumberUtils.toInt(sheet.getCell(col, row).getContents(), 0);
	}

	public static int getInt(Sheet sheet, char i, int i2) {
		String v = getValue(sheet, i, i2);
		return NumberUtils.toInt(v, 0);
	}

	/**
	 * @param col
	 * @param row
	 * @param string
	 * @param tplWorkbook
	 * @return
	 */
	public static Label createLabel(int col, int row, String string,
			Sheet tplSheet) {

		CellFormat cf = tplSheet.getCell(col, row).getCellFormat();
		WritableCellFormat format = new WritableCellFormat(cf);
		Label l = new Label(col, row, string, format);

		return l;
	}

	/**
	 * @param col
	 * @param row
	 * @param i
	 * @param cfDay
	 * @return
	 */
	public static Label createLabel(int col, int row, Object v, CellFormat cf) {
		WritableCellFormat format = new WritableCellFormat(cf);
		Label l = new Label(col, row, String.valueOf(v), format);

		return l;
	}

	/**
	 * @param sheet
	 * @param i
	 * @param rowFix
	 */
	public static void insertRow(WritableSheet sheet, int i, int rowFix) {
		while (rowFix > 0) {
			sheet.insertRow(i);
			rowFix--;
		}
	}

	/**
	 * @param sheet
	 * @param i
	 * @param j
	 */
	public static void removeRow(WritableSheet sheet, int i, int j) {
		int n = i;
		while (i <= j) {
			sheet.removeRow(n);
			i++;
		}

	}

	/**
	 * @param col
	 * @param row
	 * @param totalInSalonTimes
	 * @param tplSheet
	 * @return
	 */
	public static Label createLabelChar(char col, int row, Object obj,
			Sheet tplSheet) {
		return createLabel(col - 'A', row, String.valueOf(obj), tplSheet);
	}

	/**
	 * @param c
	 * @param i
	 * @param totalInSalonCount
	 * @param tplSheet
	 * @param d
	 * @param j
	 * @return
	 */
	public static Label createLabelChar(char c, int i, Object v,
			Sheet tplSheet, char d, int j) {

		CellFormat cf = tplSheet.getCell(d - 'A', j).getCellFormat();
		WritableCellFormat format = new WritableCellFormat(cf);
		Label l = new Label(c - 'A', i, String.valueOf(v), format);

		return l;
	}

	/**
	 * @param sheet
	 * @param string
	 * @param i
	 * @return
	 */
	public static String getValue(Sheet sheet, String string, int row) {
		int col = getCol(string);
		return StringUtils.trim(sheet.getCell(col, row).getContents());
	}

	/**
	 * @param string
	 * @return
	 */
	public static int getCol(String string) {
		int col = 0;
		int range = 'Z' - 'A' + 1;
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			int pos = string.length() - i;
			if (c >= 'A' && c <= 'Z' && pos == 2) {
				col += ((c - 'A') + 1) * range;
			} else {
				col += (c - 'A');
			}

		}
		return col;
	}

	public static void main(String[] args) {

		System.out.println("A:" + getCol("A"));
		System.out.println("AA:" + getCol("AA"));
		System.out.println("Z:" + getCol("Z"));
		System.out.println("BA:" + getCol("BA"));
		System.out.println("AZ:" + getCol("AZ"));
		List<Integer> numbers = WxlSugar.newArrayList();
		for (char i = 'A'; i <= 'Z'; i++) {
			for (char j = 'A'; j <= 'Z'; j++) {
				// System.out.println(i + "" + j + ":" + getCol(i + "" + j));
				numbers.add(getCol(i + "" + j));
			}
		}
		int lastNumber = 0;
		for (Integer n : numbers) {
			if (lastNumber == 0) {
				lastNumber = n;
				continue;
			}
			if (lastNumber - n != -1) {
				System.err.println("lastNUmber=" + lastNumber + " this=" + n);
			}
			lastNumber = n;
		}
	}

	/**
	 * @param endDate
	 * @return
	 */
	public static Calendar getDate(String endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param sheet
	 * @param row
	 * @param i
	 * @param outputA
	 * @param outputC
	 * @param salonCount
	 * @param trainedCount
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public static void writeRow(WritableSheet sheet, int col, int row,
			Object... objs) throws RowsExceededException, WriteException {
		writeRowWithFormat(sheet, col, row, null, objs);
	}

	/**
	 * @param s
	 * @param tplSheet
	 * @param c
	 * @param lastYearMonth
	 * @param d
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	// public static void write(WritableSheet s, Sheet tplSheet, char c, int
	// row,
	// Object d) throws RowsExceededException, WriteException {
	// int col = getCol(c);
	// CellFormat cf = tplSheet.getCell(col, row).getCellFormat();
	// WritableCellFormat format = new WritableCellFormat(cf);
	// WritableCell cell = null;
	// if (d instanceof Double) {
	// cell = new Number(col, row, (Double) d, format);
	// } else {
	// cell = new Label(col, row, d.toString(), format);
	// }
	//
	// s.addCell(cell);
	//
	// }

	/**
	 * @param s
	 * @param tplSheet
	 * @param string
	 * @param row
	 * @param d
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public static void write(WritableSheet s, Sheet tplSheet, Object c,
			int row, Object d) throws RowsExceededException, WriteException {

		if (d == null) {
			d = "";
		}
		int col = 0;
		col = getCol(c);

		CellFormat cf = null;
		if (tplSheet.getRows() > row && tplSheet.getColumns() > col)
			cf = tplSheet.getCell(col, row).getCellFormat();

		WritableCellFormat format = null;

		if (cf != null) {
			format = new WritableCellFormat(cf);

		} else {

		}
		WritableCell cell = null;
		if (d instanceof Double) {
			if (format == null) {
				cell = new Number(col, row, (Double) d);
			} else {
				cell = new Number(col, row, (Double) d, format);
			}
		} else if (d instanceof Integer) {
			if (format == null) {
				cell = new Number(col, row, (Integer) d);
			} else {
				cell = new Number(col, row, (Integer) d, format);
			}

		} else {
			if (format == null) {
				cell = new Label(col, row, d.toString());
			} else {
				cell = new Label(col, row, d.toString(), format);
			}
		}

		s.addCell(cell);

	}

	public static void copy(WritableSheet s, Sheet tplSheet, int c, int row,
			int src_col, int src_row) throws RowsExceededException,
			WriteException {
		Cell src_cell = null;
		if (tplSheet.getRows() > src_row && tplSheet.getColumns() > src_col)
			src_cell = tplSheet.getCell(src_col, src_row);
		else
			return;

		CellFormat cf = src_cell.getCellFormat();
		String content = src_cell.getContents();
		WritableCell cell = null;
		if (cf == null) {
			cell = new Label(c, row, content);
		} else {
			cell = new Label(c, row, content, cf);
		}

		s.addCell(cell);

	}

	/**
	 * @param c
	 * @return
	 */
	public static int getCol(Object c) {
		int col;
		if (c instanceof String) {
			col = getCol((String) c);
		} else if (c instanceof Character) {
			col = getCol(c.toString());
		} else {
			col = (Integer) c;
		}
		return col;
	}

	/**
	 * @param s
	 * @param tplSheet
	 * @param i
	 * @param row
	 * @param objs
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public static void writeRow(WritableSheet s, Sheet tplSheet, Object objCol,
			int row, Object... objs) throws RowsExceededException,
			WriteException {

		int col = 0;
		col = getCol(objCol);
		for (Object obj : objs) {
			write(s, tplSheet, col++, row, obj);
		}

	}

	public static void writeRow(WritableSheet s, Sheet tplSheet, Object objCol,
			int row, int copyRow, Object... objs) throws RowsExceededException,
			WriteException {

		int col = 0;
		col = getCol(objCol);
		for (Object obj : objs) {
			write(s, tplSheet, col, row, copyRow, col++, obj);
		}

	}

	public static void copyRow(WritableSheet s, Sheet tplSheet,
			Object beginCol, Object endCol, int row, int copyRow)
			throws RowsExceededException, WriteException {

		int col = 0;
		col = getCol(beginCol);
		int ecol = getCol(endCol);

		for (int c = col; c <= ecol; c++) {
			copy(s, tplSheet, c, row, c, copyRow);
		}

	}

	/**
	 * @param s
	 * @param tplSheet
	 * @param colIndex
	 * @param row
	 * @param tplRow
	 * @param tplCol
	 * @param d
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public static void write(WritableSheet s, Sheet tplSheet, Object c,
			int row, int tplRow, int tplCol, Object d)
			throws RowsExceededException, WriteException {
		if (d == null) {
			d = "";
		}
		int col = 0;
		col = getCol(c);

		CellFormat cf = null;
		if (tplSheet.getRows() > tplRow && tplSheet.getColumns() > tplCol)
			cf = tplSheet.getCell(tplCol, tplRow).getCellFormat();

		WritableCellFormat format = null;

		if (cf != null) {
			format = new WritableCellFormat(cf);

		} else {

		}
		WritableCell cell = null;
		if (d instanceof Double) {
			if (format == null) {
				cell = new Number(col, row, (Double) d);
			} else {
				cell = new Number(col, row, (Double) d, format);
			}
		} else if (d instanceof Integer) {
			if (format == null) {
				cell = new Number(col, row, (Integer) d);
			} else {
				cell = new Number(col, row, (Integer) d, format);
			}

		} else {
			if (format == null) {
				cell = new Label(col, row, d.toString());
			} else {
				cell = new Label(col, row, d.toString(), format);
			}
		}

		s.addCell(cell);

	}

	/**
	 * @param sheet
	 * @param c
	 * @param r
	 * @return
	 */
	public static Date getDateValue(Sheet sheet, char c, int row) {
		if (c >= 'A' && c <= 'Z') {
			Cell cell = sheet.getCell(c - 'A', row);

			if (cell.getType() == CellType.DATE) {
				return ((DateCell) cell).getDate();
			} else {
				return null;
			}

			// cell.get

		}
		return Calendar.getInstance().getTime();
	}

	public static void setColumnView(WritableSheet sheet, int[] fullWidth) {
		int idx = 0;
		for (int i : fullWidth) {
			sheet.setColumnView(idx++, i);// 设置某列(0)的列宽(20)
		}

	}

	public static void initColumns(WritableSheet sheet,
			XlsColumnSetting[] fullColumnsTicket) throws RowsExceededException,
			WriteException {
		// sheet.setRowView(2, 600);//设置某行(2)的行高(600/20)

		Label label;
		for (int i = 0; i < fullColumnsTicket.length; i++) {
			// Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是z
			// 在Label对象的子对象中指明单元格的位置和内容
			XlsColumnSetting xcs = fullColumnsTicket[i];
			label = new Label(i, 0, xcs.getName());
			sheet.addCell(label);// 将定义好的单元格添加到sheet表中
			sheet.setColumnView(i, xcs.getLength());// 设置某列(0)的列宽(20)
		}

	}

	public static void writeRowWithFormat(WritableSheet sheet, int col,
			int row, WritableCellFormat wcf, Object... objs)
			throws RowsExceededException, WriteException {
		// int i = 0;
		for (Object obj : objs) {
			if (obj instanceof Integer) {
				Number number = new Number(col++, row, (Integer) obj);
				number.setCellFormat(wcf);
				sheet.addCell(number);
			} else if (obj instanceof Double) {
				Number number = new Number(col++, row, (Double) obj);
				number.setCellFormat(wcf);
				sheet.addCell(number);
			} else if (obj instanceof Calendar) {
				Calendar cal = (Calendar) obj;
				DateTime dt = new DateTime(col++, row, cal.getTime());
				dt.setCellFormat(wcf);
				sheet.addCell(dt);
			} else if (obj instanceof String) {
				String s = (String) obj;
				if (StringUtils.indexOf(s, "=") == 0) {
					Formula f = new jxl.write.Formula(col++, row,
							StringUtils.right(s, s.length() - 1));
					f.setCellFormat(wcf);
					sheet.addCell(f);
				} else {
					Label l = new Label(col++, row, s);
					l.setCellFormat(wcf);
					sheet.addCell(l);

				}
			} else {
				if (obj == null) {
					col++;
					continue;
				}
				Label l = new Label(col++, row, obj.toString());
				l.setCellFormat(wcf);
				sheet.addCell(l);
			}
		}

	}

	// public static void border(WritableSheet s, int col, int row, int ecol,
	// Border top, BorderLineStyle medium) throws WriteException {
	//
	// for (int i = col; i <= ecol; i++) {
	// Cell cell = s.getCell(i, row);
	//
	// System.out.println(i + " ," + row + "@cell=" + cell);
	//
	// if (cell.getType() == CellType.EMPTY) {
	// continue;
	// }
	//
	// WritableCellFormat format = new WritableCellFormat(
	// cell.getCellFormat());
	// format.setBorder(top, medium);
	// // Label label = new RosterExcel(v) c.getContents();
	// Label labelcf = new Label(i, row, cell.getContents(), format);
	// s.addCell(labelcf);
	// }
	//
	// }
	//
	// public static void align(WritableSheet s, int col, int row, int ecol,
	// Alignment a) throws WriteException {
	//
	// for (int i = col; i <= ecol; i++) {
	// Cell cell = s.getCell(i, row);
	//
	// System.out.println(i + " ," + row + "@cell=" + cell);
	//
	// if (cell.getType() == CellType.EMPTY) {
	// continue;
	// }
	//
	// WritableCellFormat format = new WritableCellFormat(
	// cell.getCellFormat());
	// format.setAlignment(a);
	// // Label label = new RosterExcel(v) c.getContents();
	// Label labelcf = new Label(i, row, cell.getContents(), format);
	// s.addCell(labelcf);
	// }
	//
	// }

	public static void writeRowWithMap(WritableSheet s,
			Map<String, WritableCellFormat> map, Sheet tplSheet, Object objCol,
			int row, int copyRow, Object... objs) throws RowsExceededException,
			WriteException {

		int col = 0;
		col = getCol(objCol);
		for (Object obj : objs) {
			writeWithMap(s, map, tplSheet, col, row, copyRow, col++, obj);
		}

	}

	public static void writeWithMap(WritableSheet s,
			Map<String, WritableCellFormat> map, Sheet tplSheet, Object c,
			int row, int tplRow, int tplCol, Object d)
			throws RowsExceededException, WriteException {
		if (d == null) {
			d = "";
		}
		int col = 0;
		col = getCol(c);

		CellFormat cf = null;
		if (tplSheet.getRows() > tplRow && tplSheet.getColumns() > tplCol)
			cf = tplSheet.getCell(tplCol, tplRow).getCellFormat();

		String key = tplCol + ":" + tplRow;

		WritableCellFormat format = map.get(key);

		if (cf != null && format == null) {
			format = new WritableCellFormat(cf);
			map.put(key, format);
		} else {

		}
		WritableCell cell = null;
		if (d instanceof Double) {
			if (format == null) {
				cell = new Number(col, row, (Double) d);
			} else {
				cell = new Number(col, row, (Double) d, format);
			}
		} else if (d instanceof Integer) {
			if (format == null) {
				cell = new Number(col, row, (Integer) d);
			} else {
				cell = new Number(col, row, (Integer) d, format);
			}

		} else {
			if (format == null) {
				cell = new Label(col, row, d.toString());
			} else {
				cell = new Label(col, row, d.toString(), format);
			}
		}

		s.addCell(cell);

	}

	public static void copyRowWithMap(WritableSheet s,
			Map<String, WritableCellFormat> map, Sheet tplSheet,
			Object beginCol, Object endCol, int row, int copyRow)
			throws RowsExceededException, WriteException {

		int col = 0;
		col = getCol(beginCol);
		int ecol = getCol(endCol);

		for (int c = col; c <= ecol; c++) {
			copyWithMap(s, map, tplSheet, c, row, c, copyRow);
		}

	}

	public static void copyWithMap(WritableSheet s,
			Map<String, WritableCellFormat> map, Sheet tplSheet, int c,
			int row, int src_col, int src_row) throws RowsExceededException,
			WriteException {
		Cell src_cell = null;
		if (tplSheet.getRows() > src_row && tplSheet.getColumns() > src_col)
			src_cell = tplSheet.getCell(src_col, src_row);
		else
			return;

		CellFormat cf = src_cell.getCellFormat();
		String content = src_cell.getContents();
		WritableCell cell = null;
		if (cf == null) {
			cell = new Label(c, row, content);
		} else {
			cell = new Label(c, row, content, cf);
		}

		s.addCell(cell);

	}

	public static String getValueMayNumber(Sheet sheet, char c, int row) {
		if (c >= 'A' && c <= 'Z') {
			Cell cell = sheet.getCell(c - 'A', row);
			// cell.get
			// return StringUtils.trim(cell.getContents());

			if (cell.getType() == CellType.NUMBER
					|| cell.getType() == CellType.NUMBER_FORMULA) {
				double v = ((NumberCell) cell).getValue();
				return String.valueOf(v);
			} else {
				return StringUtils.trim(cell.getContents());
			}

		}
		return "";
	}
}
