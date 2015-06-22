package pula.sys.app;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
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
import puerta.support.utils.FileHelper;
import puerta.support.utils.JacksonUtil;
import puerta.support.utils.WxlSugar;
import puerta.support.vo.SelectOptionList;
import puerta.system.keeper.ParameterKeeper;
import puerta.system.service.SessionService;
import puerta.system.vo.JsonResult;
import puerta.system.vo.MapBean;
import puerta.system.vo.MapList;
import puerta.system.vo.YuiResult;
import puerta.system.vo.YuiResultMapper;
import pula.sys.BaseHelper;
import pula.sys.BhzqConstants;
import pula.sys.PurviewConstants;
import pula.sys.conditions.TeacherAssignmentCondition;
import pula.sys.conditions.TeacherCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.CardDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.SysUserDao;
import pula.sys.daos.TeacherAssignmentDao;
import pula.sys.daos.TeacherCardDao;
import pula.sys.daos.TeacherDao;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.SysUser;
import pula.sys.domains.Teacher;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.forms.TeacherForm;
import pula.sys.helpers.TeacherHelper;
import pula.sys.services.SessionUserService;

@Controller
public class TeacherController {

	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(TeacherController.class);
	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {
		@Override
		public Map<String, Object> toMap(MapBean obj) {

			obj.add("statusName",
					TeacherHelper.getStatusName(obj.asInteger("status")))
					.add("genderName",
							BaseHelper.getGenderName(obj.asInteger("gender")))
					.add("levelName",
							TeacherHelper.getLevelName(obj.asInteger("level")));

			return obj;
		}
	};

	// private static final YuiResultMapper<Teacher> MAPPING_FULL = new
	// YuiResultMapper<Teacher>() {
	// @Override
	// public Map<String, Object> toMap(Teacher obj) {
	//
	// Map<String, Object> m = MAPPING.toMap(obj);
	// m.put("liveAddress", obj.getLiveAddress());
	// m.put("hjAddress", obj.getHjAddress());
	// m.put("identity", obj.getIdentity());
	// m.put("school", obj.getSchool());
	// m.put("homeplace", obj.getHomeplace());
	//
	// return m;
	// }
	// };

	@Resource
	TeacherDao teacherDao;
	@Resource
	ParameterKeeper parameterKeeper;
	@Resource
	SessionService sessionService;
	@Resource
	SessionUserService sessionUserService;
	@Resource
	FileAttachmentDao fileAttachmentDao;
	@Resource
	SysUserDao sysUserDao;
	@Resource
	BranchDao branchDao;
	@Resource
	CardDao cardDao;

