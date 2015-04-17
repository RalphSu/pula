/**
 * Created on 2008-3-29 03:59:55
 *
 * NILVAR 2008
 * $Id$
 */
package puerta.support.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import puerta.support.mls.Mls;

/**
 * 
 * @author tiyi
 * 
 */
public class PewUtils {
	private static final Logger logger = Logger.getLogger(PewUtils.class);

	public static String toEnabledString(Mls mls, boolean enable) {
		return BooleanUtils.toString(enable, mls.t("enabled"),
				mls.t("disabled"));
	}

	public static String toVisibleString(Mls mls, boolean visible) {
		return BooleanUtils
				.toString(visible, mls.t("visible"), mls.t("hidden"));
	}

	/**
	 * @param fileFileName
	 * @return
	 */
	public static String checkUploadPic(MultipartFile file,
			String fileFileName, boolean flashSupport) {
		if (file == null) {
			if (flashSupport) {
				Mls.raise("UploadFileErrorFlash");
			} else {
				Mls.raise("UploadFileError");
			}
		}
		String ret = null;
		if (file != null) {
			ret = StringUtils.lowerCase(FilenameUtils
					.getExtension(fileFileName));
			// ret = FileHelper.getExtension(fileFileName);
			boolean isPhoto = isFileFormat(ret, "jpg", "gif", "png");
			// FileHelper.isFileFormat(fileFileName, "jpg",
			// "gif", "png");
			boolean isFlash = flashSupport && isFileFormat(ret, "swf");
			if (!isPhoto && !isFlash) {
				String extNameDefault = "gif/jpg/png";
				if (flashSupport) {
					extNameDefault += "/swf";
				}
				Mls.raise("UploadFileExtLimit", ret, extNameDefault);
			}
		}

		return ret;
	}

	public static boolean isFileFormat(String ret, String... exts) {
		for (String s : exts) {
			if (s.equals(ret)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param file
	 * @return
	 */
	public static String checkUploadFile(MultipartFile file, String fileFileName) {

		String ret = null;
		if (file != null) {
			ret = FilenameUtils.getExtension(fileFileName);
		}

		return ret;
	}

	public static String checkUploadFile(MultipartFile file,
			String fileFileName, String... extName) {
		String names = "";
		for (String s : extName) {
			names += "/" + s;
		}
		if (file == null) {
			Mls.raise("UploadFileError1", names);

		}
		String ret = null;
		if (file != null) {
			ret = StringUtils.lowerCase(FilenameUtils
					.getExtension(fileFileName));
			boolean matchExt = isFileFormat(ret, extName);

			if (!matchExt) {
				Mls.raise("UploadFileError1", names);
			}
		}

		return ret;
	}

	/**
	 * @param file
	 * @param extName
	 * @param id
	 * @param filePath
	 * @throws IOException
	 */
	public static String uploadFile(MultipartFile file, String extName,
			String id, String filePath) throws IOException {
		if (file != null) {
			// mkdir
			logger.debug("folder:" + filePath);
			FileUtils.forceMkdir(new File(filePath));
			// mk path
			// String extName = FileHelper.getExtension(file);
			// String fileName = po.getId() + "." + extName;
			String target = null;
			if (StringUtils.isEmpty(extName)) {
				target = filePath + "/" + id;
			} else {
				target = filePath + "/" + id + "." + extName;
			}
			// FileUtils.copyFile(file, new File(target));
			// FileUtils.w
			file.transferTo(new File(target));
			return target;
		}
		return null;

	}

	public static void writeFile(String content, String fileName,
			String filePath) throws IOException {
		content = StringUtils.defaultString(content);
		FileUtils.forceMkdir(new File(filePath));
		FileUtils.writeStringToFile(new File(filePath + "/" + fileName),
				content, "utf-8");
		// write
		// FileOutputStream fos = new FileOutputStream(filePath + "/" +
		// fileName);
		// Writer out = new OutputStreamWriter(fos, "utf-8");
		// out.write(content);
		// out.close();
		// fos.close();

	}

	public static String loadFile(File ff) throws IOException {
		return FileUtils.readFileToString(ff, "utf-8");
	}
}
