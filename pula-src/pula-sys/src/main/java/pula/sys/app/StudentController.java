package pula.sys.app;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import puerta.support.utils.JacksonUtil;
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
import pula.sys.conditions.StudentCondition;
import pula.sys.daos.BranchDao;
import pula.sys.daos.CardDao;
import pula.sys.daos.CounterDao;
import pula.sys.daos.FileAttachmentDao;
import pula.sys.daos.OrderFormDao;
import pula.sys.daos.StudentCardDao;
import pula.sys.daos.StudentDao;
import pula.sys.daos.SysUserDao;
import pula.sys.domains.Branch;
import pula.sys.domains.FileAttachment;
import pula.sys.domains.Student;
import pula.sys.domains.SysUser;
import pula.sys.forms.FileAttachmentForm;
import pula.sys.forms.StudentForm;
import pula.sys.helpers.StudentHelper;
import pula.sys.services.SessionUserService;
import pula.sys.util.FileUtil;

@Controller
public class StudentController {

	private static final YuiResultMapper<MapBean> MAPPING_FIX = new YuiResultMapper<MapBean>() {
		@Override
		public Map<String, Object> toMap(MapBean obj) {

			obj.add("genderName",
					BaseHelper.getGenderName(obj.asInteger("gender")));

			return obj;
		}
	};

