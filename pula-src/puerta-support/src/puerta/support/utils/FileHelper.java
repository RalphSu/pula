/*
 * Created on 2004-10-20
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package puerta.support.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import puerta.support.BasementException;

/**
 * @author tiyi 2004-10-20
 */
public class FileHelper {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FileHelper.class);

	public static String replace(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals(""))
			return strSource;
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	public static String filterInvalidLetter(String key) {
		if (logger.isDebugEnabled()) {
			logger.debug("filterInvalidLetter(String) - start"); //$NON-NLS-1$
		}

		String data = "\\/:*?\"<>|";
		if (key == null)
			return "";
		for (int i = 0; i < data.length(); i++) {
			key = replace(key, String.valueOf(data.charAt(i)), "");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("filterInvalidLetter(String) - end"); //$NON-NLS-1$
		}
		return key;
	}

	public static void generateEmptyFile(String path, String fileName)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("generateEmptyFile(String, String) - start"); //$NON-NLS-1$
		}

		FileOutputStream fout = null;
		try {

			fout = new FileOutputStream(path + File.separator + fileName);
			fout.close();
		} finally {
			if (fout != null) {
				fout.close();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("generateEmptyFile(String, String) - end"); //$NON-NLS-1$
		}
	}

	public static long saveFile(byte[] bytes, String savePath) throws Exception {

		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(savePath);
			fout.write(bytes);
			fout.close();

			File f = new File(savePath);
			return f.length();

		} catch (FileNotFoundException e1) {
			logger.error("文件无法找到");
			e1.printStackTrace();
			throw new BasementException(e1.getMessage());

		} catch (IOException e) {

			if (fout != null) {
				try {
					fout.close();
				} catch (IOException e2) {
					e2.printStackTrace();
					throw new BasementException(e2.getMessage());
				}
			}

			logger.error("IO错误");
			e.printStackTrace();
			throw new BasementException(e.getMessage());
		}
	}

	public static void copyFile(String src, String dest) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("copyFile(String, String) - start"); //$NON-NLS-1$
		}

		// long size = 0;
		try {
			// instance the File as file_in and file_out
			logger.debug("in file:" + src);
			logger.debug("out file:" + dest);
			java.io.File file_in = new java.io.File(src);
			java.io.File file_out = new java.io.File(dest);

			logger.debug("file_out=" + file_out.getPath());

			// size = file_in.length();
			FileInputStream in1 = new FileInputStream(file_in);
			FileOutputStream out1 = new FileOutputStream(file_out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in1.read(bytes)) != -1)
				out1.write(bytes, 0, c);
			in1.close();
			out1.close();

		} catch (IOException e) {
			logger.error("copyFile(String, String)", e); //$NON-NLS-1$

			e.printStackTrace();
			throw new BasementException(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("copyFile(String, String) - end"); //$NON-NLS-1$
		}
	}

	public static void copyFile(File src, String dest) {
		if (logger.isDebugEnabled()) {
			logger.debug("copyFile(File, String) - start"); //$NON-NLS-1$
		}

		// long size = 0;
		try {
			// instance the File as file_in and file_out
			logger.debug("in file:" + src);
			logger.debug("out file:" + dest);

			java.io.File file_out = new java.io.File(dest);

			logger.debug("parent dir=" + file_out.getParent());

			mkdir(file_out.getParent());

			// size = file_in.length();
			FileInputStream in1 = new FileInputStream(src);
			FileOutputStream out1 = new FileOutputStream(file_out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in1.read(bytes)) != -1)
				out1.write(bytes, 0, c);
			in1.close();
			out1.close();

		} catch (IOException e) {
			logger.error("copyFile(File, String)", e); //$NON-NLS-1$

			e.printStackTrace();
			throw new BasementException("global.msg.systemerror");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("copyFile(File, String) - end"); //$NON-NLS-1$
		}
	}

	public static long copyFileAndReturnSize(String src, String dest)
			throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("copyFileAndReturnSize(String, String) - start"); //$NON-NLS-1$
		}

		long size = 0;
		try {
			// instance the File as file_in and file_out
			java.io.File file_in = new java.io.File(src);
			java.io.File file_out = new java.io.File(dest);
			size = file_in.length();
			FileInputStream in1 = new FileInputStream(file_in);
			FileOutputStream out1 = new FileOutputStream(file_out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = in1.read(bytes)) != -1)
				out1.write(bytes, 0, c);
			in1.close();
			out1.close();

		} catch (IOException e) {
			logger.error("copyFileAndReturnSize(String, String)", e); //$NON-NLS-1$

			throw new BasementException(e.getMessage());
		}

		if (logger.isDebugEnabled()) {
			logger.debug("copyFileAndReturnSize(String, String) - end"); //$NON-NLS-1$
		}
		return size;
	}

	public static byte[] loadFile(String path) throws Exception {
		FileInputStream in = null;

		try {
			in = new FileInputStream(path);
			int i = in.available();
			logger.debug("文件尺寸：" + i);
			byte readBytes[] = new byte[i];
			in.read(readBytes);
			return (readBytes);

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			throw new BasementException(e1.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			throw new BasementException(e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e2) {
					e2.printStackTrace();
					throw new BasementException(e2.getMessage());
				}
			}
		}

	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 * @return
	 */
	public static boolean mkdir(String path) {
		boolean flag = false;
		File dir;

		if (path == null) {
			logger.error("路径(" + path + ")无效，导致目录无法创建");
			return false;
		}

		// 新建文件对象
		dir = new File(path);
		if (dir == null) {
			logger.error("路径(" + path + ")无效，导致实例无法创建");
			return false;
		}

		if (dir.isFile()) {
			logger.error("路径(" + path + ")指向一个文件");
			return false;
		}

		if (!dir.exists()) {
			logger.warn("路径(" + path + ")不存在，开始创建");
			boolean result = dir.mkdirs();
			if (result == false) {
				return false;
			}

			return true;
		} else {
			// 存在也算成功
			logger.warn("路径(" + path + ")已经存在，跳过创建");
			return true;
		}
	}

	/**
	 * @param path
	 */
	public static void delete(String path) {

		File file = new File(path);
		if (file.exists()) {
			logger.info("删除文件:" + path);
			file.delete();
		} else {
			logger.warn("文件并未找到:" + path);
		}

	}

	public static String extractFileName(String _TargetURL) {
		if (logger.isDebugEnabled()) {
			logger.debug("extractFileName(String) - start"); //$NON-NLS-1$
		}

		String returnStr = "";
		java.util.StringTokenizer tokens = new java.util.StringTokenizer(
				_TargetURL, "/");
		while (tokens.hasMoreTokens()) {
			String tmpStr = tokens.nextToken();
			returnStr = tmpStr;
			// if (tmpStr.indexOf(".") > -1) {
			// //returnStr = tmpStr;
			// while (tokens.hasMoreTokens()) {
			// tmpStr = "/" + tokens.nextToken();
			// returnStr += tmpStr;
			// }
			// break;
			// }
		}

		if (logger.isDebugEnabled()) {
			logger.debug("extractFileName(String) - end"); //$NON-NLS-1$
		}
		return returnStr;
	}

	public static void main(String[] args) {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - start"); //$NON-NLS-1$
		}

		System.out
				.println(extractFileName("C:/afdsafsd/asfdasfdfdsaasfd/adf adsfa/dsf/afd/bdfe.txt"));

		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - end"); //$NON-NLS-1$
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public static String getRamdomFileName(String filePath) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRamdomFileName(String) - start"); //$NON-NLS-1$
		}

		mkdir(filePath);
		String fn = filePath + File.separator + RandomTool.getRandomString(12);
		File f = new File(fn);
		logger.debug("FileName=" + fn);
		while (f.exists()) {
			fn = filePath + RandomTool.getRandomString(8);
			logger.debug("FileName=" + fn);
			f = new File(fn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getRamdomFileName(String) - end"); //$NON-NLS-1$
		}
		return fn;
	}

	public static File getRamdomFile(String filePath) {
		if (logger.isDebugEnabled()) {
			logger.debug("getRamdomFileName(String) - start"); //$NON-NLS-1$
		}

		mkdir(filePath);
		String fn = filePath + File.separator + RandomTool.getRandomString(12);
		File f = new File(fn);
		logger.debug("FileName=" + fn);
		while (f.exists()) {
			fn = filePath + RandomTool.getRandomString(8);
			logger.debug("FileName=" + fn);
			f = new File(fn);
		}

		return f;
	}

	public static boolean isFileFormat(String fileName, String... extName) {
		if (logger.isDebugEnabled()) {
			logger.debug("isFileFormat(String, String) - start"); //$NON-NLS-1$
		}

		StringTokenizer token = new StringTokenizer(fileName, ".");
		String ext = null;
		while (token.hasMoreElements()) {
			ext = (String) token.nextElement();
		}

		if (ext != null) {
			logger.debug(ext + ':' + extName);
			for (String en : extName) {
				if (ext.compareToIgnoreCase(en) == 0) {
					return true;
				}
			}
		}

		return false;
	}

	public static String getExtension(File f) {
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(File) - start"); //$NON-NLS-1$
		}

		String returnString = (f != null) ? getExtension(f.getName()) : "";
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(File) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static String getExtension(String filename) {
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(String) - start"); //$NON-NLS-1$
		}

		String returnString = getExtension(filename, "");
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(String) - end"); //$NON-NLS-1$
		}
		return returnString;
	}

	public static String getExtension(String filename, String defExt) {
		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(String, String) - start"); //$NON-NLS-1$
		}

		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > -1) && (i < (filename.length() - 1))) {
				String returnString = filename.substring(i + 1);
				if (logger.isDebugEnabled()) {
					logger.debug("getExtension(String, String) - end"); //$NON-NLS-1$
				}
				return returnString.toLowerCase();
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getExtension(String, String) - end"); //$NON-NLS-1$
		}
		return defExt;
	}

	/**
	 * @param name
	 * @return
	 */
	public static String getFileFrontName(String name) {
		if (logger.isDebugEnabled()) {
			logger.debug("getFileFrontName(String) - start"); //$NON-NLS-1$
		}

		String fileName = "";
		int i = name.lastIndexOf('.');
		if ((i > 0) && (i < (name.length() - 1))) {
			fileName = name.substring(0, i);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("getFileFrontName(String) - end"); //$NON-NLS-1$
		}
		return fileName;
	}

	/**
	 * @param string
	 * @return
	 * @throws IOException
	 */
	public static String readStringFromFile(String string) throws IOException {
		FileReader fr = new FileReader(string);// 建立FileReader对象，并实例化为fr
		BufferedReader br = new BufferedReader(fr);// 建立BufferedReader对象，并实例化为br
		String Line = br.readLine();// 从文件读取一行字符串
		// 判断读取到的字符串是否不为空
		StringBuffer sb = new StringBuffer();
		while (Line != null) {
			sb.append(Line).append("\r\n");
			Line = br.readLine();// 从文件中继续读取一行数据
		}
		br.close();// 关闭BufferedReader对象
		fr.close();// 关闭文件
		return sb.toString();
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean exists(String string) {
		File f = new File(string);
		return f.exists();
	}

	/**
	 * @param is
	 * @param path
	 * @throws IOException
	 */
	public static void saveFile(InputStream input, String path)
			throws IOException {
		try {
			FileOutputStream fos = new FileOutputStream(path);

			byte[] b = new byte[1024 * 5];
			int len;
			int size = 0;
			while ((len = input.read(b)) != -1) {
				fos.write(b, 0, len);
				size += len;
				// worker.say("...<" + path + ">" + size); //$NON-NLS-1$
				// //$NON-NLS-2$

				logger.debug("...<" + path + ">" + size);
			}
			fos.flush();
			fos.close();
			input.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param is
	 * @param filePath
	 */
	public static void appendFile(InputStream input, String path) {
		try {
			File f = new File(path);
			long fs = f.length();
			RandomAccessFile raf = new RandomAccessFile(path, "rw");
			raf.seek(fs);

			byte[] b = new byte[1024 * 5];
			int len;
			int size = 0;
			logger.debug("append <" + path + "> start from " + fs);
			while ((len = input.read(b)) != -1) {
				raf.write(b, 0, len);
				size += len;
				// worker.say("...<" + path + ">" + size); //$NON-NLS-1$
				// //$NON-NLS-2$

				logger.debug("...<" + path + ">" + (size + fs));
			}

			raf.close();
			input.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * @param fileSize
	 */
	public static String getFileSizeText(long fileSize) {
		StringBuilder sb = new StringBuilder(10);

		int m = (int) (fileSize / (1024 * 1024));
		if (m > 0) {
			return sb
					.append(Arith.round(((double) fileSize / (1024 * 1024)), 2))
					.append(" MB").toString();
		}
		long left = fileSize % (1024 * 1024);
		int k = (int) (left / 1024);

		if (k > 0) {
			return sb.append(Arith.round(((double) left / (1024)), 2))
					.append(" KB").toString();
		}

		left = fileSize % (1024); // bytes;

		return sb.append(left).append(" 字节").toString();

	}

	/**
	 * @param fileName
	 * @param string
	 * @return
	 */
	public static String changeFileExt(String fileName, String ext) {
		int dotPos = fileName.lastIndexOf('.');
		if (dotPos == -1) {
			dotPos = fileName.length(); // beyond the last char }
		}

		fileName = fileName.substring(0, dotPos) + "." + ext; // change ext

		return fileName;
	}

	/**
	 * @param url
	 * @return
	 */
	public static String getFileNameFromURL(String url) {
		int n = StringUtils.lastIndexOf(url, "/");
		if (n < 0)
			return "";

		return StringUtils.right(url, url.length() - n - 1);

	}

	public static String getMD5(File file) {
		if (!file.exists()) {
			return "";
		}
		FileInputStream fis = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length = -1;
			// System.out.println("开始算");
			logger.debug("start make md5");
			while ((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}
			// System.out.println("算完了");
			logger.debug("finish make md5");
			return bytesToString(md.digest());
		} catch (IOException ex) {
			// Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null,
			// ex);
			logger.error(ex);
			return null;
		} catch (NoSuchAlgorithmException ex) {
			logger.error(ex);
			return null;
		} finally {
			try {
				fis.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}
	}

	public static String bytesToString(byte[] data) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] temp = new char[data.length * 2];
		for (int i = 0; i < data.length; i++) {
			byte b = data[i];
			temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
			temp[i * 2 + 1] = hexDigits[b & 0x0f];
		}
		return new String(temp);

	}
}