	@Resource
	TeacherAssignmentDao teacherAssignmentDao;
	@Resource
	TeacherCardDao teacherCardDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER)
	public ModelAndView entry(
			@ObjectParam("condition") TeacherCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(TeacherCondition condition) {
		if (condition == null) {
			condition = new TeacherCondition();
		}

		// List<Department> mts = departmentDao.loadByTree();

		SelectOptionList genders = BaseHelper.getGenderList(0);
		SelectOptionList statusList = TeacherHelper
				.getStatusList(Teacher.ON_DUTY);

		SelectOptionList levelList = TeacherHelper.getLevelList(0);

		MapList branchList = branchDao.loadMeta();
		SelectOptionList enabledStatusList = PuertaWeb.getYesNoList(
				PuertaWeb.YES, new String[] { "有效", "无效" });

		return new ModelAndView().addObject("condition", condition)
				.addObject("genders", genders)
				.addObject("branches", branchList)
				.addObject("levels", levelList)
				.addObject("statusList", statusList)
				.addObject("enabledStatusList", enabledStatusList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER)
	public YuiResult list(
			@ObjectParam("condition") TeacherCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new TeacherCondition();
		}
		// PaginationSupport<Teacher> results = null;
		// results = teacherDao.search(condition, pageIndex);

		PaginationSupport<MapBean> results = teacherDao.searchMapBean(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Barrier(PurviewConstants.TEACHER)
	public ModelAndView create() {
		SelectOptionList genders = BaseHelper.getGenderList(0);
		SelectOptionList statusList = TeacherHelper
				.getStatusList(Teacher.ON_DUTY);
		SelectOptionList levelList = TeacherHelper.getLevelList(0);

		return new ModelAndView().addObject("genders", genders)
				.addObject("statusList", statusList)
				.addObject("updateMode", false).addObject("levels", levelList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER)
	public ModelAndView update(@RequestParam("id") Long id) {
		Teacher u = teacherDao.findById(id);

		SelectOptionList genders = BaseHelper.getGenderList(0);
		SelectOptionList statusList = TeacherHelper
				.getStatusList(Teacher.ON_DUTY);

		List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(
				u.toRefId(), FileAttachment.TYPE_TEACHER_ICON);

		// extract the icon
		FileAttachment icon = null;
		for (FileAttachment a : attachments) {
			if (!a.isRemoved()
					&& a.getType() == FileAttachment.TYPE_TEACHER_ICON) {
				icon = a;
				break;
			}
		}

		if (u.getUpdater() != null) {
			u.getUpdater().getLoginId();
		}

		SelectOptionList levelList = TeacherHelper.getLevelList(0);
		return new ModelAndView().addObject("genders", genders)
				.addObject("attachments", attachments)
				.addObject("statusList", statusList).addObject("icon", icon)
				.addObject("updateMode", true).addObject("teacher", u)
				.addObject("levels", levelList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER)
	public ModelAndView view(@RequestParam("id") Long id) {
		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		}
		Teacher u = teacherDao.findById(id);

		List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(
				u.toRefId(), FileAttachment.TYPE_TEACHER_ICON);

		// extract the icon
		FileAttachment icon = null;
		for (FileAttachment a : attachments) {
			if (!a.isRemoved()
					&& a.getType() == FileAttachment.TYPE_TEACHER_ICON) {
				icon = a;
				break;
			}
		}

		if (u.getUpdater() != null) {
			u.getUpdater().getLoginId();
		}

		return new ModelAndView().addObject("icon", icon).addObject("id", id)
				.addObject("updateMode", true).addObject("teacher", u)
				.addObject("attachments", attachments)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.TEACHER)
	public String _create(@ObjectParam("teacher") TeacherForm cli,
			@RequestParam("jsonAttachment") String jsonAttachment) {

		Teacher cc = cli.toTeacher();

		Long cardId = prepareData(cli, jsonAttachment, cc);

		List<FileAttachmentForm> attachmentForms = cli.getAttachmentForms();

		List<FileAttachment> attachments = processFile(attachmentForms);

		// 然后生成真实对象
		cc.setCreator(SysUser.create(sessionUserService.getActorId()));
		cc.setBarcode(cli.getCardNo());
		Teacher ef = teacherDao.save(cc);

		fileAttachmentDao.save(ef, attachments, false);

		if (cardId != null) {
			boolean saved = teacherCardDao.saveIfNeed(ef, cardId, true);

			if (saved) {
				cardDao.takeBy(TeacherHelper.buildRefId(ef.getId()),
						TeacherHelper.buildComments(ef), cardId);
			}
		}

		// card
		// precheck

		return ViewResult.JSON_SUCCESS;
	}

	private List<FileAttachment> processFile(
			List<FileAttachmentForm> attachmentForms) {
		List<FileAttachment> attachments = WxlSugar.newArrayList();

		String filePath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_TEACHER_DIR);

        FileHelper.mkdir(filePath);
		String srcPath = parameterKeeper
				.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);

		if (attachmentForms != null) {

			// 文件要复制！
			for (FileAttachmentForm a : attachmentForms) {

				if (StringUtils.isEmpty(a.getFileId())) {
					continue;
				}
				FileAttachment fa = new FileAttachment();
				fa.setExtName(FilenameUtils.getExtension(a.getFileName()));
				fa.setName(a.getFileName());
				fa.setType(a.getType());
				fa.setFileId(a.getFileId());

				if (a.getId() == 0) {

					// build

					String dest = filePath + File.separatorChar + a.getFileId();
					File f = new File(srcPath + File.separatorChar
							+ a.getFileId());
					f.renameTo(new File(dest));

				} else {
					fa.setId(a.getId());
				}
				attachments.add(fa);

			}
		}
		return attachments;
	}

	private Long prepareData(TeacherForm cli, String jsonAttachment, Teacher cc) {

		List<FileAttachmentForm> items = null;
		try {
			items = JacksonUtil.getList(jsonAttachment,
					FileAttachmentForm.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("jsonAttachment=" + jsonAttachment);
			Pe.raise(e.getMessage());
		}

		cli.setAttachmentForms(items);

		if (!StringUtils.isEmpty(cli.getCardNo())) {
			// precheck card
			Long cardId = cardDao.getId4Use(cli.getCardNo(),
					TeacherHelper.buildRefId(cc.getId()));

			if (cardId == null) {
				Pe.raise("指定的卡号不存在或已经使用:" + cli.getCardNo());
			}

			return cardId;

		}
		return null;
	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.TEACHER)
	public String _update(@ObjectParam("teacher") TeacherForm cli,
			@RequestParam("jsonAttachment") String jsonAttachment) {

		Teacher cc = cli.toTeacher();

		Long cardId = prepareData(cli, jsonAttachment, cc);

		List<FileAttachmentForm> attachmentForms = cli.getAttachmentForms();

		List<FileAttachment> attachments = processFile(attachmentForms);

		// 然后生成真实对象
		cc.setUpdater(SysUser.create(sessionUserService.getActorId()));
		cc.setBarcode(cli.getCardNo());
		Teacher ef = teacherDao.update(cc, cli.isChangePassword());

		fileAttachmentDao.save(ef, attachments, true);

		if (cardId != null) {
			boolean saved = teacherCardDao.saveIfNeed(ef, cardId, false);
			if (saved) {
				cardDao.takeBy(TeacherHelper.buildRefId(ef.getId()),
						TeacherHelper.buildComments(ef), cardId);
			}
		} else {
			teacherCardDao.cancel(ef);
		}

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.TEACHER)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		teacherDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER)
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = teacherDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER)
	public AttachmentFile icon(
			@RequestParam(value = "fp", required = false) String fp,
			@RequestParam(value = "id", required = false) Long id,
			HttpServletResponse res) {

		String srcPath = null;
		if (id == null || id == 0) {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
		} else {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_TEACHER_DIR);
		}
		String fullPath = srcPath + File.separatorChar + fp;

		return AttachmentFile.forShow(new File(fullPath));
	}

	@RequestMapping
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER)
	public AttachmentFile file(
			@RequestParam(value = "fp", required = false) String fp,
			@RequestParam(value = "id", required = false) Long id,
			HttpServletResponse res) {
		String srcPath = null;
		if (id == null || id == 0) {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_UPLOAD_DIR);
		} else {
			srcPath = parameterKeeper
					.getFilePath(BhzqConstants.FILE_TEACHER_DIR);
		}

		String fullPath = srcPath + File.separatorChar + fp;

		File f = new File(fullPath);

		if (!f.exists()) {
			res.setStatus(404);
			return null;
		}
		logger.debug("file=" + f.getAbsolutePath());
		AttachmentFile af = AttachmentFile.attach(f,
				AttachmentFile.APPLICATION_UNKNOWN);
		af.setBurnAfterReading(false);

		return af;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.TEACHER)
	@ResponseBody
	public JsonResult _assign(@RequestParam("id") long id,
			@RequestParam("branchId") long branchId) {

		if (teacherAssignmentDao.isCurrent(id, branchId)) {
			Pe.raise("当前已绑定指定的分支机构");
		}
		teacherAssignmentDao.save(id, branchId, sessionService.getActorId());

		return JsonResult.s();
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.TEACHER_SEARCH)
	public ModelAndView search(
			@ObjectParam("condition") TeacherCondition condition) {
		return _entry(condition).addObject("headquarter",
				sessionUserService.isHeadquarter());

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.TEACHER_SEARCH)
	public YuiResult list4Search(
			@ObjectParam("condition") TeacherCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new TeacherCondition();
		}
		// PaginationSupport<Teacher> results = null;
		// results = teacherDao.search(condition, pageIndex);

		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());

		}

		PaginationSupport<MapBean> results = teacherDao.searchMapBean(
				condition, pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	// @RequestMapping
	// @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	// public ModelAndView performance(HttpServletResponse res) {
	//
	// try {
	// res.sendRedirect("/pula-sys/html/teacher_performance.html");
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return null;
	// }

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public ModelAndView assignHistory(@RequestParam("id") Long id) {

		if (!sessionUserService.isHeadquarter()) {
			Pe.raise("越权访问");
		}

		MapBean meta = teacherDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的教师");
		}
		return new ModelAndView().addObject("id", id)
				.addObject("teacher_meta", meta)
				.addObject("headquarter", sessionUserService.isHeadquarter());

	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier({ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public YuiResult list4AssignHistory(
			@ObjectParam("condition") TeacherAssignmentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		// if (condition == null) {
		// condition = new SysCategoryCondition();
		// }
		// PaginationSupport<SysCategory> results = null;

		// WARN:必须传入teacher_id
		// 分校还得传入分校ID

		// MapList ml = new MapList();
		// Calendar now = Calendar.getInstance();
		// ml.add(MapBean.map("createdTime", now).add("branchName", "金桥分校")
		// .add("teacherName", "王老师").add("current", true)
		// .add("assignerName", "张三"));
		//
		// ml.add(MapBean
		// .map("createdTime", DateJedi.create(now).moveDay(-1).to())
		// .add("branchName", "碧云分校").add("teacherName", "王老师")
		// .add("current", true).add("assignerName", "张三"));

		if (!sessionUserService.isHeadquarter()) {
			Pe.raise("越权访问");
		}
		if (condition == null) {
			condition = new TeacherAssignmentCondition();
		}

		PaginationSupport<MapBean> results = teacherAssignmentDao.search(
				condition, pageIndex);

		return YuiResult.create(results);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier({ PurviewConstants.TEACHER, PurviewConstants.TEACHER_SEARCH })
	public ModelAndView personalLogs(@RequestParam("id") Long id) {

		MapList branchList = null;
		if (!sessionUserService.isHeadquarter()) {
			teacherDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		} else {
			branchList = branchDao.loadMeta();
		}
		MapBean meta = teacherDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的教师");
		}
		return new ModelAndView().addObject("condition", new Object())
				.addObject("id", id).addObject("teacher_meta", meta)
				.addObject("branchList", branchList)
				.addObject("headquarter", sessionUserService.isHeadquarter());

	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.TEACHER)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		teacherDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier()
	public ModelAndView find(
			@RequestParam(value = "no", required = false) String no) {
		ModelAndView m = new ModelAndView(ViewResult.JSON_LIST);
		long branchId = 0;

		if (!sessionUserService.isHeadquarter()) {
			branchId = sessionUserService.getBranch().getIdLong();
		}
		MapList list = teacherDao.loadByKeywords(no, branchId);
		m.addObject("list", list);
		return m;
	}
}
