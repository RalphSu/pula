/**
 * Created on 2008-10-5 上午11:06:05
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author tiyi
 * 
 */
public class FileTool {

	/**
	 * @param coverFile
	 * @param cover
	 */
	public static void saveFile(String file, String data) {
		if (StringUtils.isEmpty(data)) {
			return;
		}

		File f = new File(file);

		String dir = getFolder(f.getAbsolutePath());

		FileHelper.mkdir(dir);

		String base64 = data;

		byte[] bs = Base64.getDecoder().decode(base64);
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			os.write(bs);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				os = null;
			}
		}

	}

	public static String loadFileToBase64(String file) {
		File f = new File(file);

		if (!f.exists()) {
			return "";
		}
		InputStream is = null;
		try {
			int n = (int) f.length();
			byte[] bs = new byte[n];
			is = new FileInputStream(f);
			is.read(bs);
			return Base64.getEncoder().encodeToString(bs);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			if (is != null) {

				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				is = null;
			}
		}

		return "";

	}

	/**
	 * @param absolutePath
	 * @return
	 */
	private static String getFolder(String path) {
		int s = StringUtils.lastIndexOf(path, File.separator);
		return StringUtils.left(path, s);
	}
}
