/**
 * 
 */
package pula.sys.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import puerta.support.AttachmentFile;
import puerta.support.Pe;
import puerta.support.annotation.Barrier;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.services.SessionUserService;

/**
 * @author Liangfei
 *
 */
@Controller
public class ImageController {

	private static final Logger logger = LoggerFactory
			.getLogger(ImageController.class);

	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	FileAttachmentDao fileAttachmentDao;

	@RequestMapping
	@ResponseBody
	@Barrier(PurviewConstants.IMAGE)
	public AttachmentFile icon(
			@RequestParam(value = "fp", required = false) String fp,
			@RequestParam(value = "sub", required = false) String sub,
			HttpServletResponse res) {
		// simply check the path to avoid client traversing
		if (sub.contains("/") || sub.contains(File.separator)
				|| sub.contains("\\")) {
			Pe.raise("sub folder should be simple controller names, not allow special characters!");
		}

		String srcPath = null;
		if (StringUtils.isEmpty(sub)) {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
		} else {
			String basePath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
			int idx = basePath.lastIndexOf(File.separatorChar);
			if (idx >= 0) {
				srcPath = basePath.substring(0, idx) + File.separator + sub;
			} else {
				srcPath = basePath;
			}
		}
		String fullPath = srcPath + File.separatorChar + fp;

		return AttachmentFile.forShow(new File(fullPath));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping
	@ResponseBody
	public List<IconInfo> listDir(
			@RequestParam(value = "sub", required = false) String sub,
			HttpServletResponse res) {
		if (sub.contains("/") || sub.contains(File.separator)
				|| sub.contains("\\")) {
			Pe.raise("sub folder should be simple controller names, not allow special characters!");
		}

		String srcPath = null;
		if (StringUtils.isEmpty(sub)) {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
		} else {
			String basePath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
			int idx = basePath.lastIndexOf(File.separatorChar);
			if (idx >= 0) {
				srcPath = basePath.substring(0, idx) + File.separator + sub;
			} else {
				srcPath = basePath;
			}
		}

		List<IconInfo> results = new ArrayList<ImageController.IconInfo>();
		try {
			Collection<File> files = (Collection<File>) FileUtils.listFiles(
					new File(srcPath), new SuffixFileFilter(
							new String[] { "png" }), null);
			for (File f : files) {
				IconInfo ii = new IconInfo();
				ii.sub = sub;
				ii.fp = f.getName();

				results.add(ii);
			}
		} catch (Exception e) {
			logger.error("list folder error", e);
		}

		return results;
	}

	public static class IconInfo {
		@JsonProperty
		public String sub;
		@JsonProperty
		public String fp;
	}

}
