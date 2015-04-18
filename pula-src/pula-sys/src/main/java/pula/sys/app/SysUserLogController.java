package pula.sys.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import puerta.PuertaWeb;
import puerta.support.AttachmentFile;
import puerta.support.PaginationSupport;
import puerta.support.Pe;
import puerta.support.ViewResult;
import puerta.support.annotation.Barrier;
import puerta.support.annotation.ObjectParam;
import puerta.support.mls.Mls;
import puerta.support.utils.DateExTool;
import puerta.support.utils.RandomTool;
import puerta.support.utils.WxlSugar;
import puerta.system.helper.ParameterHelper;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.PurviewConstants;
import pula.sys.conditions.SysUserLogCondition;
import pula.sys.daos.SysUserLogDao;
import pula.sys.domains.SysUserLog;

@Controller
public class SysUserLogController {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(SysUserLogController.class);

	public static final String SPLIT = ",";

	public static final String SPLIT_ROW = ";";
	private static final YuiResultMapper<SysUserLog> MAPPING = new YuiResultMapper<SysUserLog>() {
		@Override
		public Map<String, Object> toMap(SysUserLog obj) {
			Map<String, Object> m = WxlSugar.newHashMap();
			m.put("eventTime", DateExTool.dateTime2String(obj.getEventTime()));
			m.put("id", obj.getId());
			m.put("name", obj.getSysUser().getLoginId() + "("
					+ obj.getSysUser().getName() + ")");
			m.put("event", obj.getEvent());
			m.put("extendInfo", StringUtils.defaultString(obj.getExtendInfo()));
			m.put("ip", obj.getIp());
			return m;
		}
	};
	@Resource
	private SysUserLogDao sysUserLogDao;
	@Resource
	private ParameterKeeper parameterKeeper;
	@Resource
	private Mls mls;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER_LOGS)
	public ModelAndView entry(
			@ObjectParam("condition") SysUserLogCondition condition) {

		if (condition == null) {
			condition = new SysUserLogCondition();
			condition.setBeginDate(DateExTool.getToday());
			condition.setEndDate(DateExTool.getToday());
		}

		return new ModelAndView().addObject("condition", condition);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.SYS_USER_LOGS)
	public YuiResult list(
			@ObjectParam("condition") SysUserLogCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new SysUserLogCondition();
		}
		PaginationSupport<SysUserLog> results = null;
		results = sysUserLogDao.search(condition, pageIndex);
		return YuiResult.create(results, MAPPING);
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.SYS_USER_LOGS)
	public String clear(@ObjectParam("condition") SysUserLogCondition condition) {
		sysUserLogDao.deleteSysUserLog(condition);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.SYS_USER_LOGS)
	public AttachmentFile export(
			@ObjectParam("condition") SysUserLogCondition condition) {
		AttachmentFile exportFile;
		String fileName = RandomTool.getRandomString(5);
		String exportFileName = DateExTool.formatDateTime(
				Calendar.getInstance(), "yyyy-MM-dd_HHmmss")
				+ ".CSV";

		String tmpFile = this.parameterKeeper
				.getFilePath(ParameterHelper.WORK_DIR) + fileName;

		exportFile = new AttachmentFile();
		exportFile.setAttachment(true);
		exportFile.setMediaType(PuertaWeb.CSV);
		exportFile.setFileName(exportFileName);
		exportFile.setBurnAfterReading(true);
		exportFile.setFilePath(tmpFile);

		if (condition == null) {
			condition = new SysUserLogCondition();
		}

		List<SysUserLog> l = sysUserLogDao.loadLogs(condition);
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(tmpFile), mls.t("DefaultCharset")));
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}// ATTN
			// UTF
			// -
			// 8
			// is
			// not
		// supported by Excel

		if (printWriter == null) {
			Pe.raise("创建文件失败，请联系管理员");
		}

		printWriter.println(mls.t("platform.userlog.exportTitles"));
		int i = 0;
		for (SysUserLog ul : l) {

			printWriter.print(DateExTool.dateTime2String(ul.getEventTime()));
			printWriter.print(SPLIT);
			printWriter.print(toCsv(ul.getSysUser().getId()));
			printWriter.print(SPLIT);
			printWriter.print(toCsv(ul.getSysUser().getName()));
			printWriter.print(SPLIT);
			printWriter.print(toCsv(ul.getIp()));
			printWriter.print(SPLIT);
			printWriter.print(toCsv(ul.getEvent()));
			printWriter.print(SPLIT);

			printWriter
					.print(toCsv(StringUtils.trimToEmpty(ul.getExtendInfo())));
			printWriter.println();
			i++;
		}
		printWriter.close();

		return exportFile;

	}

	private String toCsv(String str) {
		String tmp = str;
		if (StringUtils.contains(str, '"')) {
			tmp = StringUtils.replace(str, "\"", "\"\"");
		}
		if (StringUtils.contains(tmp, ',')) {
			return "\"" + tmp + "\"";
		}
		return tmp;
	}

}
