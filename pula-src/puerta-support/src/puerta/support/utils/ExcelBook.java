/**
 * Created on 2010-11-2
 * WXL 2009
 * $Id$
 */
package puerta.support.utils;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import puerta.support.Pe;

/**
 * 
 * @author tiyi
 * 
 */
public class ExcelBook {

	private WritableWorkbook workbook;
	private Workbook tplWorkbook;
	private WritableSheet sheet;
	private Sheet tplSheet;
	private int sheetIndex;

	/**
	 * @param tplFilePath
	 * @param outFile
	 */
	public ExcelBook(String tplPath, String filePath) {
		try {
			tplWorkbook = Workbook.getWorkbook(new File(tplPath));
			workbook = Workbook.createWorkbook(new File(filePath), tplWorkbook);

		} catch (Exception e) {
			throw new RuntimeException("错误的模板文件:" + e.getMessage());
		}
		sheetIndex = 0;
		resetSheet();
	}

	/**
	 * 
	 */
	private void resetSheet() {

		if (workbook.getSheets().length <= sheetIndex) {
			Pe.raise("错误的模板文件");
		}
		changeSheet();

	}

	/**
	 * 
	 */
	private void changeSheet() {
		sheet = workbook.getSheet(sheetIndex);
		tplSheet = tplWorkbook.getSheet(sheetIndex);
	}

	public void close() throws WriteException, IOException {
		workbook.write();
		workbook.close();

		tplWorkbook.close();

	}

	public static ExcelBook create(String tplFilePath, String outFile) {
		return new ExcelBook(tplFilePath, outFile);
	}

	/**
	 * @param i
	 * @param row
	 * @param value
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void write(Object col, int row, Object value)
			throws RowsExceededException, WriteException {
		ExcelHelper.write(this.sheet, this.tplSheet, col, row, value);

	}

	/**
	 * @param i
	 * @param row
	 * @param fullSalon
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void writeRow(Object col, int row, Object... objs)
			throws RowsExceededException, WriteException {
		ExcelHelper.writeRow(sheet, tplSheet, col, row, objs);

	}

	public void writeRow(Object col, int row, int copyRow, Object... objs)
			throws RowsExceededException, WriteException {
		ExcelHelper.writeRow(sheet, tplSheet, col, row, copyRow, objs);
	}

	/**
	 * @param row
	 */
	public void addRow(int row, int rowCount) {
		for (int i = 0; i < rowCount; i++)
			this.sheet.insertRow(row);

	}

	/**
	 * @param col
	 * @param row
	 * @param cOPYROW
	 * @param endCol
	 * @throws WriteException
	 * @throws RowsExceededException
	 */
	public void writeBlankRow(Object col, int row, int copyRow, Object endCol)
			throws RowsExceededException, WriteException {

		int inx = ExcelHelper.getCol(endCol);
		int start = ExcelHelper.getCol(col);
		for (int r = start; r <= inx; r++) {
			ExcelHelper.write(sheet, tplSheet, new Integer(r), row, copyRow,
					start, (Object) null);
		}

	}

	/**
	 * @param n
	 */
	public void setSheetName(String n) {
		this.sheet.setName(n);

	}

	/**
	 * @param row
	 */
	public void removeRow(int row) {
		this.sheet.removeRow(row);

	}

	/**
	 * 
	 */
	public void nextSheet() {
		sheetIndex++;
		resetSheet();
	}
}