	@Resource
	StudentDao studentDao;
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
	CounterDao counterDao;
	@Resource
	CardDao cardDao;
	@Resource
	StudentCardDao studentCardDao;
	@Resource
	OrderFormDao orderFormDao;

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT)
	public ModelAndView entry(
			@ObjectParam("condition") StudentCondition condition) {
		return _entry(condition);

	}

	private ModelAndView _entry(StudentCondition condition) {
		if (condition == null) {
			condition = new StudentCondition();
		}

		SelectOptionList genders = BaseHelper.getGenderList(0);

		// SelectOptionList levelList = StudentHelper.getLevelList(0);

		MapList branchList = branchDao.loadMeta();

		SelectOptionList statusList = PuertaWeb.getYesNoList(PuertaWeb.YES,
				new String[] { "有效", "无效" });

		return new ModelAndView().addObject("condition", condition)
				.addObject("genders", genders)
				.addObject("branches", branchList)
				// .addObject("levels", levelList)
				.addObject("statusList", statusList)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT)
	public YuiResult list(
			@ObjectParam("condition") StudentCondition condition,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex) {
		if (condition == null) {
			condition = new StudentCondition();
		}
		// PaginationSupport<Student> results = null;
		// results = studentDao.search(condition, pageIndex);
		if (!sessionUserService.isHeadquarter()) {
			condition.setBranchId(sessionUserService.getBranch().getIdLong());
		}

		PaginationSupport<MapBean> results = studentDao.search(condition,
				pageIndex);

		return YuiResult.create(results, MAPPING_FIX);
	}

	@RequestMapping
	@Barrier(PurviewConstants.STUDENT)
	public ModelAndView create() {
		SelectOptionList genders = BaseHelper.getGenderList(0);
		/*
		 * SelectOptionList statusList = StudentHelper
		 * .getStatusList(Student.ON_DUTY); SelectOptionList levelList =
		 * StudentHelper.getLevelList(0);
		 */
		return new ModelAndView().addObject("genders", genders)
		// .addObject("statusList", statusList)
				.addObject("updateMode", false);// .addObject("levels",
												// levelList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT)
	public ModelAndView update(@RequestParam("id") Long id) {
		Student u = studentDao.findById(id);

		SelectOptionList genders = BaseHelper.getGenderList(0);
		// SelectOptionList statusList = StudentHelper
		// .getStatusList(Student.ON_DUTY);

		List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(
				u.toRefId(), FileAttachment.TYPE_STUDENT_ICON);

		// extract the icon
		FileAttachment icon = null;
		for (FileAttachment a : attachments) {
			if (!a.isRemoved()
					&& a.getType() == FileAttachment.TYPE_STUDENT_ICON) {
				icon = a;
				break;
			}
		}

		if (u.getUpdater() != null) {
			u.getUpdater().getLoginId();
		}

		// SelectOptionList levelList = StudentHelper.getLevelList(0);
		return new ModelAndView().addObject("genders", genders)
				.addObject("attachments", attachments)
				// .addObject("statusList", statusList)
				.addObject("icon", icon).addObject("updateMode", true)
				.addObject("student", u);
		// .addObject("levels", levelList);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT)
	public ModelAndView view(@RequestParam("id") Long id) {
		Student u = studentDao.findById(id);

		List<FileAttachment> attachments = fileAttachmentDao.loadByRefId(
				u.toRefId(), FileAttachment.TYPE_STUDENT_ICON);

		// extract the icon
		FileAttachment icon = null;
		for (FileAttachment a : attachments) {
			if (!a.isRemoved()
					&& a.getType() == FileAttachment.TYPE_STUDENT_ICON) {
				icon = a;
				break;
			}
		}

		if (u.getUpdater() != null) {
			u.getUpdater().getLoginId();
		}

		return new ModelAndView().addObject("icon", icon).addObject("id", id)
				.addObject("updateMode", true).addObject("student", u)
				.addObject("attachments", attachments)
				.addObject("headquarter", sessionUserService.isHeadquarter());
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.STUDENT)
	public String _create(@ObjectParam("student") StudentForm cli,
			@RequestParam("jsonAttachment") String jsonAttachment) {

		Student cc = cli.toStudent();

		long branchId = sessionUserService.getBranch().getIdLong();
		if (sessionUserService.isHeadquarter()) {
			// Pe.raise("总部不能直接创建学生信息");
			// TODO: 测试
		} else {
			// 分支机构

		}

		String branchNo = branchDao.getPrefix(branchId);
		// 学生编号生成
		// 从自己的位置生成
		// 找到自己的计数器
        if (StringUtils.isEmpty(cc.getNo())) {
            int counter = 1;
            synchronized (BhzqConstants.STUDENT_SYNC) {
                counter = counterDao.inc(String.valueOf(branchId), BhzqConstants.COUNTER_STUDENT);
            }

            cc.setNo(StudentHelper.makeNo(branchNo, counter));
        }
		cc.setBranch(Branch.create(branchId));

		Long cardId = prepareData(cli, jsonAttachment, cc);

		List<FileAttachmentForm> attachmentForms = cli.getAttachmentForms();

		List<FileAttachment> attachments = FileUtil.processFile(parameterKeeper
                .getFilePath(BhzqConstants.FILE_STUDENT_DIR), parameterKeeper, attachmentForms);

		// 然后生成真实对象
		cc.setCreator(SysUser.create(sessionUserService.getActorId()));
		cc.setBarcode(cli.getCardNo());
		Student ef = studentDao.save(cc);

		fileAttachmentDao.save(ef, attachments, false);
		if (cardId != null) {
			boolean saved = studentCardDao.saveIfNeed(ef, cardId, true);

			if (saved) {
				cardDao.takeBy(StudentHelper.buildRefId(ef.getId()),
						StudentHelper.buildComments(ef), cardId);
			}
		}

		return ViewResult.JSON_SUCCESS;
	}

	private Long prepareData(StudentForm cli, String jsonAttachment, Student cc) {

		List<FileAttachmentForm> items = null;
		try {
			items = JacksonUtil.getList(jsonAttachment,
					FileAttachmentForm.class);
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error("jsonAttachment=" + jsonAttachment);
			Pe.raise(e.getMessage());
		}

		cli.setAttachmentForms(items);
		if (!StringUtils.isEmpty(cli.getCardNo())) {
			// precheck card
			Long cardId = cardDao.getId4Use(cli.getCardNo(),
					StudentHelper.buildRefId(cc.getId()));

			if (cardId == null) {
				Pe.raise("指定的卡号不存在或已经使用:" + cli.getCardNo());
			}

			return cardId;

		}
		return null;

	}

	@RequestMapping
	@Transactional()
	@Barrier(PurviewConstants.STUDENT)
	public String _update(@ObjectParam("student") StudentForm cli,
			@RequestParam("jsonAttachment") String jsonAttachment) {

		Student cc = cli.toStudent();

		Long cardId = prepareData(cli, jsonAttachment, cc);

		List<FileAttachmentForm> attachmentForms = cli.getAttachmentForms();

		List<FileAttachment> attachments = FileUtil.processFile(parameterKeeper
                .getFilePath(BhzqConstants.FILE_STUDENT_DIR), parameterKeeper, attachmentForms);

		// 然后生成真实对象
		cc.setUpdater(SysUser.create(sessionUserService.getActorId()));
		cc.setBarcode(cli.getCardNo());
		Student ef = studentDao.update(cc, cli.isChangePassword());

		fileAttachmentDao.save(ef, attachments, true);
		if (cardId != null) {
			boolean saved = studentCardDao.saveIfNeed(ef, cardId, false);
			if (saved) {
				cardDao.takeBy(StudentHelper.buildRefId(ef.getId()),
						StudentHelper.buildComments(ef), cardId);
			}
		} else {
			studentCardDao.cancel(ef);
		}

		return ViewResult.JSON_SUCCESS;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.STUDENT)
	public String remove(
			@RequestParam(value = "objId", required = false) Long[] id) {
		if (!sessionUserService.isHeadquarter()) {
			studentDao.checkOwner(id, sessionUserService.getBranch()
					.getIdLong());
		}

		studentDao.deleteById(id);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT)
	public JsonResult get(@RequestParam("id") Long id) {
		MapBean u = studentDao.unique(id);
		return JsonResult.s(u);
	}

	@RequestMapping
	@ResponseBody
	@Barrier(check = false, value = { PurviewConstants.STUDENT,
			PurviewConstants.ORDERFORM })
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
					.getFilePath(BhzqConstants.FILE_STUDENT_DIR);
		}
		String fullPath = srcPath + File.separatorChar + fp;

		return AttachmentFile.forShow(new File(fullPath));
	}

	@RequestMapping
	@ResponseBody
	@Barrier(PurviewConstants.STUDENT)
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
					.getFilePath(BhzqConstants.FILE_STUDENT_DIR);
		}

		String fullPath = srcPath + File.separatorChar + fp;

		File f = new File(fullPath);

		if (!f.exists()) {
			res.setStatus(404);
			return null;
		}
		// logger.debug("file=" + f.getAbsolutePath());
		AttachmentFile af = AttachmentFile.attach(f,
				AttachmentFile.APPLICATION_UNKNOWN);
		af.setBurnAfterReading(false);

		return af;
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
		MapList list = studentDao.loadByKeywords(no, branchId);
		m.addObject("list", list);
		return m;
	}

	@Transactional
	@RequestMapping
	@Barrier(PurviewConstants.STUDENT)
	public String enable(
			@RequestParam(value = "objId", required = false) Long[] id,
			@RequestParam(value = "enable", required = false) boolean enable) {
		if (!sessionUserService.isHeadquarter()) {
			studentDao.checkOwner(id, sessionUserService.getBranch()
					.getIdLong());
		}
		studentDao.doEnable(id, enable);
		return ViewResult.JSON_SUCCESS;
	}

	@RequestMapping
	@Transactional
	@Barrier(PurviewConstants.STUDENT)
	@ResponseBody
	public JsonResult _createLite(@ObjectParam("student") StudentForm cli) {

		Student cc = cli.toStudent();

		long branchId = sessionUserService.getBranch().getIdLong();
		if (sessionUserService.isHeadquarter()) {
			// Pe.raise("总部不能直接创建学生信息");
			// TODO: 测试
		} else {
			// 分支机构

		}

		String branchNo = branchDao.getPrefix(branchId);
		// 学生编号生成
		// 从自己的位置生成
		// 找到自己的计数器
		int counter = 1;
		synchronized (BhzqConstants.STUDENT_SYNC) {
			counter = counterDao.inc(String.valueOf(branchId),
					BhzqConstants.COUNTER_STUDENT);
		}

		cc.setNo(StudentHelper.makeNo(branchNo, counter));
		cc.setBranch(Branch.create(branchId));

		// Long cardId = prepareData(cli, jsonAttachment, cc);

		// List<FileAttachmentForm> attachmentForms = cli.getAttachmentForms();
		//
		// List<FileAttachment> attachments = processFile(attachmentForms);

		// 然后生成真实对象
		cc.setCreator(SysUser.create(sessionUserService.getActorId()));
		// cc.setBarcode(cli.getCardNo());
		Student ef = studentDao.save(cc);

		// 没有附件也没有卡，单纯保存一个用户信息
		// fileAttachmentDao.save(ef, attachments, false);
		// if (cardId != null) {
		// boolean saved = studentCardDao.saveIfNeed(ef, cardId, true);
		//
		// if (saved) {
		// cardDao.takeBy(StudentHelper.buildRefId(ef.getId()),
		// StudentHelper.buildComments(ef), cardId);
		// }
		// }

		return JsonResult.s(MapBean.map("no", ef.getNo())
				.add("name", ef.getName()).add("birthday", ef.getBirthday())
				.add("genderName", ef.getGenderName()));
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@ResponseBody
	@Barrier(check = false, value = { PurviewConstants.STUDENT,
			PurviewConstants.ORDERFORM_CREATE, PurviewConstants.ORDERFORM })
	public JsonResult getByNo(
			@RequestParam("no") String no,
			@RequestParam(value = "orderFormId", required = false) Long orderFormId) {
		MapBean u = studentDao.metaByNo(no, sessionUserService.getBranch()
				.getIdLong());

		if (u == null) {
			Pe.raise("指定的学员编号无法找到:" + no);
		}

		if (orderFormId != null) {

			String orderform_no = orderFormDao.getOpenOrderForm(u.asLong("id"),
					orderFormId);
			if (orderform_no != null) {
				Pe.raise("指定的学员" + u.string("name") + "已签署了订单[" + orderform_no
						+ "],其中课程尚未完成");
			}

		}

		FileAttachment fa = fileAttachmentDao.getByRefId(
				StudentHelper.buildFileRefId(u.asLong("id"),
						u.string("attachmentKey")),
				FileAttachment.TYPE_STUDENT_ICON);

		Map<String, Object> m = MAPPING_FIX.toMap(u);
		if (fa != null) {
			m.put("iconId", fa.getId());
			m.put("iconFileId", fa.getFileId());
		}

		return JsonResult.s(m);
	}

	@RequestMapping
	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	@Barrier(PurviewConstants.STUDENT)
	public ModelAndView personalLogs(@RequestParam("id") Long id) {

		MapList branchList = null;
		if (!sessionUserService.isHeadquarter()) {
			studentDao.checkAllowView(id, sessionUserService.getBranch()
					.getIdLong());
		} else {
			branchList = branchDao.loadMeta();
		}

		MapBean meta = studentDao.meta(id);
		if (meta == null) {
			Pe.raise("找不到指定的学生");
		}
		return new ModelAndView().addObject("condition", new Object())
				.addObject("branchList", branchList).addObject("id", id)
				.addObject("student_meta", meta)
				.addObject("headquarter", sessionUserService.isHeadquarter());

	}
}
