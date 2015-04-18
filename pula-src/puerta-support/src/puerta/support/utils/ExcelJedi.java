package puerta.support.utils;

import java.io.File;
import java.io.IOException;

import jxl.JXLException;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.commons.lang.StringUtils;

import puerta.support.Pe;

public class ExcelJedi {

	protected WritableWorkbook workbook;
	protected Workbook tplWorkbook;
	protected String filePath, tplPath;
	private String mark;
	private File file;

	// /**
	// * @return
	// */
	// protected String getTimeMark() {
	// return this.year + "-" + (this.month + 1);
	// }

	public String getFilePath() {
		return filePath;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getTplPath() {
		return tplPath;
	}

	public void setTplPath(String tplPath) {
		this.tplPath = tplPath;
	}

	protected void initWorkBook() {
		if (StringUtils.isEmpty(mark)) {
			mark = RandomTool.getRandomString(10);
		}
		String fn = this.filePath + mark + ".xls";
		this.file = new File(fn);
		try {
			tplWorkbook = Workbook.getWorkbook(new File((tplPath)));
			workbook = Workbook.createWorkbook(this.file, tplWorkbook);

		} catch (Exception e) {
			e.printStackTrace();
			Pe.raise("错误的模板文件:" + e.getMessage());
		}

		if (workbook.getSheets().length <= 0) {
			Pe.raise("错误的模板文件");
		}

	}

	public void execute(ExcelJediCallback c) {
		initWorkBook();
		try {
			c.process(workbook, tplWorkbook);
		} catch (JXLException e) {
			e.printStackTrace();
			Pe.raise("生成文件失败");
		}

		try {
			close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getFile() {
		return this.file;
	}

	/**
	 * @throws IOException
	 * @throws WriteException
	 * 
	 */
	public void close() throws IOException, WriteException {
		workbook.write();
		workbook.close();

		tplWorkbook.close();

	}
}
