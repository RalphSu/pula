package pula.sys.app;

import java.io.File;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import puerta.support.annotation.Barrier;
import puerta.support.utils.DateExTool;
import puerta.support.utils.FileHelper;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.PewUtils;
import puerta.support.utils.WxlSugar;
import puerta.system.keeper.ParameterKeeper;
import pula.sys.BhzqConstants;

@Controller
public class FileUploadController {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(FileUploadController.class);

	@Resource
	ParameterKeeper parameterKeeper;

	@RequestMapping
	@Barrier()
	public ModelAndView _upload(
			@RequestParam(value = "imgFile") MultipartFile file,
			@RequestParam(value = "type", defaultValue = "0", required = false) int type,
			HttpServletRequest request, HttpServletResponse response) {

		response.setCharacterEncoding("UTF-8");
		Map<String, Object> ret = WxlSugar.newHashMap();
		// copy Filie to directory ;
		if (file.isEmpty()) {

			ret.put("error", 1);
			ret.put("message", "请上传文件");
			// Pe.raise("请上传文件");
			return wrap(ret);
		}

		if (type == 1) {
			// image
			PewUtils.checkUploadPic(file, file.getOriginalFilename(), false);
		} else if (type == 2) {
			PewUtils.checkUploadPic(file, file.getOriginalFilename(), true);
		}

		// PewUtils.checkUploadPic(file, file.getOriginalFilename(), true);
		// dir = extractDir(dir);

		String path = parameterKeeper
				.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);

		FileHelper.mkdir(path);

		String newFileName = DateExTool.formatDateTime(Calendar.getInstance(),
				"yyyyMMddHHmmss")
				+ "_"
				+ new Random().nextInt(1000)
				+ "."
				+ FileHelper.getExtension(file.getOriginalFilename());
		try {
			file.transferTo(new File(path + "/" + newFileName));
		} catch (Exception e) {
			e.printStackTrace();
			// Pe.raise("上传失败:" + e.getMessage());

			ret.put("error", 1);
			ret.put("message", e.getMessage());

			return wrap(ret);
		}
		ret.put("error", 0);
		ret.put("filePath", newFileName);
		ret.put("fileName", file.getOriginalFilename());

		ModelAndView mv = wrap(ret);
		// logger.debug("ret=" + strRet);
		return mv;
	}

	private ModelAndView wrap(Map<String, Object> ret) {
		String str = JacksonUtil.toString(ret);

		ModelAndView m = new ModelAndView();
		m.addObject("json", str);
		return m;
	}

	@RequestMapping
	@Barrier()
	public ModelAndView upload(
			@RequestParam(value = "type", defaultValue = "0", required = false) int type) {
		return new ModelAndView().addObject("type", type);
	}

}
